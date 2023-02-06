package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MCTileEntity
import net.minecraft.network.Packet

abstract class ClassFilterTrigger(method: Any, val triggerType: TriggerType, loader: ILoader) : Trigger(method, triggerType, loader) {
    private var triggerClasses: List<Class<*>> = emptyList()

    @Deprecated("Prefer setFilteredClass", ReplaceWith("setFilteredClass(MyClass.class)"))
    fun setPacketClass(clazz: Class<*>?) = setFilteredClasses(listOfNotNull(clazz))

    @Deprecated("Prefer setFilteredClasses", ReplaceWith("setFilteredClasses([A.class, B.class, C.class])"))
    fun setPacketClasses(classes: List<Class<*>>) = setFilteredClasses(classes)

    /**
     * Alias for `setClasses([A.class, B.class])`
     *
     * @param clazz The class for which this trigger should run for
     */
    fun setFilteredClass(clazz: Class<*>?) = setFilteredClasses(listOfNotNull(clazz))

    /**
     * Sets which classes this trigger should run for. If the list is empty, it runs
     * for every class.
     *
     * @param classes The classes for which this trigger should run for
     * @return This trigger object for chaining
     */
    fun setFilteredClasses(classes: List<Class<*>>) = apply { triggerClasses = classes }

    override fun trigger(args: Array<out Any?>) {
        val placeholder = evalTriggerType(args)
        if(triggerClasses.isEmpty() || triggerClasses.any { it.isInstance(placeholder) })
            callMethod(args)
    }

    abstract fun evalTriggerType(args: Array<out Any?>): Any
}

class RenderEntityTrigger(method: Any, triggerType: TriggerType, loader: ILoader) : ClassFilterTrigger(method, triggerType, loader) {

    override fun evalTriggerType(args: Array<out Any?>): MCEntity {
        return (args.getOrNull(0) as? Entity ?: error("Expected first argument of ${this.triggerType} trigger to be instance of net.minecraft.entity.Entity")).entity
    }
}

class RenderTileEntityTrigger(method: Any, triggerType: TriggerType, loader: ILoader) : ClassFilterTrigger(method, triggerType, loader) {

    override fun evalTriggerType(args: Array<out Any?>): MCTileEntity {
        return (args.getOrNull(0) as? TileEntity ?: error("Expected first argument of ${this.triggerType} trigger to be instance of net.minecraft.tileentity.TileEntity")).tileEntity
    }
}

class PacketTrigger(method: Any, triggerType: TriggerType, loader: ILoader) : ClassFilterTrigger(method, triggerType, loader) {

    override fun evalTriggerType(args: Array<out Any?>): Packet<*> {
        return args.getOrNull(0) as? Packet<*> ?: error("Expected first argument of ${this.triggerType} trigger to be instance of Paket<*> class")
    }
}