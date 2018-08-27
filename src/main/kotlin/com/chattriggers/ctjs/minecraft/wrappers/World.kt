package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.wrappers.objects.Chunk
import com.chattriggers.ctjs.minecraft.wrappers.objects.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.Particle
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.kotlin.BlockPos
import com.chattriggers.ctjs.utils.kotlin.MCParticle
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.util.EnumParticleTypes
import net.minecraftforge.fml.relauncher.ReflectionHelper

//#if MC>=11202
//$$ import net.minecraft.util.ResourceLocation
//$$ import net.minecraft.util.SoundEvent
//#endif

object World {
    @JvmStatic
    fun getWorld(): WorldClient? {
        //#if MC<=10809
        return Client.getMinecraft().theWorld
        //#else
        //$$ return Client.getMinecraft().world;
        //#endif
    }

    @JvmStatic
    fun isLoaded() = getWorld() != null

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
        //$$ val sound = SoundEvent.REGISTRY.getObject(ResourceLocation("minecraft", name));
        //$$ Player.getPlayer()?.playSound(sound, volume, pitch);
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
        //#if MC<=10809
        getWorld()?.playRecord(BlockPos(x, y, z), name)
        //#else
        //$$ val sound = SoundEvent.REGISTRY.getObject(new ResourceLocation("minecraft", name));
        //$$ getWorld()?.playRecord(BlockPos(x, y, z), sound);
        //#endif
    }

    /**
     * Display a title.
     *
     * @param title    title text
     * @param subtitle subtitle text
     * @param fadeIn   time to fade in
     * @param time     time to stay on screen
     * @param fadeOut  time to fade out
     */
    @JvmStatic
    fun showTitle(title: String, subtitle: String, fadeIn: Int, time: Int, fadeOut: Int) {
        val gui = Client.getMinecraft().ingameGUI
        gui.displayTitle(ChatLib.addColor(title), null, fadeIn, time, fadeOut)
        gui.displayTitle(null, ChatLib.addColor(subtitle), fadeIn, time, fadeOut)
        gui.displayTitle(null, null, fadeIn, time, fadeOut)
    }

    @JvmStatic
    fun isRaining() = getWorld()?.worldInfo?.isRaining ?: false

    @JvmStatic
    fun getRainingStrength() = getWorld()?.rainingStrength ?: false

    @JvmStatic
    fun getTime() = getWorld()?.worldTime ?: -1L

    @JvmStatic
    fun getDifficulty() = getWorld()?.difficulty.toString()

    @JvmStatic
    fun getMoonPhase() = getWorld()?.moonPhase ?: -1

    @JvmStatic
    fun getSeed() = getWorld()?.seed ?: -1L

    @JvmStatic
    fun getType(): String {
        //#if MC<=10809
        return getWorld()?.worldType?.worldTypeName.toString()
        //#else
        //$$ return getWorld()?.worldType?.name.toString();
        //#endif
    }

    /**
     * Gets the [Block] at a location in the world.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the [Block] at the location
     */
    @JvmStatic
    fun getBlockAt(x: Int, y: Int, z: Int): Block {
        val blockPos = BlockPos(x, y, z)
        val blockState = getWorld()!!.getBlockState(blockPos)

        return Block(blockState.block).setBlockPos(blockPos)
    }

    /**
     * Gets all of the players in the world, and returns their wrapped versions.
     *
     * @return the players
     */
    @JvmStatic
    fun getAllPlayers() = getWorld()?.playerEntities?.map {
        PlayerMP(it)
    } ?: listOf()

    /**
     * Gets a player by their username, must be in the currently loaded world!
     *
     * @param name the username
     * @return the player with said username
     * @throws IllegalArgumentException if the player is not valid
     */
    @JvmStatic
    @Throws(IllegalArgumentException::class)
    fun getPlayerByName(name: String): PlayerMP {
        return PlayerMP(
                getWorld()?.getPlayerEntityByName(name)
                        ?: throw IllegalArgumentException()
        )
    }

    @JvmStatic
    fun hasPlayer(name: String) = getWorld()?.getPlayerEntityByName(name) != null

    @JvmStatic
    fun getChunk(x: Int, y: Int, z: Int): Chunk {
        return Chunk(
                getWorld()!!.getChunkFromBlockCoords(
                        BlockPos(x, y, z)
                )
        )
    }

    @JvmStatic
    fun getAllEntities(): List<Entity> {
        return getWorld()?.loadedEntityList?.map {
            Entity(it)
        } ?: listOf()
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
        fun getCenterX() = getWorld()!!.worldBorder.centerX

        /**
         * Gets the border center z location.
         *
         * @return the border center z location
         */
        @JvmStatic
        fun getCenterZ() = getWorld()!!.worldBorder.centerZ

        /**
         * Gets the border size.
         *
         * @return the border size
         */
        @JvmStatic
        fun getSize() = getWorld()!!.worldBorder.size

        /**
         * Gets the border target size.
         *
         * @return the border target size
         */
        @JvmStatic
        fun getTargetSize() = getWorld()!!.worldBorder.targetSize

        /**
         * Gets the border time until the target size is met.
         *
         * @return the border time until target
         */
        @JvmStatic
        fun getTimeUntilTarget() = getWorld()!!.worldBorder.timeUntilTarget
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
        fun getX() = getWorld()!!.spawnPoint.x

        /**
         * Gets the spawn y location.
         *
         * @return the spawn y location.
         */
        @JvmStatic
        fun getY() = getWorld()!!.spawnPoint.y

        /**
         * Gets the spawn z location.
         *
         * @return the spawn z location.
         */
        @JvmStatic
        fun getZ() = getWorld()!!.spawnPoint.z
    }

    object particle {
        /**
         * Gets an array of all the different particle names you can pass
         * to [.spawnParticle]
         *
         * @return the array of name strings
         */
        @JvmStatic
        fun getParticleNames() = EnumParticleTypes.values().map {
            it.name
        }.toList()

        /**
         * Spawns a particle into the world with the given attributes,
         * which can be configured further with the returned [Particle]
         *
         * @param particle the name of the particle to spawn, see [.getParticleNames]
         * @param x the x coordinate to spawn the particle at
         * @param y the y coordinate to spawn the particle at
         * @param z the z coordinate to spawn the particle at
         * @param xSpeed the motion the particle should have in the x direction
         * @param ySpeed the motion the particle should have in the y direction
         * @param zSpeed the motion the particle should have in the z direction
         * @return the newly spawned particle for further configuration
         */
        @JvmStatic
        fun spawnParticle(particle: String, x: Double, y: Double, z: Double, xSpeed: Double, ySpeed: Double, zSpeed: Double): Particle? {
            val particleType = EnumParticleTypes.valueOf(particle)

            try {
                val method = ReflectionHelper.findMethod(
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
                ) as MCParticle

                return Particle(fx)
            } catch (e: Exception) {
                Console.getInstance().printStackTrace(e)
            }

            return null
        }

        @JvmStatic
        fun spawnParticle(particle: MCParticle) {
            Client.getMinecraft().effectRenderer.addEffect(particle)
        }
    }
}
