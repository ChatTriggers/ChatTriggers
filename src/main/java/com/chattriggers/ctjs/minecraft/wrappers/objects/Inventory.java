package com.chattriggers.ctjs.minecraft.wrappers.objects;

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

    public int getSize() {
        return this.inventory != null ? this.inventory.getSizeInventory() : this.container.inventoryItemStacks.size();
    }

    public Item getStackInSlot(int slot) {
        return this.inventory != null ? new Item(this.inventory.getStackInSlot(slot)) : new Item(this.container.inventoryItemStacks.get(slot));
    }

    public boolean isItemValidForSlot(int slot, Item item) {
        return this.inventory == null || this.inventory.isItemValidForSlot(slot, item.getItemStack());
    }

    public String getName() {
        return this.inventory != null ? this.inventory.getName() : "container";
    }

    public String getClassName() {
        return this.inventory != null ? this.inventory.getClass().getSimpleName() : this.container.getClass().getSimpleName();
    }
}
