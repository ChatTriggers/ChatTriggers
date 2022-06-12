package com.chattriggers.ctjs.minecraft.wrappers.inventory

//#if MC<=11202
import net.minecraft.client.gui.inventory.GuiContainerCreative.CreativeSlot
//#endif

class Slot(val mcSlot: net.minecraft.inventory.Slot) {
    fun getIndex(): Int {
        //#if MC<=11202
        return if (mcSlot is CreativeSlot) {
            mcSlot.slotIndex
        } else {
            mcSlot.slotNumber
        }
        //#else
        //#if FORGE
        //$$ return mcSlot.slotIndex
        //#else
        //$$ return mcSlot.index
        //#endif
        //#endif
    }

    fun getDisplayX(): Int {
        //#if MC<=11202
        return mcSlot.xDisplayPosition
        //#else
        //$$ return mcSlot.x
        //#endif
    }

    fun getDisplayY(): Int {
        //#if MC<=11202
        return mcSlot.yDisplayPosition
        //#else
        //$$ return mcSlot.y
        //#endif
    }

    fun getInventory(): Inventory {
        //#if MC<=11202
        return Inventory(mcSlot.inventory)
        //#else
        //$$ return Inventory(mcSlot.container)
        //#endif
    }

    fun getItem(): Item? {
        //#if MC<=11202
        return mcSlot.stack?.let(::Item)
        //#else
        //$$ return mcSlot.item?.let(::Item)
        //#endif
    }

    override fun toString(): String = "Slot ${getIndex()} of (${getInventory().getClassName()}: ${getInventory().getName()}): ${getItem()}"
}
