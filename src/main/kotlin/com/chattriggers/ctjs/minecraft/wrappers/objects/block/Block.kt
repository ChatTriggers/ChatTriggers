package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item

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
     * {@link Player#lookingAt()} to specify the block face
     * being looked at.
     */
    fun withFace(face: BlockFace) = Block(type, pos, face)

    fun getState() = World.getBlockStateAt(pos)

    // TODO(1.16.2)
    //#if MC==10809
    fun getMetadata(): Int = type.mcBlock.getMetaFromState(getState())
    //#endif

    fun isPowered() = World.getWorld()!!.isBlockPowered(pos.toMCBlockPos())

    fun getRedstoneStrength() = World.getWorld()!!.getStrongPower(pos.toMCBlockPos())

    fun getLightValue(): Int {
        //#if MC==11602
        //$$ val blockPos = pos.toMCBlockPos()
        //$$ return this.type.mcBlock.getLightValue(
        //$$     World.getWorld()!!.getBlockState(pos.toMCBlockPos()),
        //$$     World.getWorld(),
        //$$     blockPos
        //$$ )
        //#else
        return this.type.mcBlock.lightValue
        //#endif
    }

    fun canProvidePower(): Boolean {
        //#if MC==11602
        //$$ return this.type.mcBlock.canProvidePower(
        //$$     World.getWorld()!!.getBlockState(pos.toMCBlockPos())
        //$$ )
        //#else
        return this.type.mcBlock.canProvidePower()
        //#endif
    }

    fun isTranslucent(): Boolean {
        //#if MC==11602
        //$$ return this.type.mcBlock.isTransparent(
        //$$     World.getWorld()!!.getBlockState(pos.toMCBlockPos())
        //$$ )
        //#else
        return this.type.mcBlock.isTranslucent
        //#endif
    }

    /**
     * Checks whether the block can be mined with the tool in the player's hand
     *
     * @return whether the block can be mined
     */
    fun canBeHarvested(): Boolean {
        return type.mcBlock.canHarvestBlock(
            //#if MC==11602
            //$$ World.getWorld()!!.getBlockState(pos.toMCBlockPos()),
            //#endif
            World.getWorld()!!,
            pos.toMCBlockPos(),
            Player.getPlayer()!!,
        )
    }

    fun canBeHarvestedWith(item: Item): Boolean = item.canHarvest(type)

    override fun toString() = "Block{type=${type.mcBlock.registryName}, x=$x, y=$y, z=$z}"
}
