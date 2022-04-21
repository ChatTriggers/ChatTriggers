package com.chattriggers.ctjs.minecraft.wrappers.world.block

import com.chattriggers.ctjs.minecraft.wrappers.utils.Vec3i
import com.chattriggers.ctjs.utils.kotlin.MCEnumFacing
import net.minecraft.util.EnumFacing
import net.minecraft.util.IStringSerializable
import java.util.function.Predicate

enum class BlockFace(
    val oppositeIndex: Int,
    val axisDirection: AxisDirection,
    val axis: Axis,
    val directionVec: Vec3i
) : IStringSerializable {
    Down(1, AxisDirection.Negative, Axis.Y, Vec3i(0, -1, 0)),
    Up(0, AxisDirection.Positive, Axis.Y, Vec3i(0, 1, 0)),
    North(3, AxisDirection.Negative, Axis.Z, Vec3i(0, 0, -1)),
    South(2, AxisDirection.Positive, Axis.Z, Vec3i(0, 0, 1)),
    West(5, AxisDirection.Negative, Axis.X, Vec3i(-1, 0, 0)),
    East(4, AxisDirection.Positive, Axis.X, Vec3i(1, 0, 0));

    fun getOpposite() = values()[oppositeIndex]

    fun getOffsetX() = directionVec.x

    fun getOffsetY() = directionVec.y

    fun getOffsetZ() = directionVec.z

    fun rotateAround(axis: Axis): BlockFace {
        return when (axis) {
            Axis.X -> if (this != West && this != East) rotateX() else this
            Axis.Y -> if (this != Up && this != Down) rotateY() else this
            Axis.Z -> if (this != North && this != South) rotateZ() else this
        }
    }

    fun rotateX() = when (this) {
        Down -> South
        Up -> North
        North -> Down
        South -> Up
        else -> throw IllegalStateException("Cannot rotate $this around x-axis")
    }

    fun rotateY() = when (this) {
        North -> East
        South -> West
        West -> North
        East -> South
        else -> throw IllegalStateException("Cannot rotate $this around y-axis")
    }

    fun rotateZ() = when (this) {
        Down -> West
        Up -> East
        West -> Up
        East -> Down
        else -> throw IllegalStateException("Cannot rotate $this around z-axis")
    }

    override fun getName() = name.lowercase()

    enum class Plane : Predicate<BlockFace>, Iterable<BlockFace> {
        Horizontal,
        Vertical;

        override fun test(t: BlockFace): Boolean {
            return t.axis.plane == this
        }

        fun facings() = when (this) {
            Horizontal -> arrayOf(North, East, West, South)
            Vertical -> arrayOf(Up, Down)
        }

        override fun iterator() = facings().iterator()
    }

    enum class AxisDirection(val offset: Int) {
        Positive(1),
        Negative(-1),
    }

    enum class Axis(val plane: Plane) : Predicate<BlockFace>, IStringSerializable {
        X(Plane.Horizontal),
        Y(Plane.Vertical),
        Z(Plane.Horizontal);

        fun isHorizontal() = plane == Plane.Horizontal

        fun isVertical() = plane == Plane.Vertical

        override fun test(t: BlockFace): Boolean {
            return t.axis == this
        }

        override fun getName(): String {
            return name.lowercase()
        }
    }

    companion object {
        fun fromMCEnumFacing(facing: MCEnumFacing) = when (facing) {
            EnumFacing.DOWN -> Down
            EnumFacing.UP -> Up
            EnumFacing.NORTH -> North
            EnumFacing.SOUTH -> South
            EnumFacing.WEST -> West
            EnumFacing.EAST -> East
        }
    }
}
