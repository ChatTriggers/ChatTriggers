package com.chattriggers.ctjs.minecraft.wrappers.inventory

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.minecraft.wrappers.utils.ResourceLocation
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockType
import com.chattriggers.ctjs.utils.kotlin.*
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.Constants

//#if MC==11202
//$$ import net.minecraft.client.util.ITooltipFlag
//#endif

//#if MC<=11202
import net.minecraft.client.renderer.RenderHelper
//#elseif MC>=11701
//$$ import net.minecraft.world.item.TooltipFlag
//$$ import net.minecraftforge.registries.ForgeRegistries
//#endif

class Item {
    val item: MCItem
    var itemStack: ItemStack

    constructor(itemStack: ItemStack) {
        item = itemStack.item
        this.itemStack = itemStack
    }

    constructor(itemName: String) {
        val id = itemName.toIntOrNull()
        //#if MC<=11202
        val item = if (id != null) {
            MCItem.getItemById(id)
        } else {
            MCItem.itemRegistry.getObject(ResourceLocation(itemName).toMC())
        }
        //#else
        //$$ val item = if (id != null) {
        //$$     MCItem.byId(id)
        //$$ } else {
        //$$     ForgeRegistries.ITEMS.getValue(ResourceLocation(itemName).toMC())
        //$$ }
        //#endif
        if (item == null)
            throw IllegalArgumentException("Item with name or id $itemName does not exist")

        this.item = item
        itemStack = ItemStack(item)
    }

    constructor(resource: ResourceLocation) {
        //#if MC<=11202
        val item = MCItem.itemRegistry.getObject(resource.toMC())
        //#else
        //$$ val item = ForgeRegistries.ITEMS.getValue(resource.toMC())
        //#endif

        if (item == null)
            throw IllegalArgumentException("Item with resource $resource does not exist")
        this.item = item
        itemStack = ItemStack(item)
    }

    constructor(itemID: Int) {
        //#if MC<=11202
        val item = MCItem.getItemById(itemID)
        //#else
        //$$ val item = MCItem.byId(itemID)
        //#endif

        if (item == null)
            throw IllegalArgumentException("Item with id $itemID does not exist")
        this.item = item
        itemStack = ItemStack(item)
    }

