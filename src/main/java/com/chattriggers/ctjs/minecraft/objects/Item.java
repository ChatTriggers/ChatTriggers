package com.chattriggers.ctjs.minecraft.objects;

import lombok.Getter;
import net.minecraft.item.ItemStack;

public class Item {
    @Getter
    private net.minecraft.item.Item item;
    @Getter
    private ItemStack itemStack;

    /* Constructors */
    public Item(ItemStack itemStack) {
        this.item = itemStack.getItem();
        this.itemStack = itemStack;
    }

    public Item(net.minecraft.item.Item item) {
        this.item = item;
        this.itemStack = new ItemStack(item);
    }

    public Item(String itemName) {
        this.item = net.minecraft.item.Item.getByNameOrId(itemName);
        this.itemStack = new ItemStack(this.item);
    }

    public Item(int itemID) {
        this.item = net.minecraft.item.Item.getItemById(itemID);
        this.itemStack = new ItemStack(this.item);
    }

    public Item(net.minecraft.block.Block block) {
        this.item = net.minecraft.item.Item.getItemFromBlock(block);
        this.itemStack = new ItemStack(this.item);
    }

    public Item(Block block) {
        this.item = net.minecraft.item.Item.getItemFromBlock(block.getBlock());
        this.itemStack = new ItemStack(this.item);
    }
    /* End of constructors */

    public int getID() {
        return net.minecraft.item.Item.getIdFromItem(this.item);
    }

    public Item setStackSize(int stackSize) {
        this.itemStack = new ItemStack(this.item, stackSize);
        return this;
    }

    public int getStackSize() {
        return this.itemStack.stackSize;
    }

    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName();
    }

    public String getRegistryName() {
        return this.item.getRegistryName();
    }
}
