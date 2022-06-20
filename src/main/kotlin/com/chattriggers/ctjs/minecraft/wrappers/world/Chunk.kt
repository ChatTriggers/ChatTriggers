package com.chattriggers.ctjs.minecraft.wrappers.world

import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockPos

//#if MC<=11202
import net.minecraft.world.EnumSkyBlock
//#else
//$$ import net.minecraft.client.multiplayer.ClientLevel
//$$ import net.minecraft.world.level.LightLayer
//$$ import net.minecraft.world.level.chunk.LevelChunk
//$$ import net.minecraft.world.level.chunk.ProtoChunk
//#endif

class Chunk(val chunk: net.minecraft.world.chunk.Chunk) {
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
        return chunk.getLightFor(EnumSkyBlock.SKY, BlockPos(x, y, z).toMCBlock())
        //#else
        //$$ return chunk.level.lightEngine.getLayerListener(LightLayer.SKY).getLightValue(BlockPos(x, y, z).toMCBlock())
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
        return chunk.getLightFor(EnumSkyBlock.BLOCK, BlockPos(x, y, z).toMCBlock())
        //#else
        //$$ return chunk.level.lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(BlockPos(x, y, z).toMCBlock())
        //#endif
    }

    /**
     * Gets every entity in this chunk
     *
     * @return the entity list
     */
    fun getAllEntities(): List<Entity> {
        //#if MC<=11202
        return chunk.entityLists.toList().flatten().map(::Entity)
        //#elseif MC>=11701
        //$$ return (chunk.level as ClientLevel).entitiesForRendering()?.map(::Entity) ?: listOf()
        //#endif
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

    /**
     * Gets every tile entity in this chunk
     *
     * @return the tile entity list
     */
    fun getAllTileEntities(): List<TileEntity> {
        //#if MC<=11202
        return chunk.tileEntityMap.values.map(::TileEntity)
        //#else
        //$$ return chunk.blockEntities.values.map(::TileEntity)
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
