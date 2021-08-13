package com.chattriggers.ctjs.minecraft.wrappers.objects.block

import kotlin.math.sqrt

open class Vec3i(val x: Int, val y: Int, val z: Int) : Comparable<Vec3i> {
    constructor(x: Number, y: Number, z: Number) : this(x.toInt(), y.toInt(), z.toInt())

    fun crossProduct(other: Vec3i): Vec3i {
        return Vec3i(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x,
        )
    }

    fun distanceSq(other: Vec3i): Double {
        val dx = (x - other.x).toDouble()
        val dy = (y - other.y).toDouble()
        val dz = (z - other.z).toDouble()
        return dx * dx + dy * dy + dz * dz
    }

    fun distance(other: Vec3i): Double {
        return sqrt(distanceSq(other))
    }

    fun distanceSqToCenter(x: Double, y: Double, z: Double): Double {
        val dx = this.x.toDouble() + 0.5 - x
        val dy = this.y.toDouble() + 0.5 - y
        val dz = this.z.toDouble() + 0.5 - z
        return dx * dx + dy * dy + dz * dz
    }

    override fun compareTo(other: Vec3i): Int {
        return if (y == other.y) {
            if (z == other.z) {
                x - other.x
            } else z - other.z
        } else y - other.y
    }

    override fun equals(other: Any?) = other is Vec3i && compareTo(other) == 0

    override fun hashCode() = (y + z * 31) * 31 + x

    override fun toString() = "Vec3i{x=$x,y=$y,z=$z}"
}