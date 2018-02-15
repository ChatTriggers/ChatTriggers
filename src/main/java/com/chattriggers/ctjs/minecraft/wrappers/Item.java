package com.chattriggers.ctjs.minecraft.wrappers;

import net.minecraft.item.ItemStack;

public class Item {
    private net.minecraft.item.Item item;
    private ItemStack itemStack;

    public Item(ItemStack item) {
        this.item = item.getItem();
        this.itemStack = item;
    }

    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName();
    }

    public String getRegistryName() {
        return this.item.getRegistryName();
    }
}
