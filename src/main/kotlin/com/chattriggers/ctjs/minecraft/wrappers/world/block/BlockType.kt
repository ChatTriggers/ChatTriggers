package com.chattriggers.ctjs.minecraft.wrappers.world.block

import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.minecraft.wrappers.utils.ResourceLocation
import net.minecraft.block.state.IBlockState

//#if MC>=11701
//$$ import gg.essential.universal.wrappers.message.UTextComponent
//$$ import net.minecraft.core.Registry
//#endif

/**
 * An immutable wrapper around Minecraft's Block object. Note
 * that this references a block "type", and not an actual block
 * in the world. If a reference to a particular block is needed,
 * use [Block]
 */
class BlockType(val mcBlock: net.minecraft.block.Block) {
    constructor(block: BlockType) : this(block.mcBlock)

    constructor(blockName: String) : this(ResourceLocation("minecraft", blockName))

    constructor(resourceLocation: ResourceLocation) : this(resourceLocation.toMC())

    constructor(resourceLocation: net.minecraft.util.ResourceLocation) :
    //#if MC<=11202
        this(net.minecraft.block.Block.blockRegistry.getObject(resourceLocation))
    //#else
    //$$ this(Registry.BLOCK.get(resourceLocation)!!)
    //#endif

    constructor(blockID: Int) :
    //#if MC<=11202
        this(net.minecraft.block.Block.getBlockById(blockID))
    //#else
    //$$     this(Registry.BLOCK.byId(blockID))
    //#endif

    constructor(item: Item) :
    //#if MC<=11202
        this(net.minecraft.block.Block.getBlockFromItem(item.item))
    //#elseif MC>=11701
    //$$     this(net.minecraft.world.level.block.Block.byItem(item.item))
    //#endif

    /**
     * Returns a PlacedBlock based on this block and the
     * provided BlockPos
     *
     * @param blockPos the block position
     * @return a PlacedBlock object
     */
    fun withBlockPos(blockPos: BlockPos) = Block(this, blockPos)

    fun getID(): Int {
        //#if MC<=11202
        return net.minecraft.block.Block.getIdFromBlock(mcBlock)
        //#else
        //$$ return Registry.BLOCK.getId(mcBlock)
        //#endif
    }

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
        //$$ return Registry.BLOCK.getId(mcBlock).toString()
        //#endif
    }

    /**
     * Gets the block's unlocalized name.
     * Example: tile.wood
     *
     * @return the block's unlocalized name
     */
    fun getUnlocalizedName(): String {
        //#if MC<=11202
        return mcBlock.unlocalizedName
        //#else
        //$$ return mcBlock.descriptionId
        //#endif
    }

    /**
     * Gets the block's localized name.
     * Example: Wooden Planks
     *
     * @return the block's localized name
     */
    fun getName(): String {
        //#if MC<=11202
        return mcBlock.localizedName
        //#else
        //$$ return UTextComponent(mcBlock.name).formattedText
        //#endif
    }

    fun getLightValue(): Int {
        //#if MC<=10809
        return mcBlock.lightValue
        //#else
        //$$ return getDefaultState().lightEmission
        //#endif
    }

    fun getDefaultState(): IBlockState {
        //#if MC<=11202
        return mcBlock.defaultState
        //#else
        //$$ return mcBlock.defaultBlockState()
        //#endif
    }

    // TODO(BREAKING): See other block metadata methods
    // fun getDefaultMetadata(): Int = mcBlock.getMetaFromState(getDefaultState())

    fun canProvidePower(): Boolean {
        //#if MC<=10809
        return mcBlock.canProvidePower()
        //#else
        //$$ return getDefaultState().isSignalSource
        //#endif
    }

    // TODO(BREAKING): Probably remove this, or move to Block
    //#if MC<=11202
    fun getHarvestLevel(): Int = mcBlock.getHarvestLevel(getDefaultState())

    fun isTranslucent(): Boolean = mcBlock.isTranslucent
    //#endif

    override fun toString(): String = "BlockType{name=${getRegistryName()}}"
}
