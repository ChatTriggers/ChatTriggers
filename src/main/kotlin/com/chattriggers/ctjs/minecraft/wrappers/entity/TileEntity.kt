package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.wrappers.world.block.Block
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockType
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCTileEntity

@External
class TileEntity(val tileEntity: MCTileEntity) {
    fun getX(): Int = getBlock().x

    fun getY(): Int = getBlock().y

    fun getZ(): Int = getBlock().z

    fun getBlock(): Block = Block(getBlockType(), getBlockPos())

    fun getBlockType(): BlockType = BlockType(tileEntity.blockType)

    fun getBlockPos(): BlockPos = BlockPos(tileEntity.pos)

    fun getMetadata(): Int = tileEntity.blockMetadata

    override fun toString(): String {
        return "TileEntity{x=${getX()}, y=${getY()}, z=${getZ()}, blockType=${getBlockType()}}"
    }
}
