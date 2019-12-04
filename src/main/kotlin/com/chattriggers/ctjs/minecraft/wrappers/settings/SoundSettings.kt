package com.chattriggers.ctjs.minecraft.wrappers.settings

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.SoundCategory

/**
 * Used from [Settings.sound] to get sound settings
 */
class SoundSettings {
    private val settings = Client.settings.getSettings()

    fun getMasterVolume() = settings.getSoundLevel(SoundCategory.MASTER)
    fun setMasterVolume(level: Float) = settings.setSoundLevel(SoundCategory.MASTER, level)

    fun getMusicVolume() = settings.getSoundLevel(SoundCategory.MUSIC)
    fun setMusicVolume(level: Float) = settings.setSoundLevel(SoundCategory.MUSIC, level)

    fun getNoteblockVolume() = settings.getSoundLevel(SoundCategory.RECORDS)
    fun setNoteblockVolume(level: Float) = settings.setSoundLevel(SoundCategory.RECORDS, level)

    fun getWeather() = settings.getSoundLevel(SoundCategory.WEATHER)
    fun setWeather(level: Float) = settings.setSoundLevel(SoundCategory.WEATHER, level)

    fun getBlocks() = settings.getSoundLevel(SoundCategory.BLOCKS)
    fun setBlocks(level: Float) = settings.setSoundLevel(SoundCategory.BLOCKS, level)

    //#if MC<=10809
    fun getHostileCreatures() = settings.getSoundLevel(SoundCategory.MOBS)
    fun setHostileCreatures(level: Float) = settings.setSoundLevel(SoundCategory.MOBS, level)

    fun getFriendlyCreatures() = settings.getSoundLevel(SoundCategory.ANIMALS)
    fun setFriendlyCreatures(level: Float) = settings.setSoundLevel(SoundCategory.ANIMALS, level)
    //#else
    //$$ fun getHostileCreatures() = settings.getSoundLevel(SoundCategory.HOSTILE)
    //$$ fun setHostileCreatures(level: Float) = settings.setSoundLevel(SoundCategory.HOSTILE, level)
    //$$
    //$$ fun getFriendlyCreatures() = settings.getSoundLevel(SoundCategory.NEUTRAL)
    //$$ fun setFriendlyCreatures(level: Float) = settings.setSoundLevel(SoundCategory.NEUTRAL, level)
    //#endif

    fun getPlayers() = settings.getSoundLevel(SoundCategory.PLAYERS)
    fun setPlayers(level: Float) = settings.setSoundLevel(SoundCategory.PLAYERS, level)

    fun getAmbient() = settings.getSoundLevel(SoundCategory.AMBIENT)
    fun setAmbient(level: Float) = settings.setSoundLevel(SoundCategory.AMBIENT, level)
}