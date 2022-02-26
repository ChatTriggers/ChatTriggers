package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCSoundCategory
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.world.EnumDifficulty

@External
class Settings {
    fun getSettings() = Client.getMinecraft().gameSettings

    fun getFOV() = getSettings().fovSetting

    fun setFOV(fov: Float) {
        getSettings().fovSetting = fov
    }

    fun getDifficulty() = getSettings().difficulty.difficultyId

    fun setDifficulty(difficulty: Int) {
        getSettings().difficulty = EnumDifficulty.getDifficultyEnum(difficulty)
    }

    val skin = object {
        fun getCape() = getSettings().modelParts.contains(EnumPlayerModelParts.CAPE)

        fun setCape(toggled: Boolean) {
            getSettings().setModelPartEnabled(EnumPlayerModelParts.CAPE, toggled)
        }

        fun getJacket() = getSettings().modelParts.contains(EnumPlayerModelParts.JACKET)

        fun setJacket(toggled: Boolean) {
            getSettings().setModelPartEnabled(EnumPlayerModelParts.JACKET, toggled)
        }

        fun getLeftSleeve() = getSettings().modelParts.contains(EnumPlayerModelParts.LEFT_SLEEVE)

        fun setLeftSleeve(toggled: Boolean) {
            getSettings().setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, toggled)
        }

        fun getRightSleeve() = getSettings().modelParts.contains(EnumPlayerModelParts.RIGHT_SLEEVE)

        fun setRightSleeve(toggled: Boolean) {
            getSettings().setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, toggled)
        }

        fun getLeftPantsLeg() = getSettings().modelParts.contains(EnumPlayerModelParts.LEFT_PANTS_LEG)

        fun setLeftPantsLeg(toggled: Boolean) {
            getSettings().setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, toggled)
        }

        fun getRightPantsLeg() = getSettings().modelParts.contains(EnumPlayerModelParts.RIGHT_PANTS_LEG)

        fun setRightPantsLeg(toggled: Boolean) {
            getSettings().setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, toggled)
        }

        fun getHat() = getSettings().modelParts.contains(EnumPlayerModelParts.HAT)

        fun setHat(toggled: Boolean) {
            getSettings().setModelPartEnabled(EnumPlayerModelParts.HAT, toggled)
        }
    }

    val sound = object {
        fun getMasterVolume() = getSettings().getSoundLevel(MCSoundCategory.MASTER)

        fun setMasterVolume(level: Float) = getSettings().setSoundLevel(MCSoundCategory.MASTER, level)

        fun getMusicVolume() = getSettings().getSoundLevel(MCSoundCategory.MUSIC)

        fun setMusicVolume(level: Float) = getSettings().setSoundLevel(MCSoundCategory.MUSIC, level)

        fun getNoteblockVolume() = getSettings().getSoundLevel(MCSoundCategory.RECORDS)

        fun setNoteblockVolume(level: Float) = getSettings().setSoundLevel(MCSoundCategory.RECORDS, level)

        fun getWeather() = getSettings().getSoundLevel(MCSoundCategory.WEATHER)

        fun setWeather(level: Float) = getSettings().setSoundLevel(MCSoundCategory.WEATHER, level)

        fun getBlocks() = getSettings().getSoundLevel(MCSoundCategory.BLOCKS)

        fun setBlocks(level: Float) = getSettings().setSoundLevel(MCSoundCategory.BLOCKS, level)

        //#if MC<=10809
        fun getHostileCreatures() = getSettings().getSoundLevel(MCSoundCategory.MOBS)

        fun setHostileCreatures(level: Float) = getSettings().setSoundLevel(MCSoundCategory.MOBS, level)

        fun getFriendlyCreatures() = getSettings().getSoundLevel(MCSoundCategory.ANIMALS)

        fun setFriendlyCreatures(level: Float) = getSettings().setSoundLevel(MCSoundCategory.ANIMALS, level)
        //#else
        //$$ fun getHostileCreatures() = getSettings().getSoundLevel(MCSoundCategory.HOSTILE)
        //$$ fun setHostileCreatures(level: Float) = getSettings().setSoundLevel(MCSoundCategory.HOSTILE, level)
        //$$
        //$$ fun getFriendlyCreatures() = getSettings().getSoundLevel(MCSoundCategory.NEUTRAL)
        //$$ fun setFriendlyCreatures(level: Float) = getSettings().setSoundLevel(MCSoundCategory.NEUTRAL, level)
        //#endif

        fun getPlayers() = getSettings().getSoundLevel(MCSoundCategory.PLAYERS)

        fun setPlayers(level: Float) = getSettings().setSoundLevel(MCSoundCategory.PLAYERS, level)

        fun getAmbient() = getSettings().getSoundLevel(MCSoundCategory.AMBIENT)

        fun setAmbient(level: Float) = getSettings().setSoundLevel(MCSoundCategory.AMBIENT, level)
    }

    val video = object {
        fun getGraphics() = getSettings().fancyGraphics

        fun setGraphics(fancy: Boolean) {
            getSettings().fancyGraphics = fancy
        }

        fun getRenderDistance() = getSettings().renderDistanceChunks

        fun setRenderDistance(distance: Int) {
            getSettings().renderDistanceChunks = distance
        }

        fun getSmoothLighting() = getSettings().ambientOcclusion

        fun setSmoothLighting(level: Int) {
            getSettings().ambientOcclusion = level
        }

        fun getMaxFrameRate() = getSettings().limitFramerate

        fun setMaxFrameRate(frameRate: Int) {
            getSettings().limitFramerate = frameRate
        }

        fun get3dAnaglyph() = getSettings().anaglyph

        fun set3dAnaglyph(toggled: Boolean) {
            getSettings().anaglyph = toggled
        }

        fun getBobbing() = getSettings().viewBobbing

        fun setBobbing(toggled: Boolean) {
            getSettings().viewBobbing = toggled
        }

        fun getGuiScale() = getSettings().guiScale

        fun setGuiScale(scale: Int) {
            getSettings().guiScale = scale
        }

        fun getBrightness() = getSettings().gammaSetting

        fun setBrightness(brightness: Float) {
            getSettings().gammaSetting = brightness
        }

        fun getClouds() = getSettings().clouds

        fun setClouds(clouds: Int) {
            getSettings().clouds = clouds
        }

        fun getParticles() = getSettings().particleSetting

        fun setParticles(particles: Int) {
            getSettings().particleSetting = particles
        }

        fun getFullscreen() = getSettings().fullScreen

        fun setFullscreen(toggled: Boolean) {
            getSettings().fullScreen = toggled
        }

        fun getVsync() = getSettings().enableVsync

        fun setVsync(toggled: Boolean) {
            getSettings().enableVsync = toggled
        }

        fun getMipmapLevels() = getSettings().mipmapLevels

        fun setMipmapLevels(mipmapLevels: Int) {
            getSettings().mipmapLevels = mipmapLevels
        }

        //#if MC<=10809
        fun getAlternateBlocks() = getSettings().allowBlockAlternatives

        fun setAlternateBlocks(toggled: Boolean) {
            getSettings().allowBlockAlternatives = toggled
        }
        //#else
        //$$ fun getAlternateBlocks() = UnsupportedOperationException("1.12 has no Alternative Blocks settings")
        //$$ fun setAlternateBlocks(toggled: Boolean) = UnsupportedOperationException("1.12 has no Alternative Blocks settings")
        //#endif

        fun getVBOs() = getSettings().useVbo

        fun setVBOs(toggled: Boolean) {
            getSettings().useVbo = toggled
        }

        fun getEntityShadows() = getSettings().entityShadows

        fun setEntityShadows(toggled: Boolean) {
            getSettings().entityShadows = toggled
        }
    }

    val chat = object {
        fun getVisibility() = getSettings().chatVisibility

        fun setVisibility(visibility: String) {
            getSettings().chatVisibility = when (visibility.lowercase()) {
                "hidden" -> EntityPlayer.EnumChatVisibility.HIDDEN
                "commands", "system" -> EntityPlayer.EnumChatVisibility.SYSTEM
                else -> EntityPlayer.EnumChatVisibility.FULL
            }
        }

        fun getColors() = getSettings().chatColours

        fun setColors(toggled: Boolean) {
            getSettings().chatColours = toggled
        }

        fun getWebLinks() = getSettings().chatLinks

        fun setWebLinks(toggled: Boolean) {
            getSettings().chatLinks = toggled
        }

        fun getOpacity() = getSettings().chatOpacity

        fun setOpacity(opacity: Float) {
            getSettings().chatOpacity = opacity
        }

        fun getPromptOnWebLinks() = getSettings().chatLinksPrompt

        fun setPromptOnWebLinks(toggled: Boolean) {
            getSettings().chatLinksPrompt = toggled
        }

        fun getScale() = getSettings().chatScale

        fun setScale(scale: Float) {
            getSettings().chatScale = scale
        }

        fun getFocusedHeight() = getSettings().chatHeightFocused

        fun setFocusedHeight(height: Float) {
            getSettings().chatHeightFocused = height
        }

        fun getUnfocusedHeight() = getSettings().chatHeightUnfocused

        fun setUnfocusedHeight(height: Float) {
            getSettings().chatHeightUnfocused = height
        }

        fun getWidth() = getSettings().chatWidth

        fun setWidth(width: Float) {
            getSettings().chatWidth = width
        }

        fun getReducedDebugInfo() = getSettings().reducedDebugInfo

        fun setReducedDebugInfo(toggled: Boolean) {
            getSettings().reducedDebugInfo = toggled
        }
    }
}
