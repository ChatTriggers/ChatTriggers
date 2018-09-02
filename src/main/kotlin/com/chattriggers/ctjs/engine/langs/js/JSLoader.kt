package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.ILoader.Companion.modulesFolder
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.kotlin.ModuleLoader
import jdk.nashorn.api.scripting.NashornScriptEngine
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import jdk.nashorn.api.scripting.ScriptObjectMirror
import jdk.nashorn.internal.objects.Global
import net.minecraft.client.Minecraft
import org.apache.commons.io.FileUtils
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@ModuleLoader
object JSLoader : ILoader {
    private val triggers = mutableListOf<OnTrigger>()
    private var global: Global? = null
    private val cachedModules = mutableListOf<Module>()
    private var scriptEngine: NashornScriptEngine
    private val console = Console(this)

    init {
        scriptEngine = instanceScriptEngine(listOf())
    }

    override fun load(modules: List<Module>) {
        val jars = modules.map {
            it.folder.listFiles().toList()
        }.flatten().filter {
            it.name.endsWith(".jar")
        }.map {
            it.toURI().toURL()
        }

        instanceScriptEngine(jars)

        val script = saveResource(
                "/providedLibs.js",
                File(modulesFolder.parentFile,
                        "chattriggers-provided-libs.js"
                ),
                true
        )

        try {
            scriptEngine.eval(script)
        } catch (e: Exception) {
            console.printStackTrace(e)
        }

        cachedModules.clear()

        val combinedScript = modules.map {
            it.getFilesWithExtension(".js")
        }.flatten().joinToString(separator = "\n") {
            FileUtils.readFileToString(it)
        }

        try {
            scriptEngine.eval(combinedScript)
        } catch (e: Exception) {
            console.printStackTrace(e)
        }
    }

    override fun load(module: Module) {
        if (cachedModules.map {
            it.name
        }.contains(module.name)) return

        val script = module.getFilesWithExtension(".js").joinToString(separator = "\n") {
            FileUtils.readFileToString(it)
        }

        try {
            scriptEngine.eval(script)
        } catch (e: Exception) {
            console.out.println("Error loading module ${module.name}")
            console.printStackTrace(e)
        }
    }

    override fun exec(type: TriggerType, vararg args: Any) {
        triggers.filter {
            it.type == type
        }.forEach {
            it.trigger(*args)
        }
    }

    override fun eval(code: String): Any? {
        return scriptEngine.eval(code)
    }

    override fun addTrigger(trigger: OnTrigger) {
        triggers.add(trigger)

        triggers.sortBy {
            it.priority.ordinal
        }
    }

    override fun clearTriggers() {
        triggers.clear()
    }

    override fun getLanguageName(): String {
        return "js"
    }

    override fun trigger(trigger: OnTrigger, method: Any, vararg args: Any) {
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

    override fun removeTrigger(trigger: OnTrigger) {
        triggers.remove(trigger)
    }

    override fun getModules(): List<Module> {
        return cachedModules
    }

    override fun getConsole(): Console {
        return console
    }

    private fun callActualMethod(method: Any, vararg args: Any) {
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

    private fun callNamedMethod(method: String, vararg args: Any) {
        scriptEngine.invokeFunction(method, *args)
    }

    private fun instanceScriptEngine(files: List<URL>): NashornScriptEngine {
        val ucl = URLClassLoader(files.toTypedArray(), Minecraft::class.java.classLoader)

        return NashornScriptEngineFactory().getScriptEngine(ucl) as NashornScriptEngine
    }
}