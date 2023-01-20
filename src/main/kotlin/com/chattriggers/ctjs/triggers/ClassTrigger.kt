package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MCTileEntity
import net.minecraft.network.Packet

class ClassTrigger(method: Any,private val triggerType: TriggerType, loader: ILoader) : Trigger(method, triggerType, loader) {
    private var triggerClasses: List<Class<*>> = emptyList()

    /**
     * Alias for `setClasses([clazz])`
     */
    fun setClass(clazz: Class<*>?) = setClasses(listOfNotNull(clazz))

    /**
     * Sets which classes this trigger should run for. If the list is empty, it runs
     * for every class.
     *
     * @param classes The classes for which this trigger should run for
     * @return This trigger object for chaining
     */
    fun setClasses(classes: List<Class<*>>) = apply { triggerClasses = classes }

    override fun trigger(args: Array<out Any?>) {
        val placeholder = evalTriggerType(args)
        if(triggerClasses.isEmpty() || triggerClasses.any { it.isInstance(placeholder) })
            callMethod(args)
    }

    private fun evalTriggerType(args: Array<out Any?>): Any {
         return when(this.triggerType) {
             TriggerType.PacketReceived -> returnPacket(args.getOrNull(0), TriggerType.PacketReceived)
             TriggerType.PacketSent -> returnPacket(args.getOrNull(0), TriggerType.PacketSent)
             TriggerType.RenderEntity -> returnEntity(args.getOrNull(0), TriggerType.RenderEntity)
             TriggerType.PostRenderEntity -> returnEntity(args.getOrNull(0), TriggerType.PostRenderEntity)
             TriggerType.RenderTileEntity -> returnTileEntity(args.getOrNull(0), TriggerType.RenderTileEntity)
             TriggerType.PostRenderTileEntity -> returnTileEntity(args.getOrNull(0), TriggerType.PostRenderTileEntity)
             else -> {
                 error("Logging args: ${this.triggerType}")
             }
        }
    }

    private fun returnEntity(obj: Any?, type: TriggerType): MCEntity {
        return (obj as? Entity ?: error("Expected first argument of $type trigger to be instance of net.minecraft.entity.Entity")).entity
    }

    private fun returnPacket(obj: Any?, type: TriggerType): Packet<*> {
        return obj as? Packet<*> ?: error("Expected first argument of $type trigger to be instance of Paket<*> class")
    }

    private fun returnTileEntity(obj: Any?, type: TriggerType): MCTileEntity {
        return (obj as? TileEntity ?: error("Expected first argument of $type trigger to be instance of net.minecraft.tileentity.TileEntity")).tileEntity
    }
}