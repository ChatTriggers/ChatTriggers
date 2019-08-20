package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.ILoader.Companion.modulesFolder
import com.chattriggers.ctjs.engine.IRegister
import com.chattriggers.ctjs.engine.langs.Lang
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.utils.console.Console
import org.mozilla.javascript.Context
import org.mozilla.javascript.Function
import org.mozilla.javascript.Scriptable
import java.io.File
import java.net.URL

object JSLoader : ILoader {
    override var triggers = mutableListOf<OnTrigger>()
    override val toRemove = mutableListOf<OnTrigger>()
    override val console by lazy { Console(this) }

    private val cachedModules = mutableListOf<Module>()
    private lateinit var moduleContext: Context
    private lateinit var evalContext: Context
    private lateinit var scope: Scriptable

    override fun load(modules: List<Module>) {
        cachedModules.clear()

        if (::moduleContext.isInitialized) {
            Context.exit()
        }

        val jars = modules.map {
            it.folder.listFiles()?.toList() ?: listOf()
        }.flatten().filter {
            it.name.endsWith(".jar")
        }.map {
            it.toURI().toURL()
        }

        instanceContexts(jars)

        val providedLibs = saveResource(
                "/providedLibs.js",
                File(modulesFolder.parentFile,
                        "chattriggers-provided-libs.js"
                ),
                true
        )

        JSContextFactory.enterContext(moduleContext)

        try {
            moduleContext.evaluateString(
                scope,
                providedLibs,
                "provided",
                1, null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            console.printStackTrace(e)
            return
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
        IRegister.currentModule = module

        val files = module.getFilesWithExtension(".js")

        files.forEach {
            try {
                moduleContext.evaluateString(
                    scope,
                    it.readText(),
                    it.absolutePath.substringAfter(ILoader.modulesFolder.absolutePath),
                    1, null
                )
            } catch (e: Exception) {
                console.printStackTrace(e)
            }
        }

        IRegister.currentModule = null
    }

    override fun eval(code: String): String? {
        JSContextFactory.enterContext(evalContext)
        try {
            return Context.toString(evalContext.evaluateString(scope, code, "<eval>", 1, null))
        } finally {
            Context.exit()
        }
    }

    override fun getLanguage() = Lang.JS

    override fun trigger(trigger: OnTrigger, method: Any, vararg args: Any?) {
        try {
            if (method !is Function) throw ClassCastException("Need to pass actual function to the register function, not the name!")

            method.call(moduleContext, scope, scope, args)
        } catch (e: Exception) {
            console.printStackTrace(e)
            removeTrigger(trigger)
        }
    }

    override fun getModules(): List<Module> {
        return cachedModules
    }

    private fun instanceContexts(files: List<URL>) {
        JSContextFactory.addAllURLs(files)

        moduleContext = JSContextFactory.enterContext()
        scope = moduleContext.initStandardObjects()
        Context.exit()

        JSContextFactory.optimize = false
        evalContext = JSContextFactory.enterContext()
        Context.exit()
        JSContextFactory.optimize = true
    }
}