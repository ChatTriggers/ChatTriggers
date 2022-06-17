package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.entity.Particle
import com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.entity.TileEntity
import com.chattriggers.ctjs.minecraft.wrappers.world.Chunk
import com.chattriggers.ctjs.minecraft.wrappers.world.block.Block
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockType
import com.chattriggers.ctjs.utils.kotlin.asMixin
import net.minecraft.block.state.IBlockState
import net.minecraft.client.multiplayer.WorldClient

//#if MC<=11202
import com.chattriggers.ctjs.launch.mixins.transformers.render.RenderGlobalAccessor
import net.minecraft.util.EnumParticleTypes
//#else
//$$ import com.chattriggers.ctjs.launch.mixins.transformers.render.LevelRendererAccessor
//$$ import com.chattriggers.ctjs.utils.kotlin.asMixin
//$$ import net.minecraft.core.Registry
//$$ import net.minecraft.core.particles.ParticleOptions
//$$ import net.minecraft.network.FriendlyByteBuf
//$$ import net.minecraft.resources.ResourceLocation
//$$ import net.minecraft.server.level.ServerLevel
//$$ import net.minecraft.sounds.SoundSource
//#endif

object World {
    /**
     * Gets Minecraft's WorldClient object
     *
     * @return The Minecraft WorldClient object
     */
    @JvmStatic
    fun getWorld(): WorldClient? {
        //#if MC<=11202
        return Client.getMinecraft().theWorld
        //#else
        //$$ return Client.getMinecraft().level
        //#endif
    }

    @JvmStatic
    fun isLoaded(): Boolean = getWorld() != null

    /**
     * Play a sound at the player location.
     *
     * @param name the name of the sound
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    @JvmStatic
    fun playSound(name: String, volume: Float, pitch: Float) {
        Client.scheduleTask {
            //#if MC<=10809
            Player.getPlayer()?.playSound(name, volume, pitch)
            //#else
            //$$ val sound = Registry.SOUND_EVENT.get(ResourceLocation("minecraft", name))
            //$$ Player.getPlayer()?.playSound(sound, volume, pitch)
            //#endif
        }
    }

    /**
     * Play a record at location x, y, and z.
     * Use null as name in the same location to stop record.
     *
     * @param name the name of the sound/record
     * @param x the x location
     * @param y the y location
     * @param z the z location
     */
    @JvmStatic
    fun playRecord(name: String, x: Double, y: Double, z: Double) {
        Client.scheduleTask {
            //#if MC<=10809
            getWorld()?.playRecord(net.minecraft.util.BlockPos(x, y, z), name)
            //#else
            //$$ val sound = Registry.SOUND_EVENT.get(ResourceLocation("minecraft", name)) ?: return@scheduleTask
            //$$ getWorld()?.playLocalSound(x, y, z, sound, SoundSource.RECORDS, 1.0f, 1.0f, false)
            //#endif
        }
    }

    @JvmStatic
    fun stopAllSounds() {
        //#if MC<=11202
        Client.getMinecraft().soundHandler.stopSounds()
        //#else
        //$$ Client.getMinecraft().soundManager.stop()
        //#endif
    }

    @JvmStatic
    fun isRaining(): Boolean {
        //#if MC<=11202
        return getWorld()?.worldInfo?.isRaining ?: false
        //#else
        //$$ return getWorld()?.levelData?.isRaining ?: false
        //#endif
    }

    @JvmStatic
    fun getRainingStrength(): Float {
        //#if MC<=11202
        return getWorld()?.rainingStrength ?: -1f
        //#else
        //$$ return getWorld()?.getRainLevel(1f) ?: -1f
        //#endif
    }

    @JvmStatic
    fun getTime(): Long {
        //#if MC<=11202
        return getWorld()?.worldTime ?: -1L
        //#else
        //$$ return getWorld()?.dayTime ?: -1L
        //#endif
    }

    @JvmStatic
    fun getDifficulty(): String = getWorld()?.difficulty.toString()

    @JvmStatic
    fun getMoonPhase(): Int = getWorld()?.moonPhase ?: -1

    @JvmStatic
    fun getSeed(): Long {
        //#if MC<=11202
        return getWorld()?.seed ?: -1L
        //#else
        //$$ return (Player.getPlayer()?.level as? ServerLevel)?.seed ?: -1L
        //#endif
    }

