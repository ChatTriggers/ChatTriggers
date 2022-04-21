package com.chattriggers.ctjs.minecraft.wrappers.world.block

import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCBlock
import net.minecraft.block.state.IBlockState

/**
 * An immutable wrapper around Minecraft's Block object. Note
 * that this references a block "type", and not an actual block
 * in the world. If a reference to a particular block is needed,
 * use [Block]
 */
@External
class BlockType(val mcBlock: MCBlock) {
    constructor(block: BlockType) : this(block.mcBlock)

    constructor(blockName: String) : this(MCBlock.getBlockFromName(blockName)!!)

    constructor(blockID: Int) : this(MCBlock.getBlockById(blockID))

    constructor(item: Item) : this(MCBlock.getBlockFromItem(item.item))

    /**
     * Returns a PlacedBlock based on this block and the
     * provided BlockPos
     *
     * @param blockPos the block position
     * @return a PlacedBlock object
     */
    fun withBlockPos(blockPos: BlockPos) = Block(this, blockPos)

    fun getID(): Int = MCBlock.getIdFromBlock(mcBlock)

    /**
     * Gets the block's registry name.
     * Example: minecraft:planks
     *
     * @return the block's registry name
     */
    fun getRegistryName(): String {
        //#if MC<=10809
        return mcBlock.registryName
        //#else
        //$$ return block.registryName.toString()
        //#endif
    }

    /**
     * Gets the block's unlocalized name.
     * Example: tile.wood
     *
     * @return the block's unlocalized name
     */
    fun getUnlocalizedName(): String = mcBlock.unlocalizedName

    /**
     * Gets the block's localized name.
     * Example: Wooden Planks
     *
     * @return the block's localized name
     */
    fun getName(): String = mcBlock.localizedName

    fun getLightValue(): Int {
        //#if MC<=10809
        return mcBlock.lightValue
        //#else
        //$$ return block.getLightValue(
        //$$         World.getWorld()!!.getBlockState(blockPos),
        //$$         World.getWorld(),
        //$$         blockPos
        //$$ )
        //#endif
    }

    fun getDefaultState(): IBlockState = mcBlock.defaultState

    fun getDefaultMetadata(): Int = mcBlock.getMetaFromState(getDefaultState())

    fun canProvidePower(): Boolean {
        //#if MC<=10809
        return mcBlock.canProvidePower()
        //#else
        //$$ return block.canProvidePower(
        //$$         World.getWorld()!!.getBlockState(blockPos)
        //$$ )
        //#endif
    }

    fun getHarvestLevel(): Int = mcBlock.getHarvestLevel(getDefaultState())

    fun isTranslucent(): Boolean {
        //#if MC<=10809
        return mcBlock.isTranslucent
        //#else
        //$$ return block.isTranslucent(
        //$$         World.getWorld()!!.getBlockState(blockPos)
        //$$ )
        //#endif
    }

    override fun toString(): String = "BlockType{name=${mcBlock.registryName}}"
}
