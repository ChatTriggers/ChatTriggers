package com.chattriggers.ctjs.minecraft.wrappers;

public class Item {
    private net.minecraft.item.Item item;

    public Item(net.minecraft.item.Item item) {
        this.item = item;
    }

    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName();
    }

    public String getRegistryName() {
        return this.item.getRegistryName();
    }
}
