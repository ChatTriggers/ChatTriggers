package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.engine.ILoader

abstract class Trigger protected constructor(
    var method: Any,
    var type: TriggerType,
    internal var loader: ILoader
) : Comparable<Trigger> {
    private var priority: Priority = Priority.NORMAL

    init {
        // See note for register method
        @Suppress("LeakingThis")
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

        // Re-register so the position in the ConcurrentSkipListSet gets updated
        unregister()
        register()
    }

    /**
     * Registers a trigger based on its type.
     * This is done automatically with TriggerRegister.
     * @return the trigger for method chaining
     */
    // NOTE: Class initialization cannot be done in this method. It is called in
    //       the init block above, and thus the child class version will not be
    //       run initially
    open fun register() = apply {
        loader.addTrigger(this)
    }

    /**
     * Unregisters a trigger.
     * @return the trigger for method chaining
     */
    open fun unregister() = apply {
        loader.removeTrigger(this)
    }

    protected fun callMethod(args: Array<out Any?>) {
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
