package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.langs.Lang
import com.chattriggers.ctjs.engine.langs.js.impl.CTHostHooks
import com.chattriggers.ctjs.engine.langs.js.objects.ASMGlobalObject
import com.chattriggers.ctjs.engine.langs.js.objects.ModuleGlobalObject
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.engine.module.ModuleManager.modulesFolder
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.triggers.OnTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.console.Console
import com.reevajs.reeva.core.Agent
import com.reevajs.reeva.core.errors.DefaultErrorReporter
import com.reevajs.reeva.core.errors.ThrowException
import com.reevajs.reeva.core.lifecycle.*
import com.reevajs.reeva.core.realm.Realm
import com.reevajs.reeva.jvmcompat.JSClassInstanceObject
import com.reevajs.reeva.jvmcompat.JVMValueMapper
import com.reevajs.reeva.runtime.Operations
import com.reevajs.reeva.runtime.functions.JSFunction
import com.reevajs.reeva.runtime.primitives.JSUndefined
import com.reevajs.reeva.runtime.toJSString
import dev.falsehonesty.asmhelper.dsl.*
import dev.falsehonesty.asmhelper.dsl.writers.AccessType
import java.io.File
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.net.URI
import java.net.URL
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.contracts.ExperimentalContracts

@OptIn(ExperimentalContracts::class)
object JSLoader : ILoader {
    override val console = Console(this)

    private val agent = Agent().apply {
        hostHooks = CTHostHooks()
        errorReporter = DefaultErrorReporter(console.out)
        Agent.setAgent(this)
    }

    val eventLoop = EventLoop(agent)

    private val triggers = ConcurrentHashMap<TriggerType, ConcurrentSkipListSet<OnTrigger>>()

    private var asmLibInitialized = false
    private lateinit var asmRealm: Realm
    private lateinit var moduleRealm: Realm
    private lateinit var evalRealm: Realm

    private val INVOKE_JS_CALL = MethodHandles.lookup().findStatic(
        JSLoader::class.java,
        "asmInvoke",
        MethodType.methodType(Any::class.java, JSFunction::class.java, Array<Any?>::class.java)
    )

    override fun exec(type: TriggerType, args: Array<out Any?>) {
        triggers[type]?.forEach { it.trigger(args) }
    }

    override fun addTrigger(trigger: OnTrigger) {
        triggers.getOrPut(trigger.type, ::newTriggerSet).add(trigger)
    }

    override fun clearTriggers() {
        triggers.clear()
    }

    override fun removeTrigger(trigger: OnTrigger) {
        triggers[trigger.type]?.remove(trigger)
    }

    private fun newTriggerSet() = ConcurrentSkipListSet<OnTrigger>()

    override fun setup(jars: List<URL>) {
        asmLibInitialized = false

        instanceContexts(jars)
    }

    override fun asmSetup() {
        val asmLibFile = File(modulesFolder.parentFile, "chattriggers-asmLib.js")

        saveResource("/js/asmLib.js", asmLibFile, true)
        asmRealm = agent.makeRealm {
            ASMGlobalObject.create(it)
        }

        val sourceInfo = FileSourceInfo(File(asmLibFile.toURI()), isModule = true, "ASMLib")

        try {
            val result = executeModule(asmRealm, sourceInfo)
                ?: throw IllegalStateException("Unexpected error running ASMLib")

            val asmLib = result.env.getBinding(ModuleRecord.DEFAULT_SPECIFIER)
            asmRealm.globalObject.defineOwnProperty("ASM", asmLib, 0)
            asmLibInitialized = true
        } catch (e: ThrowException) {
            asmLibInitialized = false
            agent.errorReporter.reportRuntimeError(sourceInfo, e)
        } catch (e: Throwable) {
            asmLibInitialized = false
            agent.errorReporter.reportInternalError(sourceInfo, e)
        }
    }

    override fun asmPass(module: Module, asmURI: URI) {
        val sourceInfo = FileSourceInfo(File(asmURI), isModule = true, module.name)

        try {
            executeModule(asmRealm, sourceInfo)
        } catch (e: ThrowException) {
            agent.errorReporter.reportRuntimeError(sourceInfo, e)
        } catch (e: Throwable) {
            agent.errorReporter.reportInternalError(sourceInfo, e)
        }
    }

    override fun entrySetup() {
        // This can be called in a different thread, so make sure this thread's
        // agent is set correctly
        Agent.setAgent(agent)

        evalRealm = agent.makeRealm {
            ModuleGlobalObject.create(it)
        }

        moduleRealm = agent.makeRealm {
            ModuleGlobalObject.create(it)
        }
    }

    override fun entryPass(module: Module, entryURI: URI) {
        val sourceInfo = FileSourceInfo(File(entryURI), isModule = true, module.name)

        try {
            executeModule(moduleRealm, sourceInfo)
        } catch (e: ThrowException) {
            agent.errorReporter.reportRuntimeError(sourceInfo, e)
        } catch (e: Throwable) {
            agent.errorReporter.reportInternalError(sourceInfo, e)
        }
    }

    private fun executeModule(realm: Realm, sourceInfo: SourceInfo): ModuleRecord? {
        val result = SourceTextModuleRecord.parseModule(realm, sourceInfo)
        if (result.hasError) {
            val error = result.error()
            agent.errorReporter.reportParseError(sourceInfo, error.cause, error.start, error.end)
            return null
        }

        val moduleRecord = result.value()
        moduleRecord.execute()
        return moduleRecord
    }

