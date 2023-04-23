package com.chattriggers.ctjs.minecraft.wrappers.utils

import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockFace
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockPos
import com.chattriggers.ctjs.utils.kotlin.MCRayTraceResult
import net.minecraft.util.Vec3

class RayTraceResult(val rayTraceResult: MCRayTraceResult) {
    /**
     * @return [BlockPos] of the block that was hit. Or null
     * if a block wasn't hit
     */
    fun getBlockPos() = rayTraceResult.blockPos?.let(::BlockPos)

    /**
     * @return A [String] representing the type of hit.
     * "ENTITY", "BLOCK" or "MISS"
     */
    fun getTypeOfHit() = rayTraceResult.typeOfHit.toString()

    /**
     * @return A [BlockFace] representing the side of the block
     * the ray trace hit. Or null if a block wasn't hit
     */
    fun getSideHit() = rayTraceResult.sideHit?.let { BlockFace.fromMCEnumFacing(it) }

    /**
     * @return [Vec3] position of the hit
     */
    fun getHitVec(): Vec3 = rayTraceResult.hitVec

    /**
     * @return the [Entity] that was hit, or null if none was hit
     */
    fun getEntityHit() = rayTraceResult.entityHit?.let(::Entity)

    override fun toString() = "RayTraceResult{" +
            "blockPos=${getBlockPos()}, " +
            "type=${getTypeOfHit()}, " +
            "sideHit=${getSideHit()}, " +
            "hitVec=${getHitVec()}, " +
            "entity=${getEntityHit()}" +
            "}"
}