package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11
import net.minecraft.item.Item as MCItem


//#if MC>10809
//$$ import net.minecraft.client.util.ITooltipFlag
//$$ import com.chattriggers.ctjs.minecraft.wrappers.World
//#endif

class Item {
    val item: MCItem
    var itemStack: ItemStack

    /* Constructors */
    constructor(itemStack: ItemStack?) {
        //#if MC<=10809
        if (itemStack == null) {
        //#else
        //$$ if (itemStack == null || itemStack == ItemStack.EMPTY) {
        //#endif
            this.item = ItemBlock(Block(0).block)
            this.itemStack = ItemStack(item)
        } else {
            this.item = itemStack.item
            this.itemStack = itemStack
        }
    }

    constructor(itemName: String) {
        item = MCItem.getByNameOrId(itemName)
        itemStack = ItemStack(item)
    }

    constructor(itemID: Int) {
        item = MCItem.getItemById(itemID)
        itemStack = ItemStack(item)
    }

    constructor(block: Block) {
        item = MCItem.getItemFromBlock(block.block)
        itemStack = ItemStack(item)
    }

    constructor(entityItem: EntityItem) {
        //#if MC<=10809
        this.item = entityItem.entityItem.item
        this.itemStack = entityItem.entityItem
        //#else
        //$$ this.item = entityItem.item.item;
        //$$ this.itemStack = entityItem.item;
        //#endif
    }

    /**
     * Created an Item object from an Entity.
     * Has to be wrapping an EntityItem.
     *
     * @param entity the Entity
     */
    constructor(entity: Entity) {
        if (entity.entity is EntityItem) {
            //#if MC<=10809
            this.item = entity.entity.entityItem.item
            this.itemStack = entity.entity.entityItem
            //#else
            //$$ this.item = entity.entity.item.item;
            //$$ this.itemStack = entity.entity.item;
            //#endif
        } else {
            throw IllegalArgumentException("Entity is not of type EntityItem")
        }
    }
    /* End of constructors */

    fun getID() = MCItem.getIdFromItem(item)

    fun setStackSize(stackSize: Int) = apply {
        itemStack = ItemStack(item, stackSize)
    }

    fun getStackSize(): Int {
        //#if MC<=10809
        return this.itemStack.stackSize
        //#else
        //$$ return this.itemStack.count
        //#endif
    }

    /**
     * Gets the item's unlocalized name.<br>
     * Example: <code>tile.wood</code>
     *
     * @return the item's unlocalized name
     */
    fun getUnlocalizedName() = item.unlocalizedName

    /**
     * Gets the item's registry name.<br>
     * Example: <code>minecraft:planks</code>
     *
     * @return the item's registry name
     */
    fun getRegistryName(): String {
        //#if MC<=10809
        return this.item.registryName.toString()
        //#else
        //$$ return this.item.registryName.toString()
        //#endif
    }

    /**
     * Gets the item's stack display name.<br>
     * Example: <code>Oak Wood Planks</code>
     *
     * @return the item's stack display name
     */
    fun getName(): String = if (getID() == 0) "air" else itemStack.displayName

    fun getEnchantments(): Map<String, Int> {
        return EnchantmentHelper.getEnchantments(itemStack).mapKeys {
            //#if MC<=10809
            Enchantment.getEnchantmentById(
            //#else
            //$$ Enchantment.getEnchantmentByID(
            //#endif
                it.key
            ).name.replace("enchantment.", "")
        }
    }

    fun isEnchantable() = itemStack.isItemEnchantable

    fun isEnchanted() = itemStack.isItemEnchanted

    fun getItemNBT() = itemStack.serializeNBT().toString()

    fun getMetadata() = itemStack.metadata

    fun canPlaceOn(block: Block) = itemStack.canPlaceOn(block.block)

    fun canHarvest(block: Block): Boolean {
        //#if MC<=10809
        return this.itemStack.canHarvestBlock(block.block)
        //#else
        //$$ return this.itemStack.canHarvestBlock(
        //$$         World.getWorld()!!.getBlockState(block.getBlockPos())
        //$$ );
        //#endif
    }

    fun canDestroy(block: Block) = itemStack.canDestroy(block.block)

    /**
     * Gets the items durability, i.e. the number of uses left
     *
     * @return the items durability
     */
    fun getDurability() = getMaxDamage() - getDamage()

    fun getDamage() = itemStack.itemDamage

    fun setDamage(damage: Int) = apply {
        itemStack.itemDamage = damage
    }

    fun getMaxDamage() = itemStack.maxDamage

    fun isDamagable() = itemStack.isItemStackDamageable

    fun getLore(): List<String> {
        //#if MC<=10809
        return itemStack.getTooltip(Player.getPlayer(), Client.getMinecraft().gameSettings.advancedItemTooltips)
        //#else
        //$$ return itemStack.getTooltip(Player.getPlayer(), ITooltipFlag.TooltipFlags.ADVANCED);
        //#endif
    }

    /**
     * Renders the item icon to the client's overlay.
     *
     * @param x the x location
     * @param y the y location
     * @param scale the scale
     */
    @JvmOverloads
    fun draw(x: Float, y: Float, scale: Float = 1f) {
        val itemRenderer = Client.getMinecraft().renderItem

        GlStateManager.pushMatrix()

        GlStateManager.scale(scale, scale, 1f)
        GlStateManager.translate(x / scale, y / scale, 0f)
        GL11.glColor4f(1f, 1f, 1f, 1f)

        RenderHelper.enableStandardItemLighting()
        RenderHelper.enableGUIStandardItemLighting()

        itemRenderer.zLevel = 200f
        itemRenderer.renderItemIntoGUI(itemStack, 0, 0)

        GlStateManager.popMatrix()
    }

    /**
     * Checks whether another Item is the same as this one.
     * It compares id, stack size, and durability.
     *
     * @param other the object to compare to
     * @return whether the objects are equal
     */
    override fun equals(other: Any?): Boolean {
        return other is Item &&
            getID() == other.getID() &&
            getStackSize() == other.getStackSize() &&
            getDamage() == other.getDamage()
    }

    override fun hashCode(): Int {
        var result = item.hashCode()
        result = 31 * result + itemStack.hashCode()
        return result
    }

    override fun toString() = itemStack.toString()
}