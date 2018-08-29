package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.loader.ModuleManager
import com.chattriggers.ctjs.modules.Module
import com.chattriggers.ctjs.utils.console.Console
import jdk.nashorn.api.scripting.NashornScriptEngine
import jdk.nashorn.api.scripting.ScriptObjectMirror
import jdk.nashorn.internal.objects.Global
import net.minecraftforge.fml.relauncher.ReflectionHelper

import javax.script.ScriptException

abstract class OnTrigger protected constructor(method: Any, type: TriggerType) {
    var method: Any
        protected set
    var priority: Priority
    var type: TriggerType
        protected set
    var owningModule: Module? = null

    private var global: Global? = null

    init {
        this.method = method
        this.priority = Priority.NORMAL
        this.type = type

        if (TriggerRegister.currentModule != null) {
            owningModule = TriggerRegister.currentModule
        }

        this.register()
    }

    /**
     * @return boolean of if trigger is registered
     */
    fun isRegistered() = this.type.containsTrigger(this)

    /**
     * Sets a triggers priority using [Priority].
     * Highest runs first.
     * @param priority the priority of the trigger
     * @return the trigger for method chaining
     */
    fun setPriority(priority: Priority) = apply { this.priority = priority }

    /**
     * Registers a trigger based on its type.
     * This is done automatically with TriggerRegister.
     * @return the trigger for method chaining
     */
    fun register() = apply { this.type.addTrigger(this) }

    /**
     * Unregisters a trigger.
     * @return the trigger for method chaining
     */
    fun unregister() = apply { this.type.removeTrigger(this) }

    protected fun callMethod(vararg args: Any) {
        try {
            if (this.method is String) {
                callNamedMethod(*args)
                return
            }

            callActualMethod(*args)
        } catch (e: Exception) {
            Console.getInstance().out.println("Error on $type trigger in module $owningModule")
            Console.getInstance().printStackTrace(e, this)
            this.type.removeTrigger(this)
        }
    }

    protected fun callActualMethod(vararg args: Any) {
        val som: ScriptObjectMirror

        if (this.method is ScriptObjectMirror) {
            som = this.method as ScriptObjectMirror
        } else {
            if (global == null) {
                global = ReflectionHelper.getPrivateValue<Global, NashornScriptEngine>(
                    NashornScriptEngine::class.java,
                    ModuleManager.getInstance().scriptLoaders[0].scriptEngine as NashornScriptEngine,
                    "global"
                )
            }

            som = ScriptObjectMirror.wrap(this.method, global) as ScriptObjectMirror
        }

        som.call(som, *args)
    }

    @Throws(ScriptException::class, NoSuchMethodException::class)
    protected fun callNamedMethod(vararg args: Any) {
        ModuleManager.getInstance().invokeFunction(
            method as String,
            *args
        )
    }

    abstract fun trigger(vararg args: Any)

    enum class TriggerResult {
        CANCEL
    }

    enum class Priority {
        //LOWEST IS RAN LAST
        LOWEST,
        LOW, NORMAL, HIGH, HIGHEST
    }
}
