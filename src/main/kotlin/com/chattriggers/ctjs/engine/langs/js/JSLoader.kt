package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.ILoader.Companion.modulesFolder
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.kotlin.ModuleLoader
import jdk.nashorn.api.scripting.NashornScriptEngine
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import jdk.nashorn.internal.objects.Global
import net.minecraft.client.Minecraft
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import javax.script.ScriptException
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@ModuleLoader
object JSLoader : ILoader {
    override var triggers = mutableListOf<OnTrigger>()
    override val toRemove = mutableListOf<OnTrigger>()
    override val console by lazy { Console(this) }

    private var global: Global? = null
    private val cachedModules = mutableListOf<Module>()
    private lateinit var scriptEngine: NashornScriptEngine

    override fun load(modules: List<Module>) {
        cachedModules.clear()

        val jars = modules.map {
            it.folder.listFiles()?.toList() ?: listOf()
        }.flatten().filter {
            it.name.endsWith(".jar")
        }.map {
            it.toURI().toURL()
        }

        scriptEngine = instanceScriptEngine(jars)

        val providedLibs = saveResource(
                "/providedLibs.js",
                File(modulesFolder.parentFile,
                        "chattriggers-provided-libs.js"
                ),
                true
        )

        try {
            scriptEngine.eval(providedLibs)
        } catch (e: Exception) {
            console.printStackTrace(e)
        }

        modules.forEach(::evalModule)

        cachedModules.addAll(modules)
    }

    override fun loadExtra(module: Module) {
        if (cachedModules.any {
            it.name == module.name
        }) return

        cachedModules.add(module)
        evalModule(module)
    }

    private fun evalModule(module: Module) {
        val files = module.getFilesWithExtension(".js")
        val text = files.joinToString("\n") {
            it.readText()
        }

        try {
            eval(text)
        } catch (e: ScriptException) {
            var line = e.lineNumber
            var fileName = ""

            for (file in files) {
                val fileLines = file.readLines().size

                if (line <= fileLines) {
                    fileName = file.name
                    break
                } else {
                    line -= fileLines
                }
            }

            console.out.println("Error loading module ${module.name} in $fileName:$line")
            console.printStackTrace(e)
        }
    }

    override fun eval(code: String): Any? {
        return scriptEngine.eval(code)
    }
    override fun getLanguageName(): List<String> {
        return listOf("js")
    }

    override fun trigger(trigger: OnTrigger, method: Any, vararg args: Any?) {
        try {
            if (method is String) {
                callNamedMethod(method, *args)
            } else {
                callActualMethod(method, *args)
            }
        } catch (e: Exception) {
            console.printStackTrace(e)
            removeTrigger(trigger)
        }
    }

    override fun getModules(): List<Module> {
        return cachedModules
    }

    private fun callActualMethod(method: Any, vararg args: Any?) {
        val som: ScriptObjectMirror = if (method is ScriptObjectMirror) {
            method
        } else {
            if (global == null) {
                val prop = NashornScriptEngine::class.memberProperties.firstOrNull {
                    it.name == "global"
                }!!

                prop.isAccessible = true
                global = prop.get(scriptEngine) as Global
            }

            val obj = ScriptObjectMirror.wrap(method, global)

            obj as ScriptObjectMirror
        }

        som.call(som, *args)
    }

    private fun callNamedMethod(method: String, vararg args: Any?) {
        scriptEngine.invokeFunction(method, *args)
    }

    private fun instanceScriptEngine(files: List<URL>): NashornScriptEngine {
        val ucl = URLClassLoader(files.toTypedArray(), Minecraft::class.java.classLoader)

        return NashornScriptEngineFactory().getScriptEngine(ucl) as NashornScriptEngine
    }
}