package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolItem

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

    // TODO("fabric")
    // fun getMetadata(): Int = type.mcBlock.getMetaFromState(getState())

    fun isPowered() = World.getWorld()?.isReceivingRedstonePower(pos.toMCBlockPos())

    fun getRedstoneStrength() = World.getWorld()?.getReceivedRedstonePower(pos.toMCBlockPos())

    /**
     * Checks whether the block can be mined with the tool in the player's hand
     *
     * @return whether the block can be mined
     */
    fun canBeHarvested() = (Player.getHeldItem()?.item as? MiningToolItem)?.isSuitableFor(getState()) ?: false

    fun canBeHarvestedWith(item: Item): Boolean = item.canHarvest(type)

    override fun toString() = "Block{type=${type.getRegistryName()}, x=$x, y=$y, z=$z}"
}
