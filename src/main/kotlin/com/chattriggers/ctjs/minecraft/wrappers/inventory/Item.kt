package com.chattriggers.ctjs.minecraft.wrappers.inventory

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockType
import com.chattriggers.ctjs.utils.kotlin.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.Constants

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
            ?: throw IllegalArgumentException("Item with name or id $itemName does not exist")
        itemStack = ItemStack(item)
    }

    constructor(itemID: Int) {
        item = MCItem.getItemById(itemID)
            ?: throw IllegalArgumentException("Item with id $itemID does not exist")
        itemStack = ItemStack(item)
    }

    constructor(block: BlockType) {
        item = MCItem.getItemFromBlock(block.mcBlock)
            ?: throw IllegalArgumentException("BlockType $block does not exist")
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
     * Create an Item object from an Entity.
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

    fun getTextComponent() = TextComponent(itemStack.chatComponent)

    fun getRawNBT() = itemStack.serializeNBT().toString()

    fun getNBT() = NBTTagCompound(itemStack.serializeNBT())

    @Deprecated("Use the better-named method", ReplaceWith("getNBT"))
    fun getItemNBT(): NBTTagCompound = getNBT()

    fun getID(): Int = MCItem.getIdFromItem(item)

    fun setStackSize(stackSize: Int) = apply {
        itemStack.stackSize = stackSize
    }

    fun getStackSize(): Int {
        //#if MC<=10809
        return itemStack.stackSize
        //#else
        //$$ return itemStack.count
        //#endif
    }

    /**
     * Gets the item's unlocalized name.
     * Example: tile.wood
     *
     * @return the item's unlocalized name
     */
    fun getUnlocalizedName(): String = item.unlocalizedName

    /**
     * Gets the item's registry name.
     * Example: minecraft:planks
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
     * Gets the item's stack display name.
     * Example: Oak Wood Planks
     *
     * @return the item's stack display name
     */
    fun getName(): String = itemStack.displayName

    /**
     * Sets the item's name.
     * @param name the new name
     */
    fun setName(name: String) = apply {
        itemStack.setStackDisplayName(ChatLib.addColor(name))
    }

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
     * Gets the item's durability, i.e. the number of uses left
     *
     * @return the item's durability
     */
    fun getDurability(): Int = getMaxDamage() - getDamage()

    fun getDamage(): Int = itemStack.itemDamage

    fun setDamage(damage: Int) = apply {
        itemStack.itemDamage = damage
    }

    fun getMaxDamage(): Int = itemStack.maxDamage

    fun isDamagable(): Boolean = itemStack.isItemStackDamageable

    /**
     * Gets the item's name and lore lines.
     *
     * @return the item's name and lore lines
     */
    fun getLore(): List<String> {
        //#if MC<=10809
        return itemStack.getTooltip(Player.getPlayer(), Client.getMinecraft().gameSettings.advancedItemTooltips)
        //#else
        //$$ return itemStack.getTooltip(Player.getPlayer(), ITooltipFlag.TooltipFlags.ADVANCED)
        //#endif
    }

    /**
     * Sets the item's lore. Does not set the item's name, use [setName] instead.
     * @param loreLines the new lore lines
     */
    fun setLore(vararg loreLines: String) = apply {
        if (itemStack.tagCompound == null) {
            itemStack.tagCompound = MCNBTTagCompound()
        }

        val lore = getNBT().getCompoundTag("tag").let {
            if (!it.rawNBT.hasKey("display")) {
                it["display"] = MCNBTTagCompound()
            }
            it.getCompoundTag("display")
        }.let {
            if (!it.rawNBT.hasKey("display")) {
                it["Lore"] = MCNBTTagList()
            }
            it.getTagList("Lore", Constants.NBT.TAG_STRING)
        }

        lore.tagList.clear()
        loreLines.forEach {
            lore.appendTag(MCNBTTagString(ChatLib.addColor(it)))
        }
    }

    /**
     * Renders the item icon to the client's overlay, with customizable overlay information.
     *
     * @param x the x location
     * @param y the y location
     * @param scale the scale
     * @param z the z level to draw the item at
     */
    @JvmOverloads
    fun draw(x: Float = 0f, y: Float = 0f, scale: Float = 1f, z: Float = 200f) {
        val itemRenderer = Client.getMinecraft().renderItem

        GlStateManager.scale(scale, scale, 1f)
        GlStateManager.translate(x / scale, y / scale, 0f)
        GlStateManager.color(1f, 1f, 1f, 1f)

        RenderHelper.enableStandardItemLighting()
        RenderHelper.enableGUIStandardItemLighting()

        itemRenderer.zLevel = z
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