    constructor(block: BlockType) {
        //#if MC<=11202
        val item = MCItem.getItemFromBlock(block.mcBlock)
        //#else
        //$$ val item = MCItem.byBlock(block.mcBlock)
        //#endif

        if (item == null)
            throw IllegalArgumentException("BlockType $block does not exist")
        this.item = item
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

    fun getTextComponent(): UTextComponent {
        //#if MC<=11202
        return UTextComponent(itemStack.chatComponent)
        //#else
        //$$ return UTextComponent(itemStack.hoverName)
        //#endif
    }

    fun getRawNBT() = itemStack.serializeNBT().toString()

    fun getNBT() = NBTTagCompound(itemStack.serializeNBT())

    @Deprecated("Use the better-named method", ReplaceWith("getNBT"))
    fun getItemNBT(): NBTTagCompound = getNBT()

    fun getID(): Int {
        //#if MC<=11202
        return MCItem.getIdFromItem(item)
        //#else
        //$$ return MCItem.getId(item)
        //#endif
    }

    fun setStackSize(stackSize: Int) = apply {
        //#if MC<=10809
        itemStack.stackSize = stackSize
        //#else
        //$$ itemStack.count = stackSize
        //#endif
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
    fun getUnlocalizedName(): String {
        //#if MC<=11202
        return item.unlocalizedName
        //#else
        //$$ return item.descriptionId
        //#endif
    }

    /**
     * Gets the item's registry name.
     * Example: minecraft:planks
     *
     * @return the item's registry name
     */
    fun getRegistryName(): String = item.registryName.toString()

    /**
     * Gets the item's stack display name.
     * Example: Oak Wood Planks
     *
     * @return the item's stack display name
     */
    fun getName(): String {
        //#if MC<=11202
        return itemStack.displayName
        //#else
        //$$ return UTextComponent(itemStack.displayName).unformattedText
        //#endif
    }

    /**
     * Sets the item's name.
     * @param name the new name
     */
    fun setName(name: String) = apply {
        //#if MC<=11202
        itemStack.setStackDisplayName(ChatLib.addColor(name))
        //#else
        //$$ itemStack.hoverName = UTextComponent(ChatLib.addColor(name))
        //#endif
    }

    fun getEnchantments(): Map<String, Int> {
        return EnchantmentHelper.getEnchantments(itemStack).mapKeys {
            //#if MC<=10809
            Enchantment.getEnchantmentById(it.key).name.replace("enchantment.", "")
            //#elseif MC<=11202
            //$$ it.key.name.replace("enchantment.", "")
            //#else
            //$$ it.key.descriptionId.replace("enchantment.", "")
            //#endif
        }
    }

    fun isEnchantable(): Boolean {
        //#if MC<=11202
        return itemStack.isItemEnchantable
        //#else
        //$$ return itemStack.isEnchantable
        //#endif
    }

    fun isEnchanted(): Boolean {
        //#if MC<=11202
        return itemStack.isItemEnchanted
        //#else
        //$$ return itemStack.isEnchanted
        //#endif
    }

    // TODO(BREAKING): Removed this. Metadata doesn't exist in newer versions, being
    //                 replaced completely by block states. I'd like to come up with
    //                 a cross-platform solution to get this information.
    // fun getMetadata(): Int = itemStack.metadata

    // TODO(BREAKING): Removed this. Newer versions require a specific BlockPos. Maybe
    //                 recreate the 1.8.9 canPlaceOn method manually, without the predicate
    //                 present in newer versions? Or just make this a method on Block?
    // fun canPlaceOn(block: BlockType): Boolean = itemStack.canPlaceOn(block.mcBlock)

    // TODO(BREAKING): See canPlaceOn
    // fun canHarvest(block: BlockType): Boolean {
    //     //#if MC<=10809
    //     return itemStack.canHarvestBlock(block.mcBlock)
    //     //#else
    //     //$$ return itemStack.canHarvestBlock(
    //     //$$         World.getWorld().getBlockState(block.blockPos)
    //     //$$ )
    //     //#endif
    // }

    // TODO(BREAKING: See canPlaceOn
    // fun canDestroy(block: BlockType): Boolean = itemStack.canDestroy(block.mcBlock)

    /**
     * Gets the item's durability, i.e. the number of uses left
     *
     * @return the item's durability
     */
    fun getDurability(): Int = getMaxDamage() - getDamage()

    fun getDamage(): Int {
        //#if MC<=11202
        return itemStack.itemDamage
        //#else
        //$$ return itemStack.damageValue
        //#endif
    }

    fun setDamage(damage: Int) = apply {
        //#if MC<=11202
        itemStack.itemDamage = damage
        //#else
        //$$ itemStack.damageValue = damage
        //#endif
    }

    fun getMaxDamage(): Int = itemStack.maxDamage

    fun isDamagable(): Boolean {
        //#if MC<=11202
        return itemStack.isItemStackDamageable
        //#else
        //$$ return itemStack.isDamageableItem
        //#endif
    }

    /**
     * Gets the item's name and lore lines.
     *
     * @return the item's name and lore lines
     */
    // TODO(BREAKING): Returns a list of UTextComponent
    fun getLore(): List<UTextComponent> {
        //#if MC<=10809
        return itemStack.getTooltip(Player.getPlayer(), Client.getMinecraft().gameSettings.advancedItemTooltips).map(::UTextComponent)
        //#elseif MC<=11202
        //$$ return itemStack.getTooltip(Player.getPlayer(), ITooltipFlag.TooltipFlags.ADVANCED).map(::UTextComponent)
        //#else
        //$$ return itemStack.getTooltipLines(Player.getPlayer(), TooltipFlag.Default.ADVANCED).map(::UTextComponent)
        //#endif
    }

    /**
     * Sets the item's lore. Does not set the item's name, use [setName] instead.
     * @param loreLines the new lore lines
     */
    fun setLore(vararg loreLines: String) = apply {
        //#if MC<=11202
        if (itemStack.tagCompound == null)
            itemStack.tagCompound = MCNBTTagCompound()

        val lore = getNBT().getCompoundTag("tag").let {
            if (!it.rawNBT.hasKey("display"))
                it["display"] = MCNBTTagCompound()
            it.getCompoundTag("display")
        }.let {
            if (!it.rawNBT.hasKey("display"))
                it["Lore"] = MCNBTTagList()
            it.getTagList("Lore", Constants.NBT.TAG_STRING)
        }

        lore.clear()
        loreLines.forEach {
            lore.appendTag(MCNBTTagString(ChatLib.addColor(it)))
        }
        //#else
        //$$ if (itemStack.tag == null)
        //$$     itemStack.tag = MCNBTTagCompound()
        //$$
        //$$ val lore = getNBT().getCompoundTag("tag").let {
        //$$     if (!it.rawNBT.contains("display"))
        //$$         it["display"] = MCNBTTagCompound()
        //$$     it.getCompoundTag("display")
        //$$ }.let {
        //$$     if (!it.rawNBT.contains("display"))
        //$$         it["Lore"] = MCNBTTagList()
        //$$     it.getTagList("Lore", Constants.NBT.TAG_STRING)
        //$$ }
        //$$
        //$$ lore.clear()
        //$$ loreLines.forEach {
        //$$     lore.appendTag(MCNBTTagString.valueOf(ChatLib.addColor(it)))
        //$$ }
        //#endif

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
        //#if MC<=11202
        val itemRenderer = Client.getMinecraft().renderItem
        //#else
        //$$ val itemRenderer = Client.getMinecraft().itemRenderer
        //#endif

        Renderer.scale(scale, scale, 1f)
        Renderer.translate(x / scale, y / scale, 0f)
        Renderer.colorize(1f, 1f, 1f, 1f)

        //#if MC<=11202
        RenderHelper.enableGUIStandardItemLighting()

        itemRenderer.zLevel = z
        itemRenderer.renderItemIntoGUI(itemStack, 0, 0)
        //#else
        //$$ // TODO(VERIFY): Lighting.setupForEntityInInventory()?
        //$$ itemRenderer.blitOffset = z
        //$$ itemRenderer.renderGuiItem(itemStack, 0, 0)
        //#endif

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

