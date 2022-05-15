package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.langs.Lang
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.engine.module.ModuleManager.modulesFolder
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.triggers.Trigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.console.LogType
import org.mozilla.javascript.*
import org.mozilla.javascript.Function
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider
import org.mozilla.javascript.commonjs.module.Require
import org.mozilla.javascript.commonjs.module.provider.StrongCachingModuleScriptProvider
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider
import java.io.File
import java.net.URI
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
object JSLoader : ILoader {
    private val triggers = ConcurrentHashMap<TriggerType, ConcurrentSkipListSet<Trigger>>()
    override val console by lazy { Console(this) }

    private lateinit var moduleContext: Context
    private lateinit var evalContext: Context
    private lateinit var scope: Scriptable
    private lateinit var require: CTRequire

    override fun exec(type: TriggerType, args: Array<out Any?>) {
        triggers[type]?.forEach { it.trigger(args) }
    }

    override fun addTrigger(trigger: Trigger) {
        triggers.getOrPut(trigger.type, ::newTriggerSet).add(trigger)
    }

    override fun clearTriggers() {
        triggers.clear()
    }

    override fun removeTrigger(trigger: Trigger) {
        triggers[trigger.type]?.remove(trigger)
    }

    private fun newTriggerSet() = ConcurrentSkipListSet<Trigger>()

    override fun setup(jars: List<URL>) {
        instanceContexts(jars)
    }

    override fun entrySetup(): Unit = wrapInContext {
        val providedLibs = saveResource(
            "/js/providedLibs.js",
            File(modulesFolder.parentFile, "chattriggers-provided-libs.js"),
            true
        )

        try {
            moduleContext.evaluateString(
                scope,
                providedLibs,
                "providedLibs",
                1, null
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            e.printTraceToConsole(console)
        }
    }

    override fun entryPass(module: Module, entryURI: URI): Unit = wrapInContext {
        try {
            require.loadCTModule(module.name, module.metadata.entry!!, entryURI)
        } catch (e: Throwable) {
            println("Error loading module ${module.name}")
            e.printStackTrace()

            "Error loading module ${module.name}".printToConsole(console, LogType.ERROR)
            e.printTraceToConsole(console)
        }
    }

    private inline fun <T> wrapInContext(context: Context = moduleContext, crossinline block: () -> T): T {
        contract {
            callsInPlace(block, InvocationKind.EXACTLY_ONCE)
        }

        val missingContext = Context.getCurrentContext() == null
        if (missingContext) {
            try {
                JSContextFactory.enterContext(context)
            } catch (e: Throwable) {
                JSContextFactory.enterContext()
            }
        }

        try {
            return block()
        } finally {
            if (missingContext) Context.exit()
        }
    }

    override fun eval(code: String): String {
        return wrapInContext(evalContext) {
            Context.toString(evalContext.evaluateString(scope, code, "<eval>", 1, null))
        }
    }

    override fun getLanguage() = Lang.JS

    override fun trigger(trigger: Trigger, method: Any, args: Array<out Any?>) {
        wrapInContext {
            try {
                if (method !is Function)
                    throw IllegalArgumentException("Need to pass actual function to the register function, not the name!")

                method.call(Context.getCurrentContext(), scope, scope, args)
            } catch (e: Throwable) {
                e.printTraceToConsole(console)
                removeTrigger(trigger)
            }
        }
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

    class CTRequire(
        moduleProvider: ModuleScriptProvider,
    ) : Require(moduleContext, scope, moduleProvider, null, null, false) {
        fun loadCTModule(name: String, entry: String, uri: URI): Scriptable {
            return getExportedModuleInterface(moduleContext, name + File.separator + entry, uri, null, false)
        }
    }
}
