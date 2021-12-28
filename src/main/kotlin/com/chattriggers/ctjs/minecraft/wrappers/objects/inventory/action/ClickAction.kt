package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action

import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.screen.slot.SlotActionType

@External
class ClickAction(slot: Int) : Action(slot) {
    private var clickType = ClickType.LEFT
    private var holdingShift = false
    private var itemInHand = Player.getHeldItem() == null

    fun getClickType(): ClickType = clickType

    /**
     * The type of click
     *
     * @param clickType the new click type
     */
    fun setClickType(clickType: ClickType) = apply {
        this.clickType = clickType
    }

    fun getHoldingShift(): Boolean = holdingShift

    /**
     * Whether the click should act as if shift is being held (defaults to false)
     *
     * @param holdingShift to hold shift or not
     */
    fun setHoldingShift(holdingShift: Boolean) = apply {
        this.holdingShift = holdingShift
    }

    fun getItemInHand(): Boolean = itemInHand

    /**
     * Whether the click should act as if an item is being held
     * (defaults to whether there actually is an item in the hand)
     *
     * @param itemInHand to be holding an item or not
     */
    fun setItemInHand(itemInHand: Boolean) = apply {
        this.itemInHand = itemInHand
    }

    /**
     * Sets the type of click.
     * Possible values are: LEFT, RIGHT, MIDDLE
     *
     * @param clickType the click type
     * @return the current Action for method chaining
     */
    fun setClickString(clickType: String) = apply {
        this.clickType = ClickType.valueOf(clickType.uppercase())
    }

    override fun complete() {
        val mode = when {
            clickType == ClickType.MIDDLE -> SlotActionType.CLONE
            slot == -999 && itemInHand -> SlotActionType.THROW
            holdingShift -> SlotActionType.QUICK_MOVE
            else -> SlotActionType.PICKUP
        }

        doClick(clickType.button, mode)
    }
}
