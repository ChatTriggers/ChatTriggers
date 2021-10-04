package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.wrappers.objects.Chunk
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.Particle as CTParticle // conflicts with remapped Particle class on 1.16
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockType
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.block.state.IBlockState
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.particle.EntityFX
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

//#if MC==11602
//$$ import net.minecraft.util.ResourceLocation
//$$ import net.minecraft.util.SoundEvent
//$$ import kotlin.reflect.jvm.isAccessible
//$$ import net.minecraft.entity.player.PlayerEntity
//$$ import net.minecraft.util.registry.Registry
//#else
import net.minecraft.util.EnumParticleTypes
import net.minecraft.client.renderer.RenderGlobal
//#endif

@External
object World {
    /**
     * Gets Minecraft's WorldClient object
     *
     * @return The Minecraft WorldClient object
     */
    @JvmStatic
    fun getWorld(): WorldClient? {
        //#if MC<=10809
        return Client.getMinecraft().theWorld
        //#else
        //$$ return Client.getMinecraft().world
        //#endif
    }

    @JvmStatic
    fun isLoaded(): Boolean = getWorld() != null

    /**
     * Play a sound at the player location.
     *
     * @param name   the name of the sound
     * @param volume the volume of the sound
     * @param pitch  the pitch of the sound
     */
    @JvmStatic
    fun playSound(name: String, volume: Float, pitch: Float) {
        //#if MC<=10809
        Player.getPlayer()?.playSound(name, volume, pitch)
        //#else
        //$$ val sound = SoundEvent(ResourceLocation("minecraft", name))
        //$$ Player.getPlayer()?.playSound(sound, volume, pitch)
        //#endif
    }

    /**
     * Play a record at location x, y, and z.<br></br>
     * Use "null" as name in the same location to stop record.
     *
     * @param name  the name of the sound/record
     * @param x     the x location
     * @param y     the y location
     * @param z     the z location
     */
    @JvmStatic
    fun playRecord(name: String, x: Double, y: Double, z: Double) {
        // TODO(1.16.2)
        //#if MC==10809
        getWorld()?.playRecord(MCBlockPos(x, y, z), name)
        //#endif
    }

    @JvmStatic
    fun isRaining(): Boolean = getWorld()?.worldInfo?.isRaining ?: false

    @JvmStatic
    fun getRainingStrength(): Float = getWorld()?.rainingStrength ?: -1f

    @JvmStatic
    fun getTime(): Long = getWorld()?.worldTime ?: -1L

    @JvmStatic
    fun getDifficulty(): String = getWorld()?.difficulty.toString()

    @JvmStatic
    fun getMoonPhase(): Int = getWorld()?.moonPhase ?: -1

    // TODO(1.16.2)
    //#if MC==10809
    @JvmStatic
    fun getSeed(): Long = getWorld()?.seed ?: -1L
    //#endif

    // TODO(1.16.2)
    //#if MC==10809
    @JvmStatic
    fun getType(): String {
        return getWorld()?.worldType?.worldTypeName.toString()
    }
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
        return getWorld()!!.getBlockState(pos.toMCBlockPos())
    }

    /**
     * Gets all of the players in the world, and returns their wrapped versions.
     *
     * @return the players
     */
    @JvmStatic
    fun getAllPlayers(): List<PlayerMP> {
        //#if MC==11602
        //$$ return getWorld()?.allEntities?.filterIsInstance<PlayerEntity>()?.map(::PlayerMP) ?: listOf()
        //#else
        return getWorld()?.playerEntities?.map(::PlayerMP) ?: listOf()
        //#endif
    }

    /**
     * Gets a player by their username, must be in the currently loaded chunks!
     *
     * @param name the username
     * @return the player with said username, or null if they dont exist.
     */
    @JvmStatic
    fun getPlayerByName(name: String): PlayerMP? {
        //#if MC==11602
        //$$ return getWorld()?.allEntities?.filterIsInstance<PlayerEntity>()?.firstOrNull()?.let(::PlayerMP)
        //#else
        return getWorld()?.getPlayerEntityByName(name)?.let(::PlayerMP)
        //#endif
    }

    @JvmStatic
    fun hasPlayer(name: String): Boolean = getPlayerByName(name) != null

    @JvmStatic
    fun getChunk(x: Int, y: Int, z: Int): Chunk {
        return Chunk(
            getWorld()!!.getChunkFromBlockCoords(
                MCBlockPos(x, y, z)
            )
        )
    }

    @JvmStatic
    fun getAllEntities(): List<Entity> {
        //#if MC==11602
        //$$ return getWorld()?.allEntities?.map(::Entity) ?: listOf()
        //#else
        return getWorld()?.loadedEntityList?.map {
            Entity(it)
        } ?: listOf()
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
        return getAllEntities().filter {
            it.entity.javaClass == clazz
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
        fun getSize(): Int = getWorld()!!.worldBorder.size

        /**
         * Gets the border target size.
         *
         * @return the border target size
         */
        @JvmStatic
        fun getTargetSize(): Double = getWorld()!!.worldBorder.targetSize

        /**
         * Gets the border time until the target size is met.
         *
         * @return the border time until target
         */
        @JvmStatic
        fun getTimeUntilTarget(): Long = getWorld()!!.worldBorder.timeUntilTarget
    }

    // TODO(1.16.2)
    //#if MC==10809
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
        fun getX(): Int = getWorld()!!.spawnPoint.x

        /**
         * Gets the spawn y location.
         *
         * @return the spawn y location.
         */
        @JvmStatic
        fun getY(): Int = getWorld()!!.spawnPoint.y

        /**
         * Gets the spawn z location.
         *
         * @return the spawn z location.
         */
        @JvmStatic
        fun getZ(): Int = getWorld()!!.spawnPoint.z
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
            return EnumParticleTypes.values().map {
                it.name
            }.toList()
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
            zSpeed: Double
        ): CTParticle? {
            val particleType = EnumParticleTypes.valueOf(particle)

            val fx = RenderGlobal::class.declaredMemberFunctions.firstOrNull {
                it.name == "spawnEntityFX" || it.name == "func_174974_b"
            }?.let {
                it.isAccessible = true
                it.call(
                    Client.getMinecraft().renderGlobal,
                    particleType.particleID,
                    particleType.shouldIgnoreRange,
                    x, y, z, xSpeed, ySpeed, zSpeed, intArrayOf()
                ) as EntityFX
            }!!

            /*val method = ReflectionHelper.findMethod(
                    RenderGlobal::class.java,
                    //#if MC<=10809
                    Client.getMinecraft().renderGlobal,
                    arrayOf("spawnEntityFX", "func_174974_b"),
                    //#else
                    //$$ "spawnEntityFX",
                    //$$ "func_174974_b",
                    //#endif
                    Int::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType,
                    Double::class.javaPrimitiveType,
                    Double::class.javaPrimitiveType,
                    Double::class.javaPrimitiveType,
                    Double::class.javaPrimitiveType,
                    Double::class.javaPrimitiveType,
                    Double::class.javaPrimitiveType,
                    IntArray::class.java
            )

            val fx = method.invoke(Client.getMinecraft().renderGlobal,
                    particleType.particleID,
                    particleType.shouldIgnoreRange,
                    x, y, z, xSpeed, ySpeed, zSpeed, intArrayOf()
            ) as EntityFX */

            return CTParticle(fx)
        }

        @JvmStatic
        fun spawnParticle(particle: EntityFX) {
            Client.getMinecraft().effectRenderer.addEffect(particle)
        }
    }
    //#endif
}
