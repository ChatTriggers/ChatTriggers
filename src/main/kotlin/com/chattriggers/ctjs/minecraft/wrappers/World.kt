package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.minecraft.wrappers.objects.Chunk
import com.chattriggers.ctjs.minecraft.wrappers.objects.Particle
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockType
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.PlayerEntity
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos
import com.chattriggers.ctjs.utils.kotlin.MCParticle
import com.chattriggers.ctjs.utils.kotlin.toIdentifier
import net.minecraft.block.BlockState
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.world.ClientWorld
import net.minecraft.particle.DefaultParticleType
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.lang.IllegalArgumentException

@External
object World {
    /**
     * Gets Minecraft's WorldClient object
     *
     * @return The Minecraft WorldClient object
     */
    @JvmStatic
    fun getWorld(): ClientWorld? = Client.getMinecraft().world

    fun getWorldRenderer(): WorldRenderer? = Client.getMinecraft().worldRenderer

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
        Player.getPlayer()?.playSound(SoundEvent(name.toIdentifier()), volume, pitch)
    }

    /**
     * Play a record at location x, y, and z.
     * Use "null" as name in the same location to stop record.
     *
     * @param name the name of the sound/record
     * @param x the x location
     * @param y the y location
     * @param z the z location
     */
    // TODO(BREAKING): name changed from String to String? (null instead of "null")
    @JvmStatic
    fun playRecord(name: String?, x: Double, y: Double, z: Double) {
        // TODO: Use null instead of "null"
        val sound = name?.let {
            Registry.SOUND_EVENT[it.toIdentifier()]
                ?: throw IllegalArgumentException("Unknown record name: \"$it\"")
        }
        Client.getMinecraft().worldRenderer.playSong(sound, MCBlockPos(x, y, z))
    }

    @JvmStatic
    fun isRaining(): Boolean = getWorld()?.isRaining ?: false

    @JvmStatic
    fun getRainingStrength(): Float = TODO()

    @JvmStatic
    fun getTime(): Long = getWorld()?.time ?: -1L

    @JvmStatic
    fun getDifficulty(): String = getWorld()?.difficulty.toString()

    @JvmStatic
    fun getMoonPhase(): Int = getWorld()?.moonPhase ?: -1

    @JvmStatic
    fun getSeed(): Long = getWorld()?.biomeAccess?.asMixin<BiomeAccessAccessor>()?.getSeed() ?: -1L

    @JvmStatic
    fun getType(): String = TODO()

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
    fun getBlockStateAt(pos: BlockPos): BlockState {
        return getWorld()!!.getBlockState(pos.toMCBlockPos())
    }

    /**
     * Gets all players in the world, and returns their wrapped versions.
     *
     * @return the players
     */
    @JvmStatic
    fun getAllPlayers(): List<PlayerEntity> = getWorld()?.players?.map(::PlayerEntity) ?: listOf()

    /**
     * Gets a player by their username, must be in the currently loaded chunks!
     *
     * @param name the username
     * @return the player with said username, or null if they don't exist.
     */
    @JvmStatic
    fun getPlayerByName(name: String): PlayerEntity? {
        // TODO: Is there a more efficient way to do this?
        return getWorld()?.players?.firstOrNull { it.name.string == name }?.let(::PlayerEntity)
    }

    @JvmStatic
    fun hasPlayer(name: String): Boolean = getPlayerByName(name) != null

    // TODO(BREAKING): Removed y parameter
    @JvmStatic
    fun getChunk(x: Int, z: Int): Chunk = Chunk(getWorld()!!.getChunk(x, z))

    @JvmStatic
    fun getAllEntities(): List<Entity> = getWorld()?.entities?.map(::Entity) ?: listOf()

    /**
     * Gets every entity loaded in the world of a certain class
     *
     * @param clazz the class to filter for (Use `Java.type().class` to get this)
     * @return the entity list
     */
    @JvmStatic
    fun getAllEntitiesOfType(clazz: Class<*>): List<Entity> {
        return getAllEntities().filter { it.entity?.javaClass == clazz }
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
        fun getSize(): Double = getWorld()!!.worldBorder.size

        /**
         * Gets the border target size.
         *
         * @return the border target size
         */
        @JvmStatic
        fun getTargetSize(): Double = getWorld()!!.worldBorder.sizeLerpTarget

        /**
         * Gets the border time until the target size is met.
         *
         * @return the border time until target
         */
        @JvmStatic
        fun getTimeUntilTarget(): Long = getWorld()!!.worldBorder.sizeLerpTime
    }

    /**
     * World spawn object for getting spawn location.
     */
    object spawn {
        /**
         * Gets the spawn x location.
         *
         * @return the spawn x location.
         */
        @JvmStatic
        fun getX(): Int = getWorld()!!.spawnPos.x

        /**
         * Gets the spawn y location.
         *
         * @return the spawn y location.
         */
        @JvmStatic
        fun getY(): Int = getWorld()!!.spawnPos.y

        /**
         * Gets the spawn z location.
         *
         * @return the spawn z location.
         */
        @JvmStatic
        fun getZ(): Int = getWorld()!!.spawnPos.z
    }

    object particle {
        /**
         * Gets an array of all the different particle names you can pass
         * to [spawnParticle]
         *
         * @return the array of name strings
         */
        @JvmStatic
        fun getParticleNames(): List<String> = Registry.PARTICLE_TYPE.ids.map {
            it.path
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
        // TODO(BREAKING): Return value is nullable
        // TODO(FEATURE): Add showOnMinimal flag
        @JvmStatic
        @JvmOverloads
        fun spawnParticle(
            particle: String,
            x: Double,
            y: Double,
            z: Double,
            xSpeed: Double,
            ySpeed: Double,
            zSpeed: Double,
            // TODO("fabric"): This default should match whatever behavior is present in 1.8.9
            showOnMinimal: Boolean = false,
        ): Particle? {
            val particleType = Registry.PARTICLE_TYPE.get(Identifier("minecraft:$particle"))

            val effect = if (particleType is DefaultParticleType) {
                particleType
            } else TODO("fabric")

            return getWorldRenderer()?.asMixin<WorldRendererAccessor>()?.spawnParticle(
                effect,
                false,
                showOnMinimal,
                x,
                y,
                z,
                xSpeed,
                ySpeed,
                zSpeed,
            )?.let(::Particle)
        }

        @JvmStatic
        fun spawnParticle(particle: MCParticle) {
            Client.getMinecraft().particleManager.addParticle(particle)
        }
    }
}
