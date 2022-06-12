package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.utils.kotlin.asMixin
import net.minecraft.client.audio.SoundManager
import org.mozilla.javascript.NativeObject
import java.io.File
import java.net.MalformedURLException

//#if MC<=11202
import net.minecraftforge.fml.relauncher.ReflectionHelper
import com.chattriggers.ctjs.launch.mixins.transformers.SoundHandlerAccessor
import net.minecraft.client.audio.SoundCategory
import paulscode.sound.SoundSystem
//#elseif MC>=11701
//$$ import com.chattriggers.ctjs.launch.mixins.transformers.AbstractSoundInstanceAccessor
//$$ import com.chattriggers.ctjs.launch.mixins.transformers.SoundAccessor
//$$ import com.chattriggers.ctjs.launch.mixins.transformers.SoundManagerAccessor
//$$ import net.minecraft.client.sounds.WeighedSoundEvents
//$$ import net.minecraft.client.resources.sounds.AbstractSoundInstance
//$$ import net.minecraft.client.resources.sounds.SoundInstance
//$$ import net.minecraft.resources.ResourceLocation
//$$ import net.minecraft.client.sounds.SoundEngine
//$$ import net.minecraft.sounds.SoundSource
//#endif

// TODO(BREAKING) attenuation is now a boolean (true = linear, false = none). New
//                versions don't have rolloff
/**
 * Instances a new Sound with certain properties. These properties
 * should be passed through as a normal JavaScript object.
 *
 * REQUIRED:
 * - source (String) - filename, relative to ChatTriggers assets directory
 *
 * OPTIONAL:
 * - priority (boolean) - whether this sound should be prioritized, defaults to false
 * - loop (boolean) - whether to loop this sound over and over, defaults to false
 * - stream (boolean) - whether to stream this sound rather than preload it (should be true for large files), defaults to false
 *
 * CONFIGURABLE (can be set in config object, or changed later, but MAKE SURE THE WORLD HAS LOADED)
 * - category (String) - which category this sound should be a part of, see [setCategory].
 * - volume (float) - volume of the sound, see [setVolume]
 * - pitch (float) - pitch of the sound, see [setPitch]
 * - x, y, z (float) - location of the sound, see [setPosition]. Defaults to the players position.
 * - attenuation (boolean) - fade out model of the sound, see [setAttenuation]
 *
 * @param config the JavaScript config object
 */
class Sound(private val config: NativeObject) {
    //#if MC<=11202
    private val source: String = config["source"] as String
    //#else
    //$$ private lateinit var source: AbstractSoundInstance
    //#endif
    var isListening = false

