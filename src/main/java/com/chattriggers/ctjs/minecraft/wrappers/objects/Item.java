package com.chattriggers.ctjs.minecraft.wrappers.objects;

import lombok.Getter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Item {
    @Getter
    private net.minecraft.item.Item item;
    @Getter
    private ItemStack itemStack;

    /* Constructors */

    /**
     * Creates an Item object from a minecraft ItemStack input.<br>
     * This method is not meant for public use.
     *
     * @param itemStack the minecraft ItemStack
     */
    public Item(ItemStack itemStack) {
        this.item = itemStack.getItem();
        this.itemStack = itemStack;
    }

    /**
     * Creates an Item object from a string name input.
     *
     * @param itemName the name of the item
     */
    public Item(String itemName) {
        this.item = net.minecraft.item.Item.getByNameOrId(itemName);
        this.itemStack = new ItemStack(this.item);
    }

    /**
     * Creates an Item object from an integer ID input.
     *
     * @param itemID the ID of the item
     */
    public Item(int itemID) {
        this.item = net.minecraft.item.Item.getItemById(itemID);
        this.itemStack = new ItemStack(this.item);
    }

    /**
     * Creates an Item object from a {@link Block} object input.
     *
     * @param block the {@link Block}
     */
    public Item(Block block) {
        this.item = net.minecraft.item.Item.getItemFromBlock(block.getBlock());
        this.itemStack = new ItemStack(this.item);
    }
    /* End of constructors */

    /**
     * Gets the ID of the item.
     *
     * @return the ID
     */
    public int getID() {
        return net.minecraft.item.Item.getIdFromItem(this.item);
    }

    /**
     * Sets the stack size of the item.
     *
     * @param stackSize the stack size
     * @return the Item
     */
    public Item setStackSize(int stackSize) {
        this.itemStack = new ItemStack(this.item, stackSize);
        return this;
    }

    /**
     * Gets the stack size of the item.
     *
     * @return the stack size
     */
    public int getStackSize() {
        return this.itemStack.stackSize;
    }

    /**
     * Gets the item's unlocalized name.<br>
     * Example: <code>tile.wood</code>
     *
     * @return the item's unlocalized name
     */
    public String getUnlocalizedName() {
        return this.item.getUnlocalizedName();
    }

    /**
     * Gets the item's registry name.<br>
     * Example: <code>minecraft:planks</code>
     *
     * @return the item's registry name
     */
    public String getRegistryName() {
        return this.item.getRegistryName();
    }

    /**
     * Gets the item's stack display name.<br>
     * Example: <code>Oak Wood Planks</code>
     *
     * @return the item's stack display name
     */
    public String getName() {
        return this.itemStack.getDisplayName();
    }

    /**
     * Returns a map of the enchantment name to level for all
     * the enchantments on an item.
     *
     * @return the map of enchantments
     */
    public Map<String, Integer> getEnchantments() {
        Map rawEnchants = EnchantmentHelper.getEnchantments(this.itemStack);
        Map<String, Integer> mappedEnchants = new HashMap<>();

        for (Object enchantObj : rawEnchants.entrySet()) {
            Map.Entry<Integer, Integer> rawEnchant = (Map.Entry<Integer, Integer>) enchantObj;

            Enchantment enchant = Enchantment.getEnchantmentById(rawEnchant.getKey());

            mappedEnchants.put(enchant.getName().replace("enchantment.", ""), rawEnchant.getValue());
        }

        return mappedEnchants;
    }

    /**
     * Gets if the item can be enchanted.
     *
     * @return true if the item can be enchanted
     */
    public Boolean isEnchantable() {
        return this.itemStack.isItemEnchantable();
    }

    /**
     * Gets if the item is enchanted.
     *
     * @return true if the item is enchanted
     */
    public Boolean isEnchanted() {
        return this.itemStack.isItemEnchanted();
    }

    /**
     * Returns a json string of the item's lore.
     *
     * @return the json string of the lore
     */
    public String getLore() {
        return this.itemStack.serializeNBT().toString();
    }

    /**
     * Gets if the item can be placed on a {@link Block}.
     *
     * @param block the {@link Block} to place the item on
     * @return true if the item can be placed on the {@link Block}
     */
    public Boolean canPlaceOn(Block block) {
        return this.itemStack.canPlaceOn(block.getBlock());
    }

    /**
     * Gets if the item can harvest a {@link Block}.
     *
     * @param block the {@link Block} for the item to harvest
     * @return true if the item can harvest the {@link Block}
     */
    public Boolean canHarvest(Block block) {
        return this.itemStack.canHarvestBlock(block.getBlock());
    }

    /**
     * Gets if the item can destroy a {@link Block}.
     *
     * @param block the {@link Block} for the item to destroy
     * @return true if the item can destroy the {@link Block}
     */
    public Boolean canDestroy(Block block) {
        return this.itemStack.canDestroy(block.getBlock());
    }

    /**
     * Gets the items damage.
     *
     * @return the items damage
     */
    public int getDamage() {
        return this.itemStack.getItemDamage();
    }

    /**
     * Gets the items max damage.
     *
     * @return the items max damage
     */
    public int getMaxDamage() {
        return this.itemStack.getMaxDamage();
    }

    /**
     * Checks if the item can take damage.
     *
     * @return true if the item can take damage
     */
    public Boolean isDamagable() {
        return this.itemStack.isItemStackDamageable();
    }
}
