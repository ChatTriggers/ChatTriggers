package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block;
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity;
import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

//#if MC<=10809
//$$ import net.minecraft.client.renderer.entity.RenderItem;
//#else
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.ITooltipFlag;
import com.chattriggers.ctjs.minecraft.wrappers.World;
//#endif

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {
    @Getter private net.minecraft.item.Item item;
    @Getter private ItemStack itemStack;

    /* Constructors */
    /**
     * Creates an Item object from a Minecraft ItemStack input.
     *
     * @param itemStack the Minecraft ItemStack
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

    /**
     * Creates an Item object from an EntityItem.
     *
     * @param entityItem the EntityItem.
     */
    public Item(EntityItem entityItem) {
        //#if MC<=10809
        //$$ this.item = entityItem.getEntityItem().getItem();
        //$$ this.itemStack = entityItem.getEntityItem();
        //#else
        this.item = entityItem.getItem().getItem();
        this.itemStack = entityItem.getItem();
        //#endif
    }

    /**
     * Created an Item object from an Entity.
     * Has to be wrapping an EntityItem.
     *
     * @param entity the Entity
     */
    public Item(Entity entity) {
        if (entity.getEntity() instanceof EntityItem) {
            //#if MC<=10809
            //$$ this.item = ((EntityItem) entity.getEntity()).getEntityItem().getItem();
            //$$ this.itemStack = ((EntityItem) entity.getEntity()).getEntityItem();
            //#else
            this.item = ((EntityItem) entity.getEntity()).getItem().getItem();
            this.itemStack = ((EntityItem) entity.getEntity()).getItem();
            //#endif
        } else {
            throw new IllegalArgumentException("Entity is not of type EntityItem");
        }
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
        //#if MC<=10809
        //$$ return this.itemStack.stackSize;
        //#else
        return this.itemStack.getCount();
        //#endif
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
        //#if MC<=10809
        //$$ return this.item.getRegistryName();
        //#else
        return this.item.getRegistryName().toString();
        //#endif
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

            //#if MC<=10809
            //$$ Enchantment enchant = Enchantment.getEnchantmentById(rawEnchant.getKey());
            //#else
            Enchantment enchant = Enchantment.getEnchantmentByID(rawEnchant.getKey());
            //#endif

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
    public String getItemNBT() {
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
        //#if MC<=10809
        //$$ return this.itemStack.canHarvestBlock(block.getBlock());
        //#else
        return this.itemStack.canHarvestBlock(
                World.getWorld().getBlockState(block.getBlockPos())
        );
        //#endif
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
     * Gets the items durability, i.e. the number of uses left
     *
     * @return the items durability
     */
    public int getDurability() {
        return this.getMaxDamage() - this.getDamage();
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
    public boolean isDamagable() {
        return this.itemStack.isItemStackDamageable();
    }

    /**
     * Gets the item lore/tooltip information in a list.
     *
     * @return the item lore
     */
    public List<String> getLore() {
        //#if MC<=10809
        //$$ return this.itemStack.getTooltip(Player.getPlayer(), Client.getMinecraft().gameSettings.advancedItemTooltips);
        //#else
        return this.itemStack.getTooltip(Player.getPlayer(), ITooltipFlag.TooltipFlags.ADVANCED);
        //#endif
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
     * Renders the item icon to the client's overlay.
     *
     * @param x the x location
     * @param y the y location
     * @param scale the scale
     */
    public void draw(float x, float y, float scale) {
        float finalX = x / scale;
        float finalY = y / scale;

        RenderItem itemRenderer = Client.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();

        GlStateManager.scale(scale, scale, 1f);
        GlStateManager.translate(finalX, finalY, 0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.zLevel = 200.0F;

        itemRenderer.renderItemIntoGUI(getItemStack(), 0, 0);

        GlStateManager.popMatrix();
    }

    /**
     * Renders the item icon to the client's overlay.
     *
     * @param x the x location
     * @param y the y location
     */
    public void draw(float x, float y) {
        draw(x, y, 1);
    }
}
