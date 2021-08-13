package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos

class BlockPos(x: Number, y: Number, z: Number) : Vec3i(x, y, z) {
    constructor(pos: Vec3i) : this(pos.x, pos.y, pos.z)

    constructor(pos: MCBlockPos) : this(pos.x, pos.y, pos.z)

    constructor(source: Entity) : this(source.getPos())

    fun add(other: Vec3i) = BlockPos(x + other.x, y + other.y, z + other.z)

    fun add(x: Number, y: Number, z: Number) = add(Vec3i(x, y, z))

    operator fun plus(other: Vec3i) = add(other)

    fun subtract(other: Vec3i) = BlockPos(x - other.x, y - other.y, z - other.z)

    fun subtract(x: Number, y: Number, z: Number) = subtract(Vec3i(x, y, z))

    operator fun minus(other: Vec3i) = subtract(other)

    @JvmOverloads
    fun up(n: Int = 1) = offset(BlockFace.Up, n)

    @JvmOverloads
    fun down(n: Int = 1) = offset(BlockFace.Down, n)

    @JvmOverloads
    fun north(n: Int = 1) = offset(BlockFace.North, n)

    @JvmOverloads
    fun south(n: Int = 1) = offset(BlockFace.South, n)

    @JvmOverloads
    fun east(n: Int = 1) = offset(BlockFace.East, n)

    @JvmOverloads
    fun west(n: Int = 1) = offset(BlockFace.West, n)

    @JvmOverloads
    fun offset(facing: BlockFace, n: Int = 1): BlockPos {
        return BlockPos(x + facing.getOffsetX() * n, y + facing.getOffsetY() * n, z + facing.getOffsetZ() * n)
    }

    fun toMCBlock() = MCBlockPos(x, y, z)
}