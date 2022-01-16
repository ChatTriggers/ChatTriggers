package com.chattriggers.ctjs.minecraft.objects

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.SoundManagerAccessor
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.sound.Sound
import net.minecraft.client.sound.SoundInstance
import net.minecraft.client.sound.SoundManager
import net.minecraft.client.sound.SoundSystem
import net.minecraft.client.sound.WeightedSoundSet
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Identifier
import org.mozilla.javascript.NativeObject
import java.io.File
import java.net.MalformedURLException

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
 * - attenuation (int) - fade out model of the sound, see [setAttenuation]
 *
 * @param config the JavaScript config object
 */
// TODO("fabric"): Implement this class when the game can be debugged
@External
class Sound(private val config: NativeObject) {
    private var soundSystem: SoundSystem = Client.getMinecraft().soundManager.asMixin<SoundManagerAccessor>().soundSystem
    private val source: String = config["source"] as String
    private var soundInstance: SoundInstance? = null
    var isListening = false

    init {
        if (World.isLoaded()) {
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

        try {
            bootstrap()
        } catch (exc: MalformedURLException) {
            exc.printStackTrace()
        }
    }

    @Throws(MalformedURLException::class)
    private fun bootstrap() {
        // val source = config["source"]?.toString() ?: throw IllegalArgumentException("Sound source is null.")
        // val priority = config.getOrDefault("priority", false) as Boolean
        // val loop = config.getOrDefault("loop", false) as Boolean
        // val stream = config.getOrDefault("stream", false) as Boolean
        //
        // val url = File(CTJS.assetsDir, source).toURI().toURL()
        // val x = (config.getOrDefault("x", Player.getX()) as Double).toFloat()
        // val y = (config.getOrDefault("y", Player.getY()) as Double).toFloat()
        // val z = (config.getOrDefault("z", Player.getZ()) as Double).toFloat()
        // val attModel = config.getOrDefault("attenuation", 1) as Int
        // val distOrRoll = 16
        //
        // if (stream) {
        //     soundSystem.newStreamingSource(
        //         priority,
        //         source,
        //         url,
        //         source,
        //         loop,
        //         x,
        //         y,
        //         z,
        //         attModel,
        //         distOrRoll.toFloat()
        //     )
        // } else {
        //     soundSystem.newSource(
        //         priority,
        //         source,
        //         url,
        //         source,
        //         loop,
        //         x,
        //         y,
        //         z,
        //         attModel,
        //         distOrRoll.toFloat()
        //     )
        // }
        //
        // if (config["volume"] != null) {
        //     setVolume(config["volume"] as Float)
        // }
        //
        // if (config["pitch"] != null) {
        //     setPitch(config["pitch"] as Float)
        // }
        //
        // if (config["category"] != null) {
        //     setCategory(config["category"] as String)
        // }
    }

    /**
     * Sets the category of this sound, making it respect the Player's sound volume sliders.
     * Options are: master, music, record, weather, block, hostile, neutral, player, and ambient
     *
     * @param category the category
     */
    fun setCategory(category: String) = apply {
        // val category1 = MCSoundCategory.getCategory(category.lowercase())
        // setVolume(Client.getMinecraft().gameSettings.getSoundLevel(category1))
    }

    /**
     * Sets this sound's volume.
     * Will override the category if called after [setCategory], but not if called before.
     *
     * @param volume New volume, float value ( 0.0f - 1.0f ).
     */
    fun setVolume(volume: Float) = apply { /*soundSystem!!.setVolume(source, volume)*/ }

    fun getVolume(): Float = TODO("fabric") /*soundSystem!!.getVolume(source)*/

    /**
     * Updates the position of this sound
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    fun setPosition(x: Float, y: Float, z: Float) = apply { /*soundSystem!!.setPosition(source, x, y, z)*/ }

    /**
     * Sets this sound's pitch.
     *
     * @param pitch A float value ( 0.5f - 2.0f ).
     */
    fun setPitch(pitch: Float) = apply { /*soundSystem!!.setPitch(source, pitch)*/ }

    fun getPitch(): Float = TODO("fabric") /*soundSystem!!.getPitch(source)*/

    /**
     * Sets the attenuation (fade out over space) of the song.
     * Models are:
     *  NONE(0) - no fade
     *  ROLLOFF(1) - this is the default, meant to be somewhat realistic
     *  LINEAR(2) - fades out linearly, as the name implies
     *
     * @param model the model
     */
    fun setAttenuation(model: Int) = apply { /*soundSystem!!.setAttenuation(source, model)*/ }

    /**
     * Plays/resumes the sound
     */
    fun play() {
        /*soundSystem!!.play(source)*/
    }

    /**
     * Pauses the sound, to be resumed later
     */
    fun pause() {
        /*soundSystem!!.pause(source)*/
    }

    /**
     * Completely stops the song
     */
    fun stop() {
        /*soundSystem!!.stop(source)*/
    }

    /**
     * I really don't know what this does
     */
    fun rewind() {
        /*soundSystem!!.rewind(source)*/
    }
}
