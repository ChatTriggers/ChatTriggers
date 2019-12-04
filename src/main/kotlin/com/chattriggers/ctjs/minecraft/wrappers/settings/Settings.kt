package com.chattriggers.ctjs.minecraft.wrappers.settings

import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.world.EnumDifficulty

/**
 * Used from [Client.settings] to get settings
 */
class Settings {
    val skin = SkinSettings()
    val sound = SoundSettings()
    val video = VideoSettings()
    val chat = ChatSettings()

    fun getSettings() = Client.getMinecraft().gameSettings

    fun getFOV() = getSettings().fovSetting

    fun setFOV(fov: Float) {
        getSettings().fovSetting = fov
    }

    fun getDifficulty() = getSettings().difficulty.difficultyId

    fun setDifficulty(difficulty: Int) {
        getSettings().difficulty = EnumDifficulty.getDifficultyEnum(difficulty)
    }
}