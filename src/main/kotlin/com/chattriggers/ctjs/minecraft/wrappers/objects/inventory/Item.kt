package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockType
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Item as MCItem


//#if MC>10809
//$$ import net.minecraft.client.util.ITooltipFlag
//$$ import com.chattriggers.ctjs.minecraft.wrappers.World
//#endif

@External
class Item {
    val item: MCItem
    var itemStack: ItemStack

    constructor(itemStack: ItemStack) {
        item = itemStack.item
        this.itemStack = itemStack
    }

    constructor(itemName: String) {
        item = MCItem.getByNameOrId(itemName)
        itemStack = ItemStack(item)
    }

    constructor(itemID: Int) {
        item = MCItem.getItemById(itemID)
        itemStack = ItemStack(item)
    }

    constructor(block: BlockType) {
        item = MCItem.getItemFromBlock(block.mcBlock)
        itemStack = ItemStack(item)
    }

    constructor(entityItem: EntityItem) {
        //#if MC<=10809
        item = entityItem.entityItem.item
        itemStack = entityItem.entityItem
        //#else
        //$$ item = entityItem.item.item
        //$$ itemStack = entityItem.item
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
            item = entity.entity.entityItem.item
            itemStack = entity.entity.entityItem
            //#else
            //$$ item = entity.entity.item.item
            //$$ itemStack = entity.entity.item
            //#endif
        } else {
            throw IllegalArgumentException("Entity is not of type EntityItem")
        }
    }
    /* End of constructors */

    fun getTextComponent() = TextComponent(itemStack.chatComponent)

    fun getRawNBT() = itemStack.serializeNBT().toString()

    fun getNBT() = NBTTagCompound(itemStack.serializeNBT())

    @Deprecated("Use the better-named method", ReplaceWith("getNBT"))
    fun getItemNBT(): NBTTagCompound = getNBT()

    fun getID(): Int = MCItem.getIdFromItem(item)

    fun setStackSize(stackSize: Int) = apply {
        itemStack = ItemStack(item, stackSize)
    }

    fun getStackSize(): Int {
        //#if MC<=10809
        return itemStack.stackSize
        //#else
        //$$ return itemStack.count
        //#endif
    }

    /**
     * Gets the item's unlocalized name.<br>
     * Example: <code>tile.wood</code>
     *
     * @return the item's unlocalized name
     */
    fun getUnlocalizedName(): String = item.unlocalizedName

    /**
     * Gets the item's registry name.<br>
     * Example: <code>minecraft:planks</code>
     *
     * @return the item's registry name
     */
    fun getRegistryName(): String {
        //#if MC<=10809
        return item.registryName.toString()
        //#else
        //$$ return item.registryName.toString()
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
                it.key
            )
                //#else
                //$$ it.key
                //#endif
                .name.replace("enchantment.", "")
        }
    }

    fun isEnchantable(): Boolean = itemStack.isItemEnchantable

    fun isEnchanted(): Boolean = itemStack.isItemEnchanted

    fun getMetadata(): Int = itemStack.metadata

    fun canPlaceOn(block: BlockType): Boolean = itemStack.canPlaceOn(block.mcBlock)

    fun canHarvest(block: BlockType): Boolean {
        //#if MC<=10809
        return itemStack.canHarvestBlock(block.mcBlock)
        //#else
        //$$ return itemStack.canHarvestBlock(
        //$$         World.getWorld().getBlockState(block.blockPos)
        //$$ )
        //#endif
    }

    fun canDestroy(block: BlockType): Boolean = itemStack.canDestroy(block.mcBlock)

    /**
     * Gets the items durability, i.e. the number of uses left
     *
     * @return the items durability
     */
    fun getDurability(): Int = getMaxDamage() - getDamage()

    fun getDamage(): Int = itemStack.itemDamage

    fun setDamage(damage: Int) = apply {
        itemStack.itemDamage = damage
    }

    fun getMaxDamage(): Int = itemStack.maxDamage

    fun isDamagable(): Boolean = itemStack.isItemStackDamageable

    fun getLore(): List<String> {
        //#if MC<=10809
        return itemStack.getTooltip(Player.getPlayer(), Client.getMinecraft().gameSettings.advancedItemTooltips)
        //#else
        //$$ return itemStack.getTooltip(Player.getPlayer(), ITooltipFlag.TooltipFlags.ADVANCED)
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
    fun draw(x: Float = 0f, y: Float = 0f, scale: Float = 1f) {
        val itemRenderer = Client.getMinecraft().renderItem

        GlStateManager.scale(scale, scale, 1f)
        GlStateManager.translate(x / scale, y / scale, 0f)
        GlStateManager.color(1f, 1f, 1f, 1f)

        RenderHelper.enableStandardItemLighting()
        RenderHelper.enableGUIStandardItemLighting()

        itemRenderer.zLevel = 200f
        itemRenderer.renderItemIntoGUI(itemStack, 0, 0)

        Renderer.finishDraw()
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

    override fun toString(): String = itemStack.toString()
}
