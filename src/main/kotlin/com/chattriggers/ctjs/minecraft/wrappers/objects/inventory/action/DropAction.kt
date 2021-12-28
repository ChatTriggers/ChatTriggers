package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action

import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.screen.slot.SlotActionType

@External
class DropAction(slot: Int) : Action(slot) {
    private var holdingCtrl = false

    fun getHoldingCtrl(): Boolean = holdingCtrl

    /**
     * Whether the click should act as if control is being held (defaults to false)
     *
     * @param holdingCtrl to hold ctrl or not
     */
    fun setHoldingCtrl(holdingCtrl: Boolean) = apply {
        this.holdingCtrl = holdingCtrl
    }

    override fun complete() {
        doClick(if (holdingCtrl) 1 else 0, SlotActionType.THROW)
    }
}
