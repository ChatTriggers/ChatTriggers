package com.chattriggers.ctjs.engine.langs.js

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.langs.Lang
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.triggers.Trigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.console.LogType
import dev.falsehonesty.asmhelper.dsl.At
import dev.falsehonesty.asmhelper.dsl.applyField
import dev.falsehonesty.asmhelper.dsl.inject
import dev.falsehonesty.asmhelper.dsl.remove
import dev.falsehonesty.asmhelper.dsl.writers.AccessType
import org.apache.commons.io.output.WriterOutputStream
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.Source
import org.graalvm.polyglot.Value
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.net.URI
import java.net.URL
import java.net.URLClassLoader
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.contracts.ExperimentalContracts

object JSLoader : ILoader {
    private val classLoader = ModifiedURLClassLoader()
    private val triggers = ConcurrentHashMap<TriggerType, ConcurrentSkipListSet<Trigger>>()
    override val console by lazy { Console(this) }

    private lateinit var moduleContext: Context
    private lateinit var evalContext: Context
    private lateinit var asmLibClass: Value

    private val INVOKE_JS_CALL = MethodHandles.lookup().findStatic(
        JSLoader::class.java,
        "asmInvoke",
        MethodType.methodType(Value::class.java, Value::class.java, Array<Any?>::class.java)
    )

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

    private fun buildContext(): Context {
        val out = WriterOutputStream(console.writer)

        return Context
            .newBuilder("js")
            .out(out)
            .err(out)
            .allowAllAccess(true) // TODO: Per-module permissions?
            .allowHostClassLookup {
                // TODO: Prevent access to CT-internal classes
                true
            }
            .hostClassLoader(classLoader)
            .fileSystem(JSFileSystem)
            .option("js.bigint", "true")
            .option("js.class-fields", "true")
            .option("js.console", "true")
            .option("js.ecmascript-version", "latest")
            .option("js.error-cause", "true")
            .option("js.esm-eval-returns-exports", "true")
            .option("js.global-property", "true")
            .option("js.import-assertions", "true")
            .option("js.intl-402", "true")
            .option("js.java-package-globals", "true")
            .option("js.json-modules", "true")
            .option("js.load", "false")
            .option("js.new-set-methods", "true")
            .option("js.top-level-await", "true")
            .option("js.unhandled-rejections", "warn")
            .build()
    }

    override fun setup(jars: List<URL>) {
        classLoader.addAllURLs(jars)
        evalContext = buildContext()
        moduleContext = buildContext()
    }

    override fun teardown() {
        if (::evalContext.isInitialized)
            evalContext.close(true)
        if (::moduleContext.isInitialized)
            moduleContext.close(true)
    }

    override fun asmSetup() {
        val asmProvidedLibsSource = Source.newBuilder(
            "js",
            this.javaClass.getResourceAsStream("/js/asmProvidedLibs.js")!!.reader(),
            "asm-provided-libs",
        ).mimeType("application/javascript+module").build()

        val asmLibSource = Source.newBuilder(
            "js",
            this.javaClass.getResourceAsStream("/js/asmLib.js")!!.reader(),
            "ASMLib",
        ).mimeType("application/javascript+module").build()

        try {
            moduleContext.eval(asmProvidedLibsSource)
            asmLibClass = moduleContext.eval(asmLibSource).getMember("default")
                ?: throw IllegalStateException()
        } catch (e: Throwable) {
            e.printTraceToConsole(console)
        }
    }

    override fun asmPass(module: Module, asmURI: URI) {
        val source = Source.newBuilder("js", asmURI.toURL())
            .mimeType("application/javascript+module")
            .build()

        try {
            val result = moduleContext.eval(source)
            val export = result.getMember("default")
            if (export == null || !export.canExecute()) {
                "Asm entry for module ${module.name} has an invalid export. " +
                    "An Asm entry must have a default export of a function.".printToConsole(console, LogType.WARN)
                return
            }

            asmLibClass.putMember("currentModule", module.name)
            export.executeVoid(asmLibClass)
        } catch (e: Throwable) {
            println("Error loading asm entry for module ${module.name}")
            e.printTraceToConsole(console)
            "Error loading asm entry for module ${module.name}".printToConsole(console, LogType.ERROR)
        }
    }

