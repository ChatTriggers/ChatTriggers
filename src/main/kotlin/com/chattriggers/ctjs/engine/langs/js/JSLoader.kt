package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.loader.ILoader
import com.chattriggers.ctjs.engine.loader.ILoader.Companion.modulesFolder
import com.chattriggers.ctjs.engine.IRegister
import com.chattriggers.ctjs.engine.langs.Lang
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.utils.console.Console
import org.mozilla.javascript.Context
import org.mozilla.javascript.Function
import org.mozilla.javascript.ImporterTopLevel
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider
import org.mozilla.javascript.commonjs.module.Require
import org.mozilla.javascript.commonjs.module.provider.StrongCachingModuleScriptProvider
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider
import java.io.File
import java.lang.invoke.MethodHandles
import java.net.URI
import java.net.URL
import java.util.concurrent.CompletableFuture
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.javaMethod

object JSLoader : ILoader {
    override var triggers = mutableListOf<OnTrigger>()
    override val toRemove = mutableListOf<OnTrigger>()
    override val console by lazy { Console(this) }

    private val cachedModules = mutableListOf<Module>()
    private lateinit var moduleContext: Context
    private lateinit var evalContext: Context
    private lateinit var scope: Scriptable
    private lateinit var require: CTRequire

    override fun load(modules: List<Module>): CompletableFuture<Unit> {
        cachedModules.clear()

        if (Context.getCurrentContext() != null) {
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
            File(
                modulesFolder.parentFile,
                "chattriggers-provided-libs.js"
            ),
            true
        )

        val future = CompletableFuture<Unit>()

        Reference.conditionalThread {
            JSContextFactory.enterContext(moduleContext)

            try {
                moduleContext.evaluateString(
                    scope,
                    providedLibs,
                    "provided",
                    1, null
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                console.printStackTrace(e)

                Context.exit()

                future.complete(Unit)
                return@conditionalThread
            }

            modules.forEach(::evalModule)

            cachedModules.addAll(modules)

            Context.exit()

            future.complete(Unit)
        }

        return future
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

        var entry = module.metadata.entry ?: return
        entry = entry.replace('/', File.separatorChar).replace('\\', File.separatorChar)

        val entryFile = File(module.folder, entry).toURI()

        JSContextFactory.enterContext(moduleContext)
        try {
            require.loadCTModule(module.name, entry, entryFile)
        } catch (e: Throwable) {
            println("Error loading module ${module.name}")
            e.printStackTrace()
            console.out.println("Error loading module ${module.name}")
            console.printStackTrace(e)
        } finally {
            Context.exit()
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

    override fun trigger(trigger: OnTrigger, method: Any, args: Array<out Any?>) {
        if (Context.getCurrentContext() == null) JSContextFactory.enterContext(moduleContext)

        try {
            if (method !is Function) throw ClassCastException("Need to pass actual function to the register function, not the name!")

            method.call(moduleContext, scope, scope, args)
        } catch (e: Throwable) {
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
        scope = ImporterTopLevel(moduleContext)

        val sourceProvider = UrlModuleSourceProvider(listOf(modulesFolder.toURI()), listOf())
        val moduleProvider = StrongCachingModuleScriptProvider(sourceProvider)
        require = CTRequire(moduleProvider)
        require.install(scope)

        Context.exit()

        JSContextFactory.optimize = false
        evalContext = JSContextFactory.enterContext()
        Context.exit()
        JSContextFactory.optimize = true
    }

    class CTRequire(moduleProvider: ModuleScriptProvider) : Require(moduleContext, scope, moduleProvider, null, null, false) {
        fun loadCTModule(name: String, entry: String, uri: URI) {
            getExportedModuleInterface(moduleContext, name + File.separator + entry, uri, null, false)
        }
    }
}