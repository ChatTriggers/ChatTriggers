package com.chattriggers.ctjs.minecraft.wrappers.inventory

import com.chattriggers.ctjs.utils.kotlin.MCSlot
import net.minecraft.client.gui.inventory.GuiContainerCreative.CreativeSlot

class Slot(val mcSlot: MCSlot) {
    fun getIndex(): Int = if (mcSlot is CreativeSlot) {
        mcSlot.slotIndex
    } else {
        mcSlot.slotNumber
    }

    fun getDisplayX(): Int = mcSlot.xDisplayPosition

    fun getDisplayY(): Int = mcSlot.yDisplayPosition

    fun getInventory(): Inventory = Inventory(mcSlot.inventory)

    fun getItem(): Item? = mcSlot.stack?.let(::Item)

    override fun toString(): String = "Slot ${getIndex()} of (${getInventory().getClassName()}: ${getInventory().getName()}): ${getItem()}"
}