    init {
        if (World.isLoaded()) {
            loadSndSystem()

            try {
                bootstrap()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

        } else {
            isListening = true
        }

        CTJS.sounds.add(this)
    }

    fun onWorldLoad() {
        isListening = false

        loadSndSystem()

        try {
            bootstrap()
        } catch (exc: MalformedURLException) {
            exc.printStackTrace()
        }

    }

    @Throws(MalformedURLException::class)
    private fun bootstrap() {
        val source = config["source"]?.toString() ?: throw IllegalArgumentException("Sound source is null.")
        val priority = config.getOrDefault("priority", false) as Boolean
        val loop = config.getOrDefault("loop", false) as Boolean
        val stream = config.getOrDefault("stream", false) as Boolean

        val url = File(CTJS.assetsDir, source).toURI()
        val x = (config.getOrDefault("x", Player.getX()) as Number).toFloat()
        val y = (config.getOrDefault("y", Player.getY()) as Number).toFloat()
        val z = (config.getOrDefault("z", Player.getZ()) as Number).toFloat()
        val attenuation = (config.getOrDefault("attenuation", false) as Boolean)
        val distOrRoll = 16

        //#if MC<=11202
        if (stream) {
            sndSystem!!.newStreamingSource(
                priority,
                source,
                url.toURL(),
                source,
                loop,
                x,
                y,
                z,
                if (attenuation) 2 else 0,
                distOrRoll.toFloat()
            )
        } else {
            sndSystem!!.newSource(
                priority,
                source,
                url.toURL(),
                source,
                loop,
                x,
                y,
                z,
                if (attenuation) 2 else 0,
                distOrRoll.toFloat()
            )
        }

        if (config["volume"] != null) {
            setVolume((config["volume"] as Number).toFloat())
        }

        if (config["pitch"] != null) {
            setPitch((config["pitch"] as Number).toFloat())
        }

        if (config["category"] != null) {
            setCategory(config["category"] as String)
        }
        //#else
        //$$ val volume = config["volume"]?.let {
        //$$     (it as Number).toFloat()
        //$$ } ?: 1.0f
        //$$
        //$$ val pitch = config["pitch"]?.let {
        //$$     (it as Number).toFloat()
        //$$ } ?: 1.0f
        //$$
        //$$ val category = config["category"]?.let { c ->
        //$$     SoundSource.values().first { it.name == (c as String).lowercase() }
        //$$ } ?: SoundSource.MASTER
        //$$
        //$$ // TODO(VERIFY): See how this gets resolved to a path once we can launch the game
        //$$ this.source = object : AbstractSoundInstance(ResourceLocation("chattriggers", url.path), category) {
        //$$     init {
        //$$         this.volume = volume
        //$$         this.pitch = pitch
        //$$         this.x = x.toDouble()
        //$$         this.y = y.toDouble()
        //$$         this.z = z.toDouble()
        //$$         this.looping = loop
        //$$         this.attenuation = if (attenuation) {
        //$$             SoundInstance.Attenuation.LINEAR
        //$$         } else SoundInstance.Attenuation.NONE
        //$$     }
        //$$
        //$$     override fun resolve(arg: SoundManager): WeighedSoundEvents? {
        //$$         return super.resolve(arg).also {
        //$$             if (this.sound == null)
        //$$                 return@also
        //$$
        //$$             this.sound.asMixin<SoundAccessor>().setStream(stream)
        //$$             this.sound.asMixin<SoundAccessor>().setAttenuationDistance(distOrRoll)
        //$$             this.sound.asMixin<SoundAccessor>().setPreload(priority) // TODO(VERIFY): ???
        //$$         }
        //$$     }
        //$$ }
        //#endif
    }

    /**
     * Sets the category of this sound, making it respect the Player's sound volume sliders.
     * Options are: master, music, record, weather, block, hostile, neutral, player, and ambient
     *
     * @param category the category
     */
    fun setCategory(category: String) = apply {
        //#if MC<=11202
        val category1 = SoundCategory.getCategory(category.lowercase())
        setVolume(Client.getMinecraft().gameSettings.getSoundLevel(category1))
        //#else
        //$$ val category1 = SoundSource.values().first { it.name == category.lowercase() }
        //$$ source.asMixin<AbstractSoundInstanceAccessor>().setSource(category1)
        //#endif
    }

    fun getVolume(): Float {
        //#if MC<=11202
        return sndSystem!!.getVolume(source)
        //#else
        //$$ return source.sound.volume
        //#endif
    }

    /**
     * Sets this sound's volume.
     * Will override the category if called after [setCategory], but not if called before.
     *
     * @param volume New volume, float value ( 0.0f - 1.0f ).
     */
    fun setVolume(volume: Float) = apply {
        //#if MC<=11202
        sndSystem!!.setVolume(source, volume)
        //#else
        //$$ source.asMixin<AbstractSoundInstanceAccessor>().setVolume(volume)
        //#endif
    }

    /**
     * Updates the position of this sound
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    fun setPosition(x: Float, y: Float, z: Float) = apply {
        //#if MC<=11202
        sndSystem!!.setPosition(source, x, y, z)
        //#else
        //$$ source.asMixin<AbstractSoundInstanceAccessor>().setX(x.toDouble())
        //$$ source.asMixin<AbstractSoundInstanceAccessor>().setY(y.toDouble())
        //$$ source.asMixin<AbstractSoundInstanceAccessor>().setZ(z.toDouble())
        //#endif
    }

    fun getPitch(): Float {
        //#if MC<=11202
        return sndSystem!!.getPitch(source)
        //#else
        //$$ return source.pitch
        //#endif
    }

    /**
     * Sets this sound's pitch.
     *
     * @param pitch A float value ( 0.5f - 2.0f ).
     */
    fun setPitch(pitch: Float) = apply {
        //#if MC<=11202
        sndSystem!!.setPitch(source, pitch)
        //#else
        //$$ source.asMixin<AbstractSoundInstanceAccessor>().setPitch(pitch)
        //#endif
    }

    /**
     * Sets the attenuation (fade out over space) of the song.
     * True uses linear attenuation, false uses no attenutation
     *
     * @param model the model
     */
    fun setAttenuation(attenuation: Boolean) = apply {
        //#if MC<=11202
        sndSystem!!.setAttenuation(source, if (attenuation) 0 else 2)
        //#else
        //$$ val a = if (attenuation) {
        //$$     SoundInstance.Attenuation.LINEAR
        //$$ } else SoundInstance.Attenuation.NONE
        //$$ source.asMixin<AbstractSoundInstanceAccessor>().setAttenuation(a)
        //#endif
    }

    /**
     * Plays/resumes the sound
     */
    fun play() {
        sndSystem!!.play(source)
    }

    // TODO(BREAKING): Removed this (doesn't exist outside of paulscode)
    // /**
    //  * Pauses the sound, to be resumed later
    //  */
    // fun pause() {
    //     sndSystem!!.pause(source)
    // }

    /**
     * Completely stops the sound
     */
    fun stop() {
        sndSystem!!.stop(source)
    }

    // TODO(BREAKING): Removed this (doesn't exist outside of paulscode)
    // /**
    //  * Immediately restarts the sound
    //  */
    // fun rewind() {
    //     sndSystem!!.rewind(source)
    // }

    companion object {
        //#if MC<=11202
        private var sndSystem: SoundSystem? = null
        //#elseif MC>=11701
        //$$ private var sndSystem: SoundEngine? = null
        //#endif

        private fun loadSndSystem() {
            if (sndSystem != null)
                return

            //#if MC<=11202
            val sndManager = Client.getMinecraft().soundHandler.asMixin<SoundHandlerAccessor>().sndManager
            //#else
            //$$ val sndManager = Client.getMinecraft().soundManager
            //#endif

            // TODO(VERIFY): Does this work in dev? It doesn't take the unobfuscated name

            //#if MC<=11202
            sndSystem = ReflectionHelper.getPrivateValue(
                SoundManager::class.java,
                sndManager,
                "sndSystem",
                "field_148620_e"
            )
            //#elseif MC>=11701
            //$$ sndSystem = sndManager.asMixin<SoundManagerAccessor>().soundEngine
            //#endif
        }
    }
}
