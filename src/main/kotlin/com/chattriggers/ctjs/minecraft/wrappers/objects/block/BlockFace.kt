package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import net.minecraft.util.EnumFacing

class BlockFace(val facing: EnumFacing) {
    val axis = Axis(facing.axis)

    fun getName(): String = facing.getName()
    fun getXOffset(): Int = facing.frontOffsetX
    fun getYOffset(): Int = facing.frontOffsetY
    fun getZOffset(): Int = facing.frontOffsetZ

    override fun toString() = "BlockFace{name=${getName()}, xOffset=${getXOffset()}, yOffset=${getYOffset()}, zOffset = ${getZOffset()}, axis=$axis}"

    class Axis(val axis: EnumFacing.Axis) {
        fun getName(): String = axis.getName()
        fun isHorizontal(): Boolean = axis.isHorizontal
        fun isVertical(): Boolean = axis.isVertical

        override fun toString() = "Axis{name=${getName()}, horizontal=${isHorizontal()}, vertical=${isVertical()}}"
    }
}