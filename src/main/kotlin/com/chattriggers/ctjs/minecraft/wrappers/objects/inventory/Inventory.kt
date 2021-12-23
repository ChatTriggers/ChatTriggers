package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory

import com.chattriggers.ctjs.engine.langs.js.JSONImpl
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.Action
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.ClickAction
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.DragAction
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.DropAction
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.inventory.Container
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.IInventory

@External
class Inventory : JSONImpl {
    val inventory: IInventory?
    val container: Container?

    constructor(inventory: IInventory) {
        this.inventory = inventory
        container = null
    }

    constructor(container: Container) {
        this.container = container
        inventory = null
    }

    /**
     * Gets the total size of the Inventory.
     * The player's inventory size is 36, 27 for the main inventory, plus 9 for the hotbar.
     * A single chest's size would be 63, because it also counts the player's inventory.
     *
     * @return the size of the Inventory
     */
    fun getSize(): Int = inventory?.sizeInventory ?: container!!.inventoryItemStacks.size

    /**
     * Gets the item in any slot, starting from 0.
     *
     * @param slot the slot index
     * @return the Item in that slot
     */
    fun getStackInSlot(slot: Int): Item {
        return if (inventory == null)
            Item(container!!.getSlot(slot).stack)
        else Item(inventory.getStackInSlot(slot))
    }

    /**
     * Returns the window identifier number of this Inventory.
     * This Inventory must be backed by a Container [isContainer]
     *
     * @return the window id
     */
    fun getWindowId(): Int = container?.windowId ?: -1

    fun doAction(action: Action) {
        action.complete()
    }

    /**
     * Checks if an item can be shift clicked into a certain slot, i.e. coal into the bottom of a furnace.
     *
     * @param slot the slot index
     * @param item the item for checking
     * @return whether it can be shift clicked in
     */
    fun isItemValidForSlot(slot: Int, item: Item): Boolean {
        return inventory == null
                || inventory.isItemValidForSlot(slot, item.itemStack)
    }

    /**
     * @return a list of the [Item]s in an inventory
     */
    fun getItems(): List<Item> {
        return (0 until getSize()).map(::getStackInSlot)
    }

    /**
     * Checks whether the inventory contains the given item.
     *
     * @param item the item to check for
     * @return whether the inventory contains the item
     */
    fun contains(item: Item): Boolean {
        return getItems().contains(item)
    }

    /**
     * Checks whether the inventory contains an item with ID.
     *
     * @param id the ID of the item to match
     * @retun whether the inventory contains an item with ID
     */
    fun contains(id: Int): Boolean {
        return getItems().any { it.getID() == id }
    }

    /**
     * Gets the index of any item in the inventory, and returns the slot number.
     * Returns -1 if the inventory does not contain the item.
     *
     * @param item the item to check for
     * @return the index of the given item
     */
    fun indexOf(item: Item): Int {
        return getItems().indexOf(item)
    }

    /**
     * Gets the index of any item in the inventory with matching ID, and returns the slot number.
     * Returns -1 if the inventory does not contain the item.
     *
     * @param id the item ID to check for
     * @return the index of the given item with ID
     */
    fun indexOf(id: Int): Int {
        return getItems().indexOfFirst { it.getID() == id }
    }

    /**
     * Returns true if this Inventory wraps a Container object
     * rather than an IInventory object
     *
     * @return if this is a container
     */
    fun isContainer(): Boolean = container != null

    /**
     * Shorthand for [ClickAction]
     *
     * @param slot the slot to click on
     * @param button the mouse button to use. "LEFT" by default.
     * @param shift whether shift is being held. False by default
     * @return this inventory for method chaining
     */
    @JvmOverloads
    fun click(slot: Int, shift: Boolean = false, button: String = "LEFT") = apply {
        ClickAction(slot, getWindowId())
            .setClickString(button)
            .setHoldingShift(shift)
            .complete()
    }

    /**
     * Shorthand for [DropAction]
     *
     * @param slot the slot to drop
     * @param ctrl whether control should be held (drops whole stack)
     * @return this inventory for method chaining
     */
    fun drop(slot: Int, ctrl: Boolean) = apply {
        DropAction(slot, getWindowId())
            .setHoldingCtrl(ctrl)
            .complete()
    }

    /**
     * Shorthand for [DragAction]
     *
     * @param type what click type this should be: LEFT, MIDDLE, RIGHT
     * @param slots all of the slots to drag onto
     * @return this inventory for method chaining
     */
    fun drag(type: String, vararg slots: Int) = apply {
        DragAction(-999, getWindowId()).run {
            setStage(DragAction.Stage.BEGIN)
                .setClickType(DragAction.ClickType.valueOf(type.uppercase()))
                .complete()

            setStage(DragAction.Stage.SLOT)
            slots.forEach { setSlot(it).complete() }

            setStage(DragAction.Stage.END)
                .setSlot(-999)
                .complete()
        }
    }

    /**
     * Gets the name of the inventory, simply "container" for most chest-like blocks.
     *
     * @return the name of the inventory
     */
    fun getName(): String {
        return when (container) {
            is ContainerChest -> container.lowerChestInventory.name
            else -> inventory?.name ?: "container"
        }
    }

    fun getClassName(): String = inventory?.javaClass?.simpleName ?: container!!.javaClass.simpleName

    override fun toString(): String = "Inventory{${getClassName()}}"
}
