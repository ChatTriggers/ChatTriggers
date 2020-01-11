package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import net.minecraft.util.EnumFacing

class BlockFace(val facing: EnumFacing) {
    val axis = Axis(facing.axis)

    fun getName(): String = facing.getName()
    fun getXOffset(): Int = facing.frontOffsetX
    fun getYOffset(): Int = facing.frontOffsetY
    fun getZOffset(): Int = facing.frontOffsetZ

    class Axis(val axis: EnumFacing.Axis) {
        fun getName(): String = axis.getName()
        fun isHorizontal(): Boolean = axis.isHorizontal
        fun isVertical(): Boolean = axis.isVertical
    }
}