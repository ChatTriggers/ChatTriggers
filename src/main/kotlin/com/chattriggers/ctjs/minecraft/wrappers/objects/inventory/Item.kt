package com.chattriggers.ctjs.minecraft.wrappers.objects.inventory

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockType
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11


//#if MC>10809
//$$ import net.minecraft.client.util.ITooltipFlag
//$$ import com.chattriggers.ctjs.minecraft.wrappers.World
//$$ import net.minecraft.util.registry.Registry
//$$ import net.minecraft.util.ResourceLocation
//$$ import net.minecraftforge.registries.ForgeRegistries
//#else
import net.minecraft.enchantment.Enchantment
//#endif

@External
class Item {
    val item: net.minecraft.item.Item
    var itemStack: ItemStack

    /* Constructors */
    constructor(itemStack: ItemStack) {
        this.item = itemStack.item
        this.itemStack = itemStack
    }

    constructor(itemName: String) {
        //#if MC==11602
        //$$ item = ForgeRegistries.ITEMS.getValue(ResourceLocation(itemName))!!
        //#else
        item = net.minecraft.item.Item.getByNameOrId(itemName)
        //#endif
        itemStack = ItemStack(item)
    }

    constructor(itemID: Int) {
        item = net.minecraft.item.Item.getItemById(itemID)
        itemStack = ItemStack(item)
    }

    constructor(block: BlockType) {
        item = net.minecraft.item.Item.getItemFromBlock(block.mcBlock)
        itemStack = ItemStack(item)
    }

    constructor(entityItem: EntityItem) {
        //#if MC<=10809
        this.item = entityItem.entityItem.item
        this.itemStack = entityItem.entityItem
        //#else
        //$$ this.item = entityItem.item.item
        //$$ this.itemStack = entityItem.item
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
            //$$ this.item = entity.entity.item.item
            //$$ this.itemStack = entity.entity.item
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

    fun getID(): Int = net.minecraft.item.Item.getIdFromItem(item)

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
    fun getUnlocalizedName(): String = item.unlocalizedName

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
    fun getName(): String = if (getID() == 0) "air" else TextComponent(itemStack.displayName).getUnformattedText()

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

    //#if MC!=11602
    // TODO(1.16.2)
    fun getMetadata(): Int = itemStack.metadata

    fun canPlaceOn(block: BlockType): Boolean = itemStack.canPlaceOn(block.mcBlock)
    //#endif

    fun canHarvest(block: BlockType): Boolean {
        //#if MC<=10809
        return this.itemStack.canHarvestBlock(block.mcBlock)
        //#else
        //$$ return this.itemStack.canHarvestBlock(block.getDefaultState())
        //#endif
    }

    //#if MC!=11602
    fun canDestroy(block: BlockType): Boolean = itemStack.canDestroy(block.mcBlock)
    //#endif

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
        //$$ return itemStack
        //$$     .getTooltip(Player.getPlayer(), ITooltipFlag.TooltipFlags.ADVANCED)
        //$$     .map { TextComponent(it).getFormattedText() }
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
        //#if MC==11602
        //$$ val itemRenderer = Client.getMinecraft().itemRenderer
        //#else
        val itemRenderer = Client.getMinecraft().renderItem
        //#endif

        GlStateManager.scale(scale, scale, 1f)
        GlStateManager.translate(x / scale, y / scale, 0f)
        GL11.glColor4f(1f, 1f, 1f, 1f)

        RenderHelper.enableStandardItemLighting()
        // TODO(1.16.2): Is there an equivalent? Or is this no longer necessary
        //#if MC==10809
        RenderHelper.enableGUIStandardItemLighting()
        //#endif

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
