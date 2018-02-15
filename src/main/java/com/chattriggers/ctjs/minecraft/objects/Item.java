package com.chattriggers.ctjs.minecraft.objects;

import lombok.Getter;
import net.minecraft.item.ItemStack;

public class Item {
    @Getter
    private net.minecraft.item.Item item;
    @Getter
    private ItemStack itemStack;

    /* Constructors */

    /**
     * Creates an Item object from a minecraft ItemStack input.<br>
     * This method is not meant for public use.
     * @param itemStack the minecraft ItemStack
     */
    public Item(ItemStack itemStack) {
        this.item = itemStack.getItem();
        this.itemStack = itemStack;
    }

    /**
     * Creates an Item object from a string name input.
     * @param itemName the name of the item
     */
    public Item(String itemName) {
        this.item = net.minecraft.item.Item.getByNameOrId(itemName);
        this.itemStack = new ItemStack(this.item);
    }

    /**
     * Creates an Item object from an integer ID input.
     * @param itemID the ID of the item
     */
    public Item(int itemID) {
        this.item = net.minecraft.item.Item.getItemById(itemID);
        this.itemStack = new ItemStack(this.item);
    }

    /**
     * Creates an Item object from a {@link Block} object input.
     * @param block the {@link Block}
     */
    public Item(Block block) {
        this.item = net.minecraft.item.Item.getItemFromBlock(block.getBlock());
        this.itemStack = new ItemStack(this.item);
    }
    /* End of constructors */

    /**
     * Gets the ID of the item.
     * @return the ID
     */
    public int getID() {
        return net.minecraft.item.Item.getIdFromItem(this.item);
    }

    /**
     * Sets the stack size of the item.
     * @param stackSize the stack size
     * @return the Item
     */
    public Item setStackSize(int stackSize) {
        this.itemStack = new ItemStack(this.item, stackSize);
        return this;
    }

    /**
     * Gets the stack size of the item.
     * @return the stack size
     */
    public int getStackSize() {
        return this.itemStack.stackSize;
    }

    /**
     * Gets the item's unlocalized name.<br>
     * Example: <code>tile.wood</code>
     * @return the item's unlocalized name
     */
    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName();
    }

    /**
     * Gets the item's registry name.<br>
     * Example: <code>minecraft:planks</code>
     * @return the item's registry name
     */
    public String getRegistryName() {
        return this.item.getRegistryName();
    }

    /**
     * Gets the item's stack display name.<br>
     * Example: <code>Oak Wood Planks</code>
     * @return the item's stack display name
     */
    public String getName() {
        return this.item.getItemStackDisplayName(this.itemStack);
    }
}
