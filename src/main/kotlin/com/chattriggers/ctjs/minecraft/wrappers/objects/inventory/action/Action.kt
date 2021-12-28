package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Inventory
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.screen.slot.SlotActionType

@External
abstract class Action(var slot: Int) {
    fun setSlot(slot: Int) = apply {
        this.slot = slot
    }

    abstract fun complete()

    protected fun doClick(button: Int, mode: SlotActionType) {
        Client.getMinecraft().interactionManager?.clickSlot(
            Player.getPlayer()?.playerScreenHandler?.syncId ?: return,
            slot,
            button,
            mode,
            Player.getPlayer(),
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
        fun of(inventory: Inventory, slot: Int, typeString: String): Action {
            return when (Type.valueOf(typeString.uppercase())) {
                Type.CLICK -> ClickAction(slot)
                Type.DRAG -> DragAction(slot)
                Type.KEY -> KeyAction(slot)
                Type.DROP -> DropAction(slot)
            }
        }
    }

    enum class Type {
        CLICK, DRAG, KEY, DROP
    }

    enum class ClickType(val button: Int) {
        LEFT(0),
        RIGHT(1),
        MIDDLE(2)
    }
}