    override fun asmInvokeLookup(module: Module, functionURI: URI): MethodHandle {
        val sourceInfo = FileSourceInfo(File(functionURI), isModule = true, module.name)

        try {
            val moduleRecord = executeModule(asmRealm, sourceInfo)
                ?: throw IllegalStateException("Unable to resolve module $module")
            if (!moduleRecord.env.hasBinding(ModuleRecord.DEFAULT_SPECIFIER))
                throw IllegalStateException("ASM exported function module must have a callable default export")

            val defaultExport = moduleRecord.env.getBinding(ModuleRecord.DEFAULT_SPECIFIER)
            if (defaultExport !is JSFunction)
                throw IllegalStateException("ASM exported function module must have a callable default export")

            // When a call to this function ID is made, we always want to point it
            // to our asmInvoke method, which in turn should always call [func].
            return INVOKE_JS_CALL.bindTo(defaultExport)
        } catch (e: ThrowException) {
            agent.errorReporter.reportRuntimeError(sourceInfo, e)
        } catch (e: Throwable) {
            agent.errorReporter.reportInternalError(sourceInfo, e)
        }

        // If we can't resolve the target function correctly, we will return
        //  a no-op method handle that will always return null.
        //  It still needs to match the method type (Object[])Object, so we drop the arguments param.
        return MethodHandles.dropArguments(
            MethodHandles.constant(Any::class.java, null),
            0,
            Array<Any?>::class.java,
        )
    }

    @JvmStatic
    fun asmInvoke(func: JSFunction, args: Array<Any?>): Any {
        return Operations.call(
            moduleRealm,
            func,
            JSUndefined,
            args.map { JVMValueMapper.jvmToJS(moduleRealm, it) },
        )
    }

    @JvmStatic
    fun asmInjectHelper(
        _className: String,
        _at: At,
        _methodName: String,
        _methodDesc: String,
        _fieldMaps: Map<String, String>,
        _methodMaps: Map<String, String>,
        _insnList: JSFunction,
    ) {
        inject {
            className = _className
            methodName = _methodName
            methodDesc = _methodDesc
            at = _at
            fieldMaps = _fieldMaps
            methodMaps = _methodMaps

            insnList {
                val wrappedBuilder = JSClassInstanceObject.wrap(asmRealm, this)
                Operations.call(asmRealm, _insnList, JSUndefined, listOf(wrappedBuilder))
            }
        }
    }

    @JvmStatic
    fun asmRemoveHelper(
        _className: String,
        _at: At,
        _methodName: String,
        _methodDesc: String,
        _methodMaps: Map<String, String>,
        _numberToRemove: Int
    ) {
        remove {
            className = _className
            methodName = _methodName
            methodDesc = _methodDesc
            at = _at
            methodMaps = _methodMaps
            numberToRemove = _numberToRemove
        }
    }

    @JvmStatic
    fun asmFieldHelper(
        _className: String,
        _fieldName: String,
        _fieldDesc: String,
        _initialValue: Any?,
        _accessTypes: List<AccessType>
    ) {
        applyField {
            className = _className
            fieldName = _fieldName
            fieldDesc = _fieldDesc
            initialValue = _initialValue
            accessTypes = _accessTypes
        }
    }

    override fun eval(code: String): String? {
        val sourceInfo = LiteralSourceInfo("<eval>", code, isModule = false)
        val result = agent.compileScript(evalRealm, sourceInfo)
        if (result.hasError) {
            agent.errorReporter.reportParseError(sourceInfo, result.error())
        } else {
            try {
                return result.value().execute().toJSString(evalRealm).string
            } catch (e: ThrowException) {
                agent.errorReporter.reportRuntimeError(sourceInfo, e)
            } catch (e: Throwable) {
                agent.errorReporter.reportInternalError(sourceInfo, e)
            }
        }

        return null
    }

    override fun getLanguage() = Lang.JS

    override fun trigger(trigger: OnTrigger, method: Any, args: Array<out Any?>) {
        try {
            if (method !is JSFunction)
                throw IllegalArgumentException("Need to pass actual function to the register function, not the name!")

            val jsArgs = args.map { JVMValueMapper.jvmToJS(moduleRealm, it) }

            synchronized(moduleRealm) {
                Operations.call(moduleRealm, method, JSUndefined, jsArgs)
            }
        // TODO: How do we report errors here without SourceInfo?
        // } catch (e: ThrowException) {
        //     agent.errorReporter.reportRuntimeError()
        } catch (e: Throwable) {
            e.printTraceToConsole(console)
            removeTrigger(trigger)
        }
    }

    private fun instanceContexts(files: List<URL>) {
        // TODO

        // JSContextFactory.addAllURLs(files)
        //
        // moduleContext = JSContextFactory.enterContext()
        // scope = ImporterTopLevel(moduleContext)
        //
        // val sourceProvider = UrlModuleSourceProvider(listOf(modulesFolder.toURI()), listOf())
        // val moduleProvider = StrongCachingModuleScriptProvider(sourceProvider)
        // require = CTRequire(moduleProvider)
        // require.install(scope)
        //
        // Context.exit()
        //
        // JSContextFactory.optimize = false
        // evalContext = JSContextFactory.enterContext()
        // Context.exit()
        // JSContextFactory.optimize = true
    }
}
