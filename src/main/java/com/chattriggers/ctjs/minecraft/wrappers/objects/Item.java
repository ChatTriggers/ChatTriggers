package com.chattriggers.ctjs.minecraft.wrappers.objects;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class Item {
    @Getter private net.minecraft.item.Item item;
    @Getter private ItemStack itemStack;

    @Getter private float x = 0;
    @Getter private float y = 0;
    @Getter private float scale = 1;

    /* Constructors */

    /**
     * Creates an Item object from a minecraft ItemStack input.<br>
     * This method is not meant for public use.
     *
     * @param itemStack the minecraft ItemStack
     */
    public Item(ItemStack itemStack) {
        ItemStack newItemStack = itemStack;
        if (itemStack == null)
            newItemStack = new ItemStack(new Block(0).getBlock());

        this.item = newItemStack.getItem();
        this.itemStack = newItemStack;
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
     * Creates an Item object from an integer ID input, 0 for air.
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
        return getID() == 0 ? "air" : this.itemStack.getDisplayName();
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
     * Gets the json string of the item's lore.
     *
     * @return the json string of the lore
     */
    public String getLore() {
        return this.itemStack.serializeNBT().toString();
    }

    /**
     * Gets the metadata of the item.
     *
     * @return the metadata
     */
    public int getMetadata() {
        return this.itemStack.getMetadata();
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
     * Gets the item's damage.
     *
     * @return the damage value
     */
    public int getDamage() {
        return this.itemStack.getItemDamage();
    }

    /**
     * Sets the item's damage.
     *
     * @param damage the damage value
     */
    public Item setDamage(int damage) {
        this.itemStack.setItemDamage(damage);

        return this;
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

    /**
     * Checks whether another Item is the same as this one.
     * It compares id, stack size, and durability.
     *
     * @param obj the object to compare to
     * @return whether the objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            Item other = ((Item) obj);

            return getID() == other.getID()
                    && getStackSize() == other.getStackSize()
                    && getDamage() == other.getDamage();
        }

        return false;
    }

    @Override
    public String toString() {
        return this.itemStack.toString();
    }

    /**
     * Sets the Item's x position on screen for use with {@link Item#draw()}.
     *
     * @param x the x position
     * @return the Item for method chaining
     */
    public Item setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the Item's y position on screen for use with {@link Item#draw()}.
     *
     * @param y the y position
     * @return the Item for method chaining
     */
    public Item setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * Sets the Item's scale on screen for use with {@link Item#draw()}.
     *
     * @param scale the x position
     * @return the Item for method chaining
     */
    public Item setScale(float scale) {
        this.scale = scale;
        return this;
    }

    /**
     * Renders the item icon to the client's overlay.
     */
    public void draw() {
        float finalX = this.x / this.scale;
        float finalY = this.y / this.scale;

        RenderItem itemRenderer = Client.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();

        GlStateManager.scale(this.scale, this.scale, 1f);
        GlStateManager.translate(finalX, finalY, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.zLevel = 200.0F;

        itemRenderer.renderItemIntoGUI(getItemStack(), 0, 0);

        GlStateManager.popMatrix();
    }
}
