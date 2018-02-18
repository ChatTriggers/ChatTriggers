package com.chattriggers.ctjs.minecraft.wrappers;

import com.chattriggers.ctjs.minecraft.wrappers.objects.Item;
import lombok.Getter;
import net.minecraft.inventory.IInventory;

public class Inventory {
    @Getter
    private IInventory inventory;

    public Inventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public int getSize() {
        return this.inventory.getSizeInventory();
    }

    public Item getStackInSlot(int slot) {
        return new Item(this.inventory.getStackInSlot(slot));
    }

    public boolean isItemValidForSlot(int slot, Item item) {
        return this.inventory.isItemValidForSlot(slot, item.getItemStack());
    }

    public String getName() {
        return this.inventory.getName();
    }

    public String getClassName() {
        return this.inventory.getClass().getSimpleName();
    }
}
