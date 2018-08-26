package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action

class DropAction(slot: Int, windowId: Int) : Action(slot, windowId) {
    private var holdingCtrl = false

    fun getHoldingCtrl() = holdingCtrl

    /**
     * Whether the click should act as if control is being held (defaults to false)
     *
     * @param holdingCtrl to hold ctrl or not
     */
    fun setHoldingCtrl(holdingCtrl: Boolean) = apply {
        this.holdingCtrl = holdingCtrl
    }

    override fun complete() {
        doClick(
            if (holdingCtrl) 1 else 0,
            //#if MC<=10809
            4
            //#else
            //$$ net.minecraft.inventory.ClickType.THROW
            //#endif
        )
    }
}