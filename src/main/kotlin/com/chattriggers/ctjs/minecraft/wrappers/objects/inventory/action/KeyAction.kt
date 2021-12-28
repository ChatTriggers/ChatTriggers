package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action

import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.screen.slot.SlotActionType

//#if MC>10809
//$$ import com.chattriggers.ctjs.utils.kotlin.MCClickType
//#endif

@External
class KeyAction(slot: Int) : Action(slot) {
    private var key: Int = -1

    fun getKey(): Int = key

    /**
     * Which key to act as if has been clicked (REQUIRED).
     * Options currently are 0-8, representing the hotbar keys
     *
     * @param key which key to "click"
     */
    fun setKey(key: Int) = apply {
        if (key !in 0..8)
            throw IllegalArgumentException("KeyAction key must be between 0 and 8 inclusive")
        this.key = key
    }

    override fun complete() {
        doClick(key, SlotActionType.SWAP)
    }
}