    // TODO(CONVERT)
    //#if MC<=11202
    @JvmStatic
    fun getType() = getWorld()?.worldType?.worldTypeName.toString()
    //#endif

    /**
     * Gets the [BlockType] at a location in the world.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the [BlockType] at the location
     */
    @JvmStatic
    fun getBlockAt(x: Number, y: Number, z: Number) = getBlockAt(BlockPos(x, y, z))

    /**
     * Gets the [BlockType] at a location in the world.
     *
     * @param pos The block position
     * @return the [BlockType] at the location
     */
    @JvmStatic
    fun getBlockAt(pos: BlockPos): Block {
        return Block(BlockType(getBlockStateAt(pos).block), pos)
    }

    /**
     * Gets the [IBlockState] at a location in the world.
     *
     * @param pos The block position
     * @return the [BlockType] at the location
     */
    @JvmStatic
    fun getBlockStateAt(pos: BlockPos): IBlockState {
        return getWorld()!!.getBlockState(pos.toMCBlock())
    }

    /**
     * Gets all of the players in the world, and returns their wrapped versions.
     *
     * @return the players
     */
    @JvmStatic
    fun getAllPlayers(): List<PlayerMP> {
        //#if MC<=11202
        return getWorld()?.playerEntities?.map(::PlayerMP) ?: listOf()
        //#else
        //$$ return getWorld()?.players()?.map(::PlayerMP) ?: listOf()
        //#endif
    }

    /**
     * Gets a player by their username, must be in the currently loaded chunks!
     *
     * @param name the username
     * @return the player with said username, or null if they don't exist.
     */
    @JvmStatic
    fun getPlayerByName(name: String): PlayerMP? {
        return getAllPlayers().firstOrNull { it.getName() == name }
    }

    @JvmStatic
    fun hasPlayer(name: String): Boolean = getPlayerByName(name) != null

    @JvmStatic
    fun getChunk(x: Int, y: Int, z: Int): Chunk {
        return Chunk(
            //#if MC<=11202
            getWorld()!!.getChunkFromBlockCoords(BlockPos(x, y, z).toMCBlock())
            //#else
            //$$ getWorld()!!.getChunk(BlockPos(x, y, z).toMCBlock())
            //#endif
        )
    }

    @JvmStatic
    fun getAllEntities(): List<Entity> {
        //#if MC<=11202
        return getWorld()?.loadedEntityList?.map(::Entity) ?: listOf()
        //#else
        //$$ return getWorld()?.entitiesForRendering()?.map(::Entity) ?: listOf()
        //#endif
    }

    /**
     * Gets every entity loaded in the world of a certain class
     *
     * @param clazz the class to filter for (Use `Java.type().class` to get this)
     * @return the entity list
     */
    @JvmStatic
    fun getAllEntitiesOfType(clazz: Class<*>): List<Entity> {
        return getAllEntities().filter { clazz.isInstance(it.entity) }
    }

    @JvmStatic
    fun getAllTileEntities(): List<TileEntity> {
        //#if MC<=11202
        return getWorld()?.loadedTileEntityList?.map(::TileEntity) ?: listOf()
        //#else
        //$$ // TODO(CONVERT)
        //$$ return listOf()
        //#endif
    }

    @JvmStatic
    fun getAllTileEntitiesOfType(clazz: Class<*>): List<TileEntity> {
        return getAllTileEntities().filter {
            clazz.isInstance(it.tileEntity)
        }
    }

    /**
     * World border object to get border parameters
     */
    object border {
        /**
         * Gets the border center x location.
         *
         * @return the border center x location
         */
        @JvmStatic
        fun getCenterX(): Double = getWorld()!!.worldBorder.centerX

        /**
         * Gets the border center z location.
         *
         * @return the border center z location
         */
        @JvmStatic
        fun getCenterZ(): Double = getWorld()!!.worldBorder.centerZ

        /**
         * Gets the border size.
         *
         * @return the border size
         */
        @JvmStatic
        fun getSize(): Double = getWorld()!!.worldBorder.size.toDouble()

        /**
         * Gets the border target size.
         *
         * @return the border target size
         */
        @JvmStatic
        fun getTargetSize(): Double {
            //#if MC<=11202
            return getWorld()!!.worldBorder.targetSize
            //#else
            //$$ return getWorld()!!.worldBorder.lerpTarget
            //#endif
        }

        /**
         * Gets the border time until the target size is met.
         *
         * @return the border time until target
         */
        @JvmStatic
        fun getTimeUntilTarget(): Long {
            //#if MC<=11202
            return getWorld()!!.worldBorder.timeUntilTarget
            //#else
            //$$ return getWorld()!!.worldBorder.lerpRemainingTime
            //#endif
        }
    }

    /**
     * World spawn object for getting spawn location.
     */
    object spawn {
        /**
         * Gets the spawn BlockPos.
         *
         * @return the spawn location.
         */
        @JvmStatic
        fun getPos(): BlockPos {
            //#if MC<=11202
            return BlockPos(getWorld()!!.spawnPoint)
            //#else
            //$$ return BlockPos(getWorld()!!.sharedSpawnPos)
            //#endif
        }

        /**
         * Gets the spawn x location.
         *
         * @return the spawn x location.
         */
        @JvmStatic
        fun getX(): Int = getPos().x

        /**
         * Gets the spawn y location.
         *
         * @return the spawn y location.
         */
        @JvmStatic
        fun getY(): Int = getPos().y

        /**
         * Gets the spawn z location.
         *
         * @return the spawn z location.
         */
        @JvmStatic
        fun getZ(): Int = getPos().z
    }

    object particle {
        /**
         * Gets an array of all the different particle names you can pass
         * to [spawnParticle]
         *
         * @return the array of name strings
         */
        @JvmStatic
        fun getParticleNames(): List<String> {
            // TODO(CONVERT): Introduce ResourceLocation wrapper and return this? Could also
            //                be used anywhere Registry is used

            //#if MC<=11202
            return EnumParticleTypes.values().map { it.name }.toList()
            //#else
            //$$ // TODO(VERIFY)
            //$$ return Registry.PARTICLE_TYPE.entrySet().map { it.value.javaClass.name }
            //#endif
        }

        /**
         * Spawns a particle into the world with the given attributes,
         * which can be configured further with the returned [Particle]
         *
         * @param particle the name of the particle to spawn, see [getParticleNames]
         * @param x the x coordinate to spawn the particle at
         * @param y the y coordinate to spawn the particle at
         * @param z the z coordinate to spawn the particle at
         * @param xSpeed the motion the particle should have in the x direction
         * @param ySpeed the motion the particle should have in the y direction
         * @param zSpeed the motion the particle should have in the z direction
         * @return the newly spawned particle for further configuration
         */
        @JvmStatic
        fun spawnParticle(
            particle: String,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double,
        ): Particle {
            //#if MC<=11202
            val particleType = EnumParticleTypes.valueOf(particle)

            val fx = Client.getMinecraft().renderGlobal.asMixin<RenderGlobalAccessor>().invokeSpawnEntityFX(
                particleType.particleID,
                particleType.shouldIgnoreRange,
                x,
                y,
                z,
                xSpeed,
                ySpeed,
                zSpeed
            )
            //#else
            //$$ val resourceLocation = ResourceLocation("minecraft", particle)
            //$$ val particleType = Registry.PARTICLE_TYPE.get(resourceLocation)!!
            //$$
            //$$ val fx = Client.getMinecraft().levelRenderer.asMixin<LevelRendererAccessor>().invokeAddParticleInternal(
            //$$     object : ParticleOptions {
            //$$         override fun getType() = particleType
            //$$
            //$$         override fun writeToNetwork(arg: FriendlyByteBuf) {}
            //$$
            //$$         override fun writeToString() = resourceLocation.toString()
            //$$     },
            //$$     particleType.overrideLimiter,
            //$$     true,
            //$$     x,
            //$$     y,
            //$$     z,
            //$$     xSpeed,
            //$$     ySpeed,
            //$$     zSpeed,
            //$$ )
            //#endif


            return Particle(fx)
        }

        @JvmStatic
        fun spawnParticle(particle: net.minecraft.client.particle.EntityFX) {
            //#if MC<=11202
            Client.getMinecraft().effectRenderer.addEffect(particle)
            //#else
            //$$ Client.getMinecraft().particleEngine.add(particle)
            //#endif
        }

        @JvmStatic
        fun spawnParticle(particle: Particle) = spawnParticle(particle.entity)
    }
}
