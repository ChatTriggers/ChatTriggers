package com.chattriggers.ctjs.minecraft.wrappers.inventory.action

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Inventory

//#if MC>10809
//$$ import com.chattriggers.ctjs.utils.kotlin.MCClickType
//#endif

abstract class Action(var slot: Int, var windowId: Int) {
    fun setSlot(slot: Int) = apply {
        this.slot = slot
    }

    fun setWindowId(windowId: Int) = apply {
        this.windowId = windowId
    }

    abstract fun complete()

    //#if MC<=10809
    protected fun doClick(button: Int, mode: Int) {
        //#else
        //$$ protected fun doClick(button: Int, mode: MCClickType) {
        //#endif
        Client.getMinecraft().playerController.windowClick(
            windowId,
            slot,
            button,
            mode,
            Player.getPlayer()
        )
    }

    companion object {
        /**
         * Creates a new action.
         * The Inventory must be a container, see [Inventory.isContainer].
         * The slot can be -999 for outside of the gui
         *
         * @param inventory the inventory to complete the action on
         * @param slot the slot to complete the action on
         * @param typeString the type of action to do (CLICK, DRAG, DROP, KEY)
         * @return the new action
         */
        @JvmStatic
        fun of(inventory: Inventory, slot: Int, typeString: String) =
            when (Type.valueOf(typeString.uppercase())) {
                Type.CLICK -> ClickAction(slot, inventory.getWindowId())
                Type.DRAG -> DragAction(slot, inventory.getWindowId())
                Type.KEY -> KeyAction(slot, inventory.getWindowId())
                Type.DROP -> DropAction(slot, inventory.getWindowId())
            }
    }

    enum class Type {
        CLICK, DRAG, KEY, DROP
    }
}
