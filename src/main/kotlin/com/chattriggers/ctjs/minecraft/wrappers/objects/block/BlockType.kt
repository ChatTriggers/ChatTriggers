package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.AbstractBlockAccessor
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCBlock
import com.chattriggers.ctjs.utils.kotlin.toIdentifier
import net.minecraft.tag.BlockTags
import net.minecraft.util.registry.Registry

/**
 * An immutable wrapper around Minecraft's Block object. Note
 * that this references a block "type", and not an actual block
 * in the world. If a reference to a particular block is needed,
 * use [Block]
 */
@External
class BlockType(val mcBlock: MCBlock) {
    constructor(block: BlockType) : this(block.mcBlock)

    constructor(blockName: String) : this(Registry.BLOCK[blockName.toIdentifier()])

    constructor(blockID: Int) : this(Registry.BLOCK.get(blockID))

    constructor(item: Item) : this(MCBlock.getBlockFromItem(item.item))

    /**
     * Returns a PlacedBlock based on this block and the
     * provided BlockPos
     *
     * @param blockPos the block position
     * @return a PlacedBlock object
     */
    fun withBlockPos(blockPos: BlockPos) = Block(this, blockPos)

    fun getID(): Int = Registry.BLOCK.getRawId(mcBlock)

    /**
     * Gets the block's registry name.
     * Example: minecraft:planks
     *
     * @return the block's registry name
     */
    fun getRegistryName() = Registry.BLOCK.getId(mcBlock).toString()

    /**
     * Gets the block's unlocalized name.
     * Example: tile.wood
     *
     * @return the block's unlocalized name
     */
    fun getUnlocalizedName(): String = mcBlock.translationKey

    /**
     * Gets the block's localized name.
     * Example: Wooden Planks
     *
     * @return the block's localized name
     */
    fun getName(): String = mcBlock.name.toString()

    // TODO("fabric")
    // fun getLightValue(): Int {
    //     return mcBlock.getLightValue(
    //             World.getWorld()!!.getBlockState(blockPos),
    //             World.getWorld(),
    //             blockPos
    //     )
    // }

    fun getDefaultState() = mcBlock.defaultState

    // TODO("fabric")
    // fun getDefaultMetadata(): Int = mcBlock.getMetaFromState(getDefaultState())

    fun canProvidePower(): Boolean {
        // It seems like no blocks actually use the argument
        return mcBlock.emitsRedstonePower(null)
    }

    // TODO(VERIFY): Compatibility with previous version
    // TODO: No item needs netherite tools to break, right?
    fun getHarvestLevel(): Int = when {
        getDefaultState().isIn(BlockTags.NEEDS_DIAMOND_TOOL) -> 3
        getDefaultState().isIn(BlockTags.NEEDS_IRON_TOOL) -> 2
        getDefaultState().isIn(BlockTags.NEEDS_STONE_TOOL) -> 1
        else -> 0
    }

    fun isTranslucent() = !mcBlock.asMixin<AbstractBlockAccessor>().getMaterial().blocksLight()

    override fun toString(): String = "BlockType{name=${getRegistryName()}}"
}
