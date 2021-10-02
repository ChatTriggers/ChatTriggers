package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCBlock
import net.minecraft.block.state.IBlockState

//#if MC==11602
//$$ import net.minecraftforge.registries.ForgeRegistries
//$$ import net.minecraft.util.ResourceLocation
//$$ import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
//$$ import com.chattriggers.ctjs.minecraft.wrappers.World
//#endif

/**
 * An immutable wrapper around Minecraft's Block object. Note
 * that this references a block "type", and not an actual block
 * in the world. If a reference to a particular block is needed,
 * use {@link Block}
 */
@External
class BlockType(val mcBlock: MCBlock) {
    constructor(block: BlockType) : this(block.mcBlock)

    //#if MC==11602
    //$$ constructor(blockName: String) : this(ForgeRegistries.BLOCKS.getValue(ResourceLocation(blockName))!!)
    //#else
    constructor(blockName: String) : this(MCBlock.getBlockFromName(blockName))
    //#endif

    // TODO(1.16.2)
    //#if MC==10809
    constructor(blockID: Int) : this(MCBlock.getBlockById(blockID))
    //#endif

    //#if MC==11602
    //$$ constructor(item: Item) : this(MCBlock.getBlockFromItem(item.item))
    //#else
    constructor(item: Item) : this(MCBlock.getBlockFromItem(item.item) ?: MCBlock.getBlockById(0))
    //#endif

    /**
     * Returns a PlacedBlock based on this block and the
     * provided BlockPos
     *
     * @param blockPos the block position
     * @return a PlacedBlock object
     */
    fun withBlockPos(blockPos: BlockPos) = Block(this, blockPos)

    // TODO(1.16.2)
    //#if MC==10809
    fun getID(): Int = MCBlock.getIdFromBlock(mcBlock)
    //#endif

    /**
     * Gets the block's registry name.<br>
     * Example: <code>minecraft:planks</code>
     *
     * @return the block's registry name
     */
    fun getRegistryName(): String {
        //#if MC<=10809
        return this.mcBlock.registryName
        //#else
        //$$ return this.mcBlock.registryName.toString()
        //#endif
    }

    /**
     * Gets the block's unlocalized name.<br>
     * Example: <code>tile.wood</code>
     *
     * @return the block's unlocalized name
     */
    fun getUnlocalizedName(): String = mcBlock.unlocalizedName

    /**
     * Gets the block's localized name.<br>
     * Example: <code>Wooden Planks</code>
     *
     * @return the block's localized name
     */
    //#if MC==11602
    //$$ fun getName(): String = TextComponent(mcBlock.translatedName).getFormattedText()
    //#else
    fun getName(): String = mcBlock.localizedName
    //#endif

    fun getDefaultState(): IBlockState = mcBlock.defaultState

    // TODO(1.16.2)
    //#if MC==10809
    fun getDefaultMetadata(): Int = mcBlock.getMetaFromState(getDefaultState())
    //#endif

    fun getHarvestLevel(): Int = mcBlock.getHarvestLevel(getDefaultState())

    override fun toString(): String = "BlockType{name=${mcBlock.registryName}}"
}
