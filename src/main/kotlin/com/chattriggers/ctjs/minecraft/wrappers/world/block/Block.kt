package com.chattriggers.ctjs.minecraft.wrappers.world.block

import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item

/**
 * An immutable reference to a placed block in the world. It
 * has a block type, a position, and optionally a specific face.
 */
open class Block(
    val type: BlockType,
    val pos: BlockPos,
    val face: BlockFace? = null,
) {
    val x: Int get() = pos.x
    val y: Int get() = pos.y
    val z: Int get() = pos.z

    fun withType(type: BlockType) = Block(type, pos, face)

    fun withPos(pos: BlockPos) = Block(type, pos, face)

    /**
     * Narrows this block to reference a certain face. Used by
     * [Player.lookingAt] to specify the block face
     * being looked at.
     */
    fun withFace(face: BlockFace) = Block(type, pos, face)

    fun getState() = World.getBlockStateAt(pos)

    // TODO(BREAKING): See TileEntity.getMetadata
    // fun getMetadata(): Int = type.mcBlock.getMetaFromState(getState())

    fun isPowered(): Boolean {
        //#if MC<=11202
        return World.getWorld()!!.isBlockPowered(pos.toMCBlock())
        //#else
        //$$ return World.getWorld()!!.hasNeighborSignal(pos.toMCBlock())
        //#endif
    }

    fun getRedstoneStrength(): Int {
        //#if MC<=11202
        return World.getWorld()!!.getStrongPower(pos.toMCBlock())
        //#else
        //$$ return World.getWorld()!!.getBestNeighborSignal(pos.toMCBlock())
        //#endif
    }

    /**
     * Checks whether the block can be mined with the tool in the player's hand
     *
     * @return whether the block can be mined
     */
    fun canBeHarvested(): Boolean {
        //#if MC<=11202
        return Player.getPlayer()?.canHarvestBlock(type.mcBlock) ?: false
        //#elseif MC>=11701
        //$$ return Player.getPlayer()?.hasCorrectToolForDrops(getState()) ?: false
        //#endif
    }

    // TODO: See Item.canHarvest
    // fun canBeHarvestedWith(item: Item): Boolean = item.canHarvest(type)

    override fun toString() = "Block{type=$type, x=$x, y=$y, z=$z}"
}
