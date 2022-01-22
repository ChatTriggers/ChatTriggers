package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos
import com.chattriggers.ctjs.utils.kotlin.MCChunk

@External
class Chunk(val chunk: MCChunk) {
    /**
     * Gets the x position of the chunk
     */
    fun getX() = chunk.pos.x

    /**
     * Gets the z position of the chunk
     */
    fun getZ() = chunk.pos.z

    /**
     * Gets the minimum x coordinate of a block in the chunk
     *
     * @return the minimum x coordinate
     */
    fun getMinBlockX() = getX() * 16

    /**
     * Gets the minimum z coordinate of a block in the chunk
     *
     * @return the minimum z coordinate
     */
    fun getMinBlockZ() = getZ() * 16

    /**
     * Gets the skylight level at the given position. This is the value seen in the debug (F3) menu
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the skylight level at the location
     */
    fun getSkyLightLevel(x: Int, y: Int, z: Int): Int {
        TODO("fabric")
    }

    /**
     * Gets the block light level at the given position. This is the value seen in the debug (F3) menu
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the block light level at the location
     */
    fun getBlockLightLevel(x: Int, y: Int, z: Int): Int {
        // TODO("fabric"): Verify this is correct?
        return World.getBlockStateAt(BlockPos(x, y, z)).luminance
    }

    /**
     * Gets every entity in this chunk
     *
     * @return the entity list
     */
    fun getAllEntities(): List<Entity> {
        val xRange = chunk.pos.startX..chunk.pos.endX
        val zRange = chunk.pos.startZ..chunk.pos.endZ

        // TODO: Is there a better way to do this?
        return World.getAllEntities().filter {
            it.getX().toInt() in xRange && it.getZ().toInt() in zRange
        }
    }

    /**
     * Gets every entity in this chunk of a certain class
     *
     * @param clazz the class to filter for (Use `Java.type().class` to get this)
     * @return the entity list
     */
    fun getAllEntitiesOfType(clazz: Class<*>): List<Entity> {
        return getAllEntities().filter {
            clazz.isInstance(it.entity)
        }
    }
}
