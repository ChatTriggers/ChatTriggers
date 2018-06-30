package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory;

import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.Action;
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.ClickAction;
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.DragAction;
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.action.DropAction;
import lombok.Getter;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public class Inventory {
    @Getter
    private IInventory inventory;
    @Getter
    private Container container;

    public Inventory(IInventory inventory) {
        this.inventory = inventory;
        this.container = null;
    }

    public Inventory(Container container) {
        this.container = container;
        this.inventory = null;
    }

    /**
     * Gets the total size of the Inventory.
     * The player's inventory size is 36, 27 for the main inventory, plus 9 for the hotbar.
     * A single chest's size would be 63, because it also counts the player's inventory.
     *
     * @return the size of the Inventory
     */
    public int getSize() {
        return this.inventory != null ? this.inventory.getSizeInventory() : this.container.inventoryItemStacks.size();
    }

    /**
     * Gets the item in any slot, starting from 0.
     *
     * @param slot the slot index
     * @return the Item in that slot
     */
    public Item getStackInSlot(int slot) {
        return this.inventory != null
                ? new Item(this.inventory.getStackInSlot(slot))
                : new Item(this.container.getSlot(slot).getStack());
    }

    /**
     * Returns the window identifier number of this Inventory.
     * This Inventory must be backed by a Container {@link #isContainer()}
     *
     * @return the window id
     */
    public int getWindowId() {
        return this.isContainer() ? this.container.windowId : -1;
    }

    public void doAction(Action action) {
        action.complete();
//        if (this.isContainer()) {
//            this.container
//        }
    }

    /**
     * Checks if an item can be shift clicked into a certain slot, i.e coal into the bottom of a furnace.
     *
     * @param slot the slot index
     * @param item the item for checking
     * @return whether or not it can be shift clicked in
     */
    public boolean isItemValidForSlot(int slot, Item item) {
        return this.inventory == null || this.inventory.isItemValidForSlot(slot, item.getItemStack());
    }

    /**
     * Checks whether the inventory contains the given item.
     *
     * @param item the item to check for
     * @return whether or not the inventory contains the item
     */
    public boolean contains(Item item) {
        for (int i = 0; i < getSize(); i++) {
            Item itemInSlot = getStackInSlot(i);

            if (itemInSlot.equals(item)) return true;
        }

        return false;
    }

    /**
     * Gets the index of any item in the inventory, and returns the slot number.
     * Returns -1 if the inventory does not contain the item.
     *
     * @param item the item to check for
     * @return the index of the given item
     */
    public int indexOf(Item item) {
        for (int i = 0; i < getSize(); i++) {
            Item itemInSlot = getStackInSlot(i);

            if (itemInSlot.equals(item)) return i;
        }

        return -1;
    }

    /**
     * Returns true if this Inventory wraps a Container object
     * rather than an IInventory object
     *
     * @return if this is a container
     */
    public boolean isContainer() {
        return this.container != null;
    }

    /**
     * Shorthand for {@link ClickAction}
     *
     * @param slot the slot to click on
     * @param shift whether shift is being held
     * @return this inventory for method chaining
     */
    public Inventory click(int slot, boolean shift) {
        ClickAction action = new ClickAction(slot, this.getWindowId());

        action.setClickString("LEFT")
                .setHoldingShift(shift)
                .complete();

        return this;
    }

    /**
     * Shorthand for {@link DropAction}
     *
     * @param slot the slot to drop
     * @param ctrl whether control should be held (drops whole stack)
     * @return this inventory for method chaining
     */
    public Inventory drop(int slot, boolean ctrl) {
        DropAction action = new DropAction(slot, this.getWindowId());

        action.setHoldingCtrl(ctrl)
                .complete();

        return this;
    }

    /**
     * Shorthand for {@link DragAction}
     *
     * @param type what click type this should be: LEFT, MIDDLE, RIGHT
     * @param slots all of the slots to drag onto
     * @return this inventory for method chaining
     */
    public Inventory drag(String type, int... slots) {
        DragAction action = new DragAction(-999, this.getWindowId());
        DragAction.ClickType clickType = DragAction.ClickType.valueOf(type.toUpperCase());

        action.setStage(DragAction.Stage.BEGIN)
                .setClickType(clickType)
                .complete();

        action.setStage(DragAction.Stage.SLOT);
        for (int slot : slots) {
            action.setSlot(slot)
                    .complete();
        }

        action.setStage(DragAction.Stage.END)
                .setSlot(-999)
                .complete();

        return this;
    }

    /**
     * Gets the name of the inventory, simply "container" for most chest-like blocks.
     *
     * @return the name of the inventory
     */
    public String getName() {
        return this.inventory != null ? this.inventory.getName() : "container";
    }

    /**
     * Gets the Java class name of the Inventory, like "ContainerChest" for chests.
     *
     * @return the name of the class backing this inventory.
     */
    public String getClassName() {
        return this.inventory != null ? this.inventory.getClass().getSimpleName() : this.container.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Inventory{" + getClassName() + "}";
    }
}
