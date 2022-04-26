package com.chattriggers.ctjs.minecraft.wrappers.inventory

import com.chattriggers.ctjs.utils.kotlin.MCSlot

class Slot(val mcSlot: MCSlot) {
    fun getIndex(): Int = mcSlot.slotNumber

    fun getDisplayX(): Int = mcSlot.xDisplayPosition

    fun getDisplayY(): Int = mcSlot.yDisplayPosition

    fun getInventory(): Inventory = Inventory(mcSlot.inventory)

    fun getItem(): Item? = if (mcSlot.stack != null) Item(mcSlot.stack) else null

    override fun toString(): String = "Slot ${getIndex()} of (${getInventory().getClassName()}: ${getInventory().getName()}): ${getItem()}"
}