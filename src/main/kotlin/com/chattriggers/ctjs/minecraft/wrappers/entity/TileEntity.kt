package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.wrappers.world.block.Block
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockType

class TileEntity(val tileEntity: net.minecraft.tileentity.TileEntity) {
    fun getX(): Int = getBlock().x

    fun getY(): Int = getBlock().y

    fun getZ(): Int = getBlock().z

    fun getBlock(): Block = Block(getBlockType(), getBlockPos())

    fun getBlockType(): BlockType {
        //#if MC<=11202
        return BlockType(tileEntity.blockType)
        //#else
        //$$ return BlockType(tileEntity.blockState.block)
        //#endif
    }

    fun getBlockPos(): BlockPos {
        //#if MC<=11202
        return BlockPos(tileEntity.pos)
        //#else
        //$$ return BlockPos(tileEntity.blockPos)
        //#endif
    }

    // TODO(BREAKING): Remove this, and maybe add a blockState getter/wrapper?
    //#if MC<=11202
    fun getMetadata(): Int = tileEntity.blockMetadata
    //#endif

    override fun toString(): String {
        return "TileEntity{x=${getX()}, y=${getY()}, z=${getZ()}, blockType=${getBlockType()}}"
    }
}
