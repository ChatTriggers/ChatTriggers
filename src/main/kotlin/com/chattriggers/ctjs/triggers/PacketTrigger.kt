package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import net.minecraft.network.Packet

class PacketTrigger(method: Any, triggerType: TriggerType, loader: ILoader) : Trigger(method, triggerType, loader) {
    private var triggerClasses: List<Class<*>> = emptyList()

    /**
     * Alias for `setPacketClasses([class_])`
     */
    fun setPacketClass(class_: Class<*>?) = setPacketClasses(listOfNotNull(class_))

    /**
     * Sets which classes this trigger should run for. If the list is empty, it runs
     * for every packet class.
     *
     * @param classes The classes for which this trigger should run for
     * @return This trigger object for packet chaining
     */
    fun setPacketClasses(classes: List<Class<*>>) = apply { triggerClasses = classes }

    override fun trigger(args: Array<out Any?>) {
        val packet = args.getOrNull(0) as? Packet<*>
            ?: error("Expected first argument of packet trigger to be instance of Packet<*> class")
        if (triggerClasses.isEmpty() || triggerClasses.any { it.isInstance(packet) })
            callMethod(args)
    }
}
