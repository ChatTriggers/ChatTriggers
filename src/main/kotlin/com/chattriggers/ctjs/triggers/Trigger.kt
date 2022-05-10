package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.Engine
import com.chattriggers.ctjs.engine.ILoader

abstract class Trigger protected constructor(var method: Any, var type: TriggerType) : Comparable<Trigger> {
    private val loader = Engine.getLoader()
    private var priority: Priority = Priority.NORMAL

    init {
        register()
    }

    /**
     * Sets a trigger's priority using [Priority].
     * Highest runs first.
     * @param priority the priority of the trigger
     * @return the trigger for method chaining
     */
    fun setPriority(priority: Priority) = apply {
        this.priority = priority
    }

    /**
     * Registers a trigger based on its type.
     * This is done automatically with TriggerRegister.
     * @return the trigger for method chaining
     */
    fun register() = apply {
        loader.addTrigger(this)
    }

    /**
     * Unregisters a trigger.
     * @return the trigger for method chaining
     */
    fun unregister() = apply {
        loader.removeTrigger(this)
    }

    protected fun callMethod(args: Array<out Any?>) {
        if (!Reference.isLoaded) return

        loader.trigger(this, method, args)
    }

    abstract fun trigger(args: Array<out Any?>)

    override fun compareTo(other: Trigger): Int {
        val ordCmp = priority.ordinal - other.priority.ordinal
        return if (ordCmp == 0)
            hashCode() - other.hashCode()
        else ordCmp
    }

    enum class Priority {
        //LOWEST IS RAN LAST
        HIGHEST,
        HIGH,
        NORMAL,
        LOW,
        LOWEST
    }
}
