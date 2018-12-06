package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.SoundCategory

@External
object Settings {
    @JvmStatic
    fun getSettings()= Client.getMinecraft().gameSettings

    @JvmStatic
    fun getFOV() = getSettings().fovSetting

    object SkinCustomization {

    }

    object Sound {
        @JvmStatic
        fun getMasterVolume() = getSettings().getSoundLevel(SoundCategory.MASTER)
        @JvmStatic
        fun setMasterVolume(level: Float) = getSettings().setSoundLevel(SoundCategory.MASTER, level)

        @JvmStatic
        fun getMusicVolume() = getSettings().getSoundLevel(SoundCategory.MUSIC)
        @JvmStatic
        fun setMusicVolume(level: Float) = getSettings().setSoundLevel(SoundCategory.MUSIC, level)

        @JvmStatic
        fun getNoteblockVolume() = getSettings().getSoundLevel(SoundCategory.RECORDS)
        @JvmStatic
        fun setNoteblockVolume(level: Float) = getSettings().setSoundLevel(SoundCategory.RECORDS, level)

        @JvmStatic
        fun getWeather() = getSettings().getSoundLevel(SoundCategory.WEATHER)
        @JvmStatic
        fun setWeather(level: Float) = getSettings().setSoundLevel(SoundCategory.WEATHER, level)

        @JvmStatic
        fun getBlocks() = getSettings().getSoundLevel(SoundCategory.BLOCKS)
        @JvmStatic
        fun setBlocks(level: Float) = getSettings().setSoundLevel(SoundCategory.BLOCKS, level)

        @JvmStatic
        fun getHostileCreatures() = getSettings().getSoundLevel(SoundCategory.MOBS)
        @JvmStatic
        fun setHostileCreatures(level: Float) = getSettings().setSoundLevel(SoundCategory.MOBS, level)

        @JvmStatic
        fun getFriendlyCreatures() = getSettings().getSoundLevel(SoundCategory.ANIMALS)
        @JvmStatic
        fun setFriendlyCreatures(level: Float) = getSettings().setSoundLevel(SoundCategory.ANIMALS, level)

        @JvmStatic
        fun getPlayers() = getSettings().getSoundLevel(SoundCategory.PLAYERS)
        @JvmStatic
        fun setPlayers(level: Float) = getSettings().setSoundLevel(SoundCategory.PLAYERS, level)

        @JvmStatic
        fun getAmbient() = getSettings().getSoundLevel(SoundCategory.AMBIENT)
        @JvmStatic
        fun setAmbient(level: Float) = getSettings().setSoundLevel(SoundCategory.AMBIENT, level)
    }

    object Video {
        @JvmStatic
        fun getGraphics() = getSettings().fancyGraphics
        @JvmStatic
        fun setGraphics(fancy: Boolean) { getSettings().fancyGraphics = fancy }

        @JvmStatic
        fun getRenderDistance() = getSettings().renderDistanceChunks
        @JvmStatic
        fun seRenderDistance(distance: Int) { getSettings().renderDistanceChunks = distance }
    }
}