package com.chattriggers.ctjs.minecraft.wrappers.world

import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos
import com.chattriggers.ctjs.utils.kotlin.MCChunk

//#if MC<=11202
import net.minecraft.world.EnumSkyBlock
//#else
//$$ import com.chattriggers.ctjs.minecraft.wrappers.World
//$$ import net.minecraft.world.level.LightLayer
//$$ import net.minecraft.world.level.chunk.LevelChunk
//$$ import net.minecraft.world.level.chunk.ProtoChunk
//#endif

class Chunk(val chunk: MCChunk) {
    /**
     * Gets the x position of the chunk
     */
    fun getX(): Int {
        //#if MC<=11202
        return chunk.xPosition
        //#else
        //$$ return chunk.pos.x
        //#endif
    }

    /**
     * Gets the z position of the chunk
     */
    fun getZ(): Int {
        //#if MC<=11202
        return chunk.zPosition
        //#else
        //$$ return chunk.pos.z
        //#endif
    }

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
        //#if MC<=11202
        return chunk.getLightFor(EnumSkyBlock.SKY, MCBlockPos(x, y, z))
        //#else
        //$$ return World.getWorld()?.lightEngine?.getLayerListener(LightLayer.SKY)?.getLightValue(MCBlockPos(x, y, z)) ?: 0
        //#endif
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
        //#if MC<=11202
        return chunk.getLightFor(EnumSkyBlock.BLOCK, MCBlockPos(x, y, z))
        //#else
        //$$ return World.getWorld()?.lightEngine?.getLayerListener(LightLayer.BLOCK)?.getLightValue(MCBlockPos(x, y, z)) ?: 0
        //#endif
    }

    // TODO(CONVERT): Probably have to remove the next two methods
    //#if MC<=11202
    /**
     * Gets every entity in this chunk
     *
     * @return the entity list
     */
    fun getAllEntities(): List<Entity> {
        return chunk.entityLists.toList().flatten().map(::Entity)
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
    //#endif

    /**
     * Gets every tile entity in this chunk
     *
     * @return the tile entity list
     */
    fun getAllTileEntities(): List<TileEntity> {
        //#if MC<=11202
        return chunk.tileEntityMap.values.map(::TileEntity)
        //#else
        //$$ return when (chunk) {
        //$$     is ProtoChunk -> chunk.blockEntities.values.map(::TileEntity)
        //$$     is LevelChunk -> chunk.blockEntities.values.map(::TileEntity)
        //$$     else -> chunk.blockEntitiesPos.mapNotNull {
        //$$         chunk.getBlockEntity(it)?.let(::TileEntity)
        //$$     }
        //$$ }
        //#endif
    }

    /**
     * Gets every tile entity in this chunk of a certain class
     *
     * @param clazz the class to filter for (Use `Java.type().class` to get this)
     * @return the tile entity list
     */
    fun getAllTileEntitiesOfType(clazz: Class<*>): List<TileEntity> {
        return getAllTileEntities().filter {
            clazz.isInstance(it.tileEntity)
        }
    }
}