    override fun entrySetup() {
        moduleContext = buildContext()

        val moduleProvidedLibsSource = Source.newBuilder(
            "js",
            this.javaClass.getResourceAsStream("/js/moduleProvidedLibs.js")!!.reader(),
            "module-provided-libs",
        ).mimeType("application/javascript+module").build()

        try {
            moduleContext.eval(moduleProvidedLibsSource)
        } catch (e: Throwable) {
            e.printStackTrace()
            e.printTraceToConsole(console)
        }
    }

    override fun entryPass(module: Module, entryURI: URI) {
        val source = Source.newBuilder(
            "js",
            entryURI.toURL()
        ).name(module.name).mimeType("application/javascript+module").build()

        try {
            moduleContext.eval(source)
        } catch (e: Throwable) {
            println("Error loading module ${module.name}")
            e.printStackTrace()

            "Error loading module ${module.name}".printToConsole(console, LogType.ERROR)
            e.printTraceToConsole(console)
        }
    }

    override fun asmInvokeLookup(module: Module, functionURI: URI): MethodHandle {
        val source = Source.newBuilder("js", functionURI.toURL())
            .mimeType("application/javascript+module")
            .build()

        return try {
            val function = moduleContext.eval(source).getMember("default")
            if (function == null || !function.canExecute()) {
                throw IllegalStateException(
                    "ASM exported function at $functionURI has an invalid default export (it must be callable)"
                )
            }

            // When a call to this function ID is made, we always want to point it
            // to our asmInvoke method, which in turn should always call [func].
            INVOKE_JS_CALL.bindTo(function)
        } catch (e: Throwable) {
            "Asm entry for module ${module.name} has an invalid export. " +
                "An Asm entry must have a default export of a function.".printToConsole(console, LogType.WARN)
            e.printTraceToConsole(console)

            // If we can't resolve the target function correctly, we will return
            // a no-op method handle that will always return null.
            // It still needs to match the method type (Object[])Object, so we drop the arguments param.
            MethodHandles.dropArguments(
                MethodHandles.constant(Any::class.java, null),
                0,
                Array<Any?>::class.java,
            )
        }
    }

    @JvmStatic
    fun asmInvoke(func: Value, args: Array<Any?>) = func.execute(args)

    @JvmStatic
    fun asmInjectHelper(
        _className: String,
        _at: At,
        _methodName: String,
        _methodDesc: String,
        _fieldMaps: Map<String, String>,
        _methodMaps: Map<String, String>,
        _insnList: Value
    ) {
        if (!_insnList.canExecute())
            throw IllegalArgumentException("Argument to .instructions() must be callable")

        inject {
            className = _className
            methodName = _methodName
            methodDesc = _methodDesc
            at = _at
            fieldMaps = _fieldMaps
            methodMaps = _methodMaps

            insnList {
                _insnList.execute(this)
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
        _numberToRemove: Int,
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
        _accessTypes: List<AccessType>,
    ) {
        applyField {
            className = _className
            fieldName = _fieldName
            fieldDesc = _fieldDesc
            initialValue = _initialValue
            accessTypes = _accessTypes
        }
    }

    override fun eval(code: String): String {
        val source = Source.newBuilder("js", code, "<eval>").mimeType("application/javascript+module").build()
        return evalContext.eval(source).toString()
    }

    override fun enterContext() {
        moduleContext.enter()
    }

    override fun exitContext() {
        moduleContext.leave()
    }

    override fun getLanguage() = Lang.JS

    override fun trigger(trigger: Trigger, method: Any, args: Array<out Any?>) {
        try {
            if (method !is Value || !method.canExecute())
                throw IllegalArgumentException("Need to pass actual function to the register function, not the name!")

            method.execute(*args)
        } catch (e: Throwable) {
            e.printTraceToConsole(console)
            removeTrigger(trigger)
        }
    }

    private class ModifiedURLClassLoader : URLClassLoader(arrayOf(), javaClass.classLoader) {
        val sources = mutableListOf<URL>()

        init {
            definePackage("com.oracle.truffle.regex", null, null, null, null, null, null, null)
        }

        fun addAllURLs(urls: List<URL>) {
            (urls - sources.toSet()).forEach(::addURL)
        }

        public override fun addURL(url: URL) {
            super.addURL(url)
            sources.add(url)
        }
    }
}
