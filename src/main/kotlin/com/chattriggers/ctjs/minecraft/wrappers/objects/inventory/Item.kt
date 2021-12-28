package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockType
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCBlock
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos
import com.chattriggers.ctjs.utils.kotlin.toIdentifier
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.fabricmc.fabric.api.renderer.v1.RendererAccess
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.pattern.CachedBlockPosition
import net.minecraft.client.item.TooltipContext
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.WorldView
import net.minecraft.item.Item as MCItem

@External
class Item {
    val item: MCItem
    var itemStack: ItemStack

    constructor(itemStack: ItemStack) {
        item = itemStack.item
        this.itemStack = itemStack
    }

    constructor(itemName: String) {
        item = Registry.ITEM[itemName.toIdentifier()]
        itemStack = ItemStack(item)
    }

    constructor(itemID: Int) {
        item = MCItem.byRawId(itemID)
        itemStack = ItemStack(item)
    }

    constructor(block: BlockType) {
        item = block.mcBlock.asItem()
        itemStack = ItemStack(item)
    }

    // TODO("fabric")
    // constructor(entityItem: ItemEntity) {
    //     item = entityItem.entityItem.item
    //     itemStack = entityItem.entityItem
    // }

    // TODO("fabric")
    // /**
    //  * Create an Item object from an Entity.
    //  * Has to be wrapping an EntityItem.
    //  *
    //  * @param entity the Entity
    //  */
    // constructor(entity: Entity) {
    //     if (entity.entity is EntityItem) {
    //         //#if MC<=10809
    //         item = entity.entity.entityItem.item
    //         itemStack = entity.entity.entityItem
    //         //#else
    //         //$$ item = entity.entity.item.item
    //         //$$ itemStack = entity.entity.item
    //         //#endif
    //     } else {
    //         throw IllegalArgumentException("Entity is not of type EntityItem")
    //     }
    // }

    // TODO(VERIFY)
    fun getTextComponent() = TextComponent(itemStack.name)

    fun getRawNBT() = itemStack.orCreateNbt.toString()

    fun getNBT() = NBTTagCompound(itemStack.orCreateNbt)

    // TODO(BREAKING): remove this
    // @Deprecated("Use the better-named method", ReplaceWith("getNBT"))
    // fun getItemNBT(): NBTTagCompound = getNBT()

    fun getID(): Int = MCItem.getRawId(item)

    fun setStackSize(stackSize: Int) = apply {
        itemStack = ItemStack(item, stackSize)
    }

    fun getStackSize(): Int = itemStack.maxCount

    /**
     * Gets the item's unlocalized name.
     * Example: tile.wood
     *
     * @return the item's unlocalized name
     */
    // TODO(VERIFY)
    fun getUnlocalizedName(): String = item.name.toString()

    /**
     * Gets the item's registry name.
     * Example: minecraft:planks
     *
     * @return the item's registry name
     */
    fun getRegistryName(): String = Registry.ITEM.getId(item).toString()

    /**
     * Gets the item's stack display name.
     * Example: Oak Wood Planks
     *
     * @return the item's stack display name
     */
    // TODO(VERIFY)
    fun getName(): String = if (getID() == 0) "air" else item.getName(itemStack).toString()

    // TODO(BREAKING): Keys now have namespaces
    fun getEnchantments(): Map<String, Int> {
        return EnchantmentHelper.get(itemStack).mapKeys {
            Registry.ENCHANTMENT.getId(it.key).toString()
        }
    }

    fun isEnchantable(): Boolean = itemStack.isEnchantable

    fun isEnchanted(): Boolean = itemStack.hasEnchantments()

    // TODO("fabric")
    // fun getMetadata(): Int = itemStack.

    // TODO("fabric"): Verify this actually does what I want in the methods below
    class CachedBlockType(view: WorldView, val type: BlockType) : CachedBlockPosition(view, BlockPos.ORIGIN, false) {
        override fun getBlockState(): BlockState {
            return type.getDefaultState()
        }
    }

    fun canPlaceOn(block: BlockType): Boolean {
        val world = World.getWorld() ?: return false
        return itemStack.canPlaceOn(world.tagManager, CachedBlockType(world, block))
    }

    // TODO(BREAKING): Remove this method
    // fun canHarvest(block: BlockType): Boolean {
    //     return itemStack.canHarvestBlock(block.mcBlock)
    // }

    fun canDestroy(block: BlockType): Boolean {
        val world = World.getWorld() ?: return false
        return itemStack.canDestroy(world.tagManager, CachedBlockType(world, block))
    }

    /**
     * Gets the item's durability, i.e. the number of uses left
     *
     * @return the item's durability
     */
    fun getDurability(): Int = getMaxDamage() - getDamage()

    fun getDamage(): Int = itemStack.damage

    fun setDamage(damage: Int) = apply {
        itemStack.damage = damage
    }

    fun getMaxDamage(): Int = itemStack.maxDamage

    fun isDamagable(): Boolean = itemStack.isDamageable

    fun getLore(): List<String> {
        val context = if (Client.getMinecraft().options.advancedItemTooltips) {
            TooltipContext.Default.ADVANCED
        } else TooltipContext.Default.NORMAL

        return itemStack.getTooltip(Player.getPlayer(), context).map { TextComponent(it).getFormattedText() }
    }

    // TODO("fabric")
    // /**
    //  * Renders the item icon to the client's overlay.
    //  *
    //  * @param x the x location
    //  * @param y the y location
    //  * @param scale the scale
    //  */
    // @JvmOverloads
    // fun draw(x: Float = 0f, y: Float = 0f, scale: Float = 1f) {
    //     val itemRenderer = Client.getMinecraft().renderItem
    //
    //     GlStateManager.scale(scale, scale, 1f)
    //     GlStateManager.translate(x / scale, y / scale, 0f)
    //     GlStateManager.color(1f, 1f, 1f, 1f)
    //
    //     RenderHelper.enableStandardItemLighting()
    //     RenderHelper.enableGUIStandardItemLighting()
    //
    //     itemRenderer.zLevel = 200f
    //     itemRenderer.renderItemIntoGUI(itemStack, 0, 0)
    //
    //     Renderer.finishDraw()
    // }

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
