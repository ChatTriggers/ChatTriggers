package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action

import com.chattriggers.ctjs.minecraft.wrappers.Player

//#if MC>10809
//$$ import com.chattriggers.ctjs.utils.kotlin.MCClickType
//#endif

class ClickAction(slot: Int, windowId: Int) : Action(slot, windowId) {
    private lateinit var clickType: ClickType
    private var holdingShift = false
    private var itemInHand = Player.getPlayer()?.inventory?.currentItem == null
    //#if MC>10809
    //$$ private var pickupAll = false
    //#endif

    fun getClickType() = clickType

    /**
     * The type of click (REQUIRED)
     *
     * @param clickType the new click type
     */
    fun setClickType(clickType: ClickType) = apply {
        this.clickType = clickType
    }

    fun getHoldingShift() = holdingShift

    /**
     * Whether the click should act as if shift is being held (defaults to false)
     *
     * @param holdingShift to hold shift or not
     */
    fun setHoldingShift(holdingShift: Boolean) = apply {
        this.holdingShift = holdingShift
    }

    fun getItemInHand() = itemInHand

    /**
     * Whether the click should act as if an item is being held
     * (defaults to whether there actually is an item in the hand)
     *
     * @param itemInHand to be holding an item or not
     */
    fun setItemInHand(itemInHand: Boolean) = apply {
        this.itemInHand = itemInHand
    }

    //#if MC>10809
    //$$ fun getPickupAll() = pickupAll
    //$$
    //$$ /**
    //$$  * Whether the click should try to pick up all items of said type in the inventory (essentially double clicking)
    //$$  * (defaults to whether there actually is an item in the hand)
    //$$  *
    //$$  * @param pickupAll to pickup all items of the same type
    //$$  */
    //$$ fun setPickupAll(pickupAll: Boolean) = apply {
    //$$     this.pickupAll = pickupAll
    //$$ }
    //#endif

    /**
     * Sets the type of click.
     * Possible values are: LEFT, RIGHT, MIDDLE
     *
     * @param clickType the click type
     * @return the current Action for method chaining
     */
    fun setClickString(clickType: String) = apply {
        this.clickType = ClickType.valueOf(clickType.toUpperCase())
    }

    override fun complete() {
        //#if MC<=10809
        var mode = 0

        if (this.clickType == ClickType.MIDDLE) {
            mode = 3
        } else if (slot == -999 && !this.itemInHand) {
            mode = 4
        } else if (this.holdingShift) {
            mode = 1
        }
        //#else
        //$$ MCClickType mode;
        //$$ if (this.clickType == ClickType.MIDDLE) {
        //$$     mode = MCClickType.CLONE
        //$$ } else if (slot == -999 && !this.itemInHand) {
        //$$     mode = MCClickType.THROW
        //$$ } else if (this.holdingShift) {
        //$$     mode = MCClickType.QUICK_MOVE
        //$$ } else if (pickupAll) {
        //$$     mode = MCClickType.PICKUP_ALL
        //$$ } else {
        //$$     mode = MCClickType.PICKUP
        //$$ }
        //#endif

        doClick(clickType.button, mode)
    }

    enum class ClickType(val button: Int) {
        LEFT(0), RIGHT(1), MIDDLE(2);
    }
}