package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.MCSoundCategory
import net.minecraft.client.settings.GameSettings
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.world.EnumDifficulty

//#if MC>=11701
//$$ import net.minecraft.client.*
//$$ import net.minecraft.world.entity.player.ChatVisiblity
//#endif

class Settings {
    fun getSettings(): GameSettings {
        //#if MC<=11202
        return Client.getMinecraft().gameSettings
        //#else
        //$$ return Client.getMinecraft().options
        //#endif
    }

    fun getFOV(): Double {
        //#if MC<=11202
        return getSettings().fovSetting.toDouble()
        //#else
        //$$ return getSettings().fov
        //#endif
    }

    fun setFOV(fov: Double) {
        //#if MC<=11202
        getSettings().fovSetting = fov.toFloat()
        //#else
        //$$ getSettings().fov = fov
        //#endif
    }

    fun getDifficulty(): Int {
        //#if MC<=11202
        return getSettings().difficulty.difficultyId
        //#else
        //$$ return getSettings().difficulty.id
        //#endif
    }

    fun setDifficulty(difficulty: Int) {
        //#if MC<=11202
        getSettings().difficulty = EnumDifficulty.getDifficultyEnum(difficulty)
        //#else
        //$$ getSettings().difficulty = Difficulty.byId(difficulty)
        //#endif
    }

    // TODO(BREAKING): Changed these method names (like getCape -> isCapeEnabled)
    val skin = object {
        private fun isPartEnabled(part: EnumPlayerModelParts): Boolean {
            //#if MC<=11202
            return getSettings().modelParts.contains(part)
            //#else
            //$$ return getSettings().isModelPartEnabled(part)
            //#endif
        }

        private fun setPartEnabled(part: EnumPlayerModelParts, enabled: Boolean) {
            //#if MC<=11202
            getSettings().setModelPartEnabled(part, enabled)
            //#else
            //$$ getSettings().toggleModelPart(part, enabled)
            //#endif
        }

        fun isCapeEnabled() = isPartEnabled(EnumPlayerModelParts.CAPE)

        fun setCapeEnabled(toggled: Boolean) = setPartEnabled(EnumPlayerModelParts.CAPE, toggled)

        fun isJacketEnabled() = isPartEnabled(EnumPlayerModelParts.JACKET)

        fun setJacketEnabled(toggled: Boolean) = setPartEnabled(EnumPlayerModelParts.JACKET, toggled)

        fun isLeftSleeveEnabled() = isPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE)

        fun setLeftSleeveEnabled(toggled: Boolean) = setPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, toggled)

        fun isRightSleeveEnabled() = isPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE)

        fun setRightSleeveEnabled(toggled: Boolean) = setPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, toggled)

        fun isLeftPantsLegEnabled() = isPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG)

        fun setLeftPantsLegEnabled(toggled: Boolean) = setPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, toggled)

        fun isRightPantsLegEnabled() = isPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG)

        fun setRightPantsLegEnabled(toggled: Boolean) = setPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, toggled)

        fun isHatEnabled() = isPartEnabled(EnumPlayerModelParts.HAT)

        fun setHatEnabled(toggled: Boolean) = setPartEnabled(EnumPlayerModelParts.HAT, toggled)
    }

    val sound = object {
        private fun getCategoryVolume(category: MCSoundCategory): Float {
            //#if MC<=11202
            return getSettings().getSoundLevel(category)
            //#else
            //$$ return getSettings().getSoundSourceVolume(category)
            //#endif
        }

        private fun setCategoryVolume(category: MCSoundCategory, volume: Float) {
            //#if MC<=11202
            getSettings().setSoundLevel(category, volume)
            //#else
            //$$ getSettings().setSoundCategoryVolume(category, volume)
            //#endif
        }

        fun getMasterVolume() = getCategoryVolume(MCSoundCategory.MASTER)

        fun setMasterVolume(level: Float) = setCategoryVolume(MCSoundCategory.MASTER, level)

        fun getMusicVolume() = getCategoryVolume(MCSoundCategory.MUSIC)

        fun setMusicVolume(level: Float) = setCategoryVolume(MCSoundCategory.MUSIC, level)

        fun getNoteblockVolume() = getCategoryVolume(MCSoundCategory.RECORDS)

        fun setNoteblockVolume(level: Float) = setCategoryVolume(MCSoundCategory.RECORDS, level)

        fun getWeather() = getCategoryVolume(MCSoundCategory.WEATHER)

        fun setWeather(level: Float) = setCategoryVolume(MCSoundCategory.WEATHER, level)

        fun getBlocks() = getCategoryVolume(MCSoundCategory.BLOCKS)

        fun setBlocks(level: Float) = setCategoryVolume(MCSoundCategory.BLOCKS, level)

        //#if MC<=10809
        fun getHostileCreatures() = getCategoryVolume(MCSoundCategory.MOBS)

        fun setHostileCreatures(level: Float) = setCategoryVolume(MCSoundCategory.MOBS, level)

        fun getFriendlyCreatures() = getCategoryVolume(MCSoundCategory.ANIMALS)

        fun setFriendlyCreatures(level: Float) = setCategoryVolume(MCSoundCategory.ANIMALS, level)
        //#else
        //$$ fun getHostileCreatures() = getCategoryVolume(MCSoundCategory.HOSTILE)
        //$$ fun setHostileCreatures(level: Float) = setCategoryVolume(MCSoundCategory.HOSTILE, level)
        //$$
        //$$ fun getFriendlyCreatures() = getCategoryVolume(MCSoundCategory.NEUTRAL)
        //$$ fun setFriendlyCreatures(level: Float) = setCategoryVolume(MCSoundCategory.NEUTRAL, level)
        //#endif

        fun getPlayers() = getCategoryVolume(MCSoundCategory.PLAYERS)

        fun setPlayers(level: Float) = setCategoryVolume(MCSoundCategory.PLAYERS, level)

        fun getAmbient() = getCategoryVolume(MCSoundCategory.AMBIENT)

        fun setAmbient(level: Float) = setCategoryVolume(MCSoundCategory.AMBIENT, level)
    }

    // TODO(CONVERT):
    // TODO(BREAKING): Make enums for most of these methods
    val video = object {
        //#if MC<=11202
        // TODO(BREAKING): Change return to int (and below methods argument type)
        fun getGraphics() = if (getSettings().fancyGraphics) 1 else 0

        fun setGraphics(mode: Int) {
            getSettings().fancyGraphics = mode != 0
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

        // TODO(BREAKING): Removed these
        // fun get3dAnaglyph() = getSettings().anaglyph
        //
        // fun set3dAnaglyph(toggled: Boolean) {
        //     getSettings().anaglyph = toggled
        // }

        fun getBobbing() = getSettings().viewBobbing

        fun setBobbing(toggled: Boolean) {
            getSettings().viewBobbing = toggled
        }

        fun getGuiScale() = getSettings().guiScale

        fun setGuiScale(scale: Int) {
            getSettings().guiScale = scale
        }

        fun getBrightness() = getSettings().gammaSetting.toDouble()

        fun setBrightness(brightness: Double) {
            getSettings().gammaSetting = brightness.toFloat()
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

        fun getEntityShadows() = getSettings().entityShadows

        fun setEntityShadows(toggled: Boolean) {
            getSettings().entityShadows = toggled
        }
        //#else
        //$$ fun getGraphics() = getSettings().graphicsMode.id
        //$$
        //$$ fun setGraphics(mode: Int) {
        //$$     getSettings().graphicsMode = GraphicsStatus.byId(mode)
        //$$ }
        //$$
        //$$ fun getRenderDistance() = getSettings().renderDistance
        //$$
        //$$ fun setRenderDistance(distance: Int) {
        //$$     getSettings().renderDistance = distance
        //$$ }
        //$$
        //$$ fun getSmoothLighting() = getSettings().ambientOcclusion.id
        //$$
        //$$ fun setSmoothLighting(level: Int) {
        //$$     getSettings().ambientOcclusion = AmbientOcclusionStatus.byId(level)
        //$$ }
        //$$
        //$$ fun getMaxFrameRate() = getSettings().framerateLimit
        //$$
        //$$ fun setMaxFrameRate(frameRate: Int) {
        //$$     getSettings().framerateLimit = frameRate
        //$$ }
        //$$
        //$$ fun getBobbing() = getSettings().bobView
        //$$
        //$$ fun setBobbing(toggled: Boolean) {
        //$$     getSettings().bobView = toggled
        //$$ }
        //$$
        //$$ fun getGuiScale() = getSettings().guiScale
        //$$
        //$$ fun setGuiScale(scale: Int) {
        //$$     getSettings().guiScale = scale
        //$$ }
        //$$
        //$$ fun getBrightness() = getSettings().gamma
        //$$
        //$$ fun setBrightness(brightness: Double) {
        //$$     getSettings().gamma = brightness
        //$$ }
        //$$
        //$$ fun getClouds() = getSettings().renderClouds.ordinal
        //$$
        //$$ fun setClouds(clouds: Int) {
        //$$     getSettings().renderClouds = CloudStatus.values()[clouds]
        //$$ }
        //$$
        //$$ fun getParticles() = getSettings().particles.id
        //$$
        //$$ fun setParticles(particles: Int) {
        //$$     getSettings().particles = ParticleStatus.byId(particles)
        //$$ }
        //$$
        //$$ fun getFullscreen() = getSettings().fullscreen
        //$$
        //$$ fun setFullscreen(toggled: Boolean) {
        //$$     getSettings().fullscreen = toggled
        //$$ }
        //$$
        //$$ fun getVsync() = getSettings().enableVsync
        //$$
        //$$ fun setVsync(toggled: Boolean) {
        //$$     getSettings().enableVsync = toggled
        //$$ }
        //$$
        //$$ fun getMipmapLevels() = getSettings().mipmapLevels
        //$$
        //$$ fun setMipmapLevels(mipmapLevels: Int) {
        //$$     getSettings().mipmapLevels = mipmapLevels
        //$$ }
        //$$
        //$$ fun getEntityShadows() = getSettings().entityShadows
        //$$
        //$$ fun setEntityShadows(toggled: Boolean) {
        //$$     getSettings().entityShadows = toggled
        //$$ }
        //#endif
    }

    val chat = object {
        fun getVisibility() = getSettings().chatVisibility

        fun setVisibility(visibility: String) {
            getSettings().chatVisibility = when (visibility.lowercase()) {
                //#if MC<=11202
                "hidden" -> EntityPlayer.EnumChatVisibility.HIDDEN
                "commands", "system" -> EntityPlayer.EnumChatVisibility.SYSTEM
                else -> EntityPlayer.EnumChatVisibility.FULL
                //#else
                //$$ "hidden" -> ChatVisiblity.HIDDEN
                //$$ "commands", "system" -> ChatVisiblity.SYSTEM
                //$$ else -> ChatVisiblity.FULL
                //#endif
            }
        }

        fun getColors(): Boolean {
            //#if MC<=11202
            return getSettings().chatColours
            //#else
            //$$ return getSettings().chatColors
            //#endif
        }

        fun setColors(toggled: Boolean) {
            //#if MC<=11202
            getSettings().chatColours = toggled
            //#else
            //$$ getSettings().chatColors = toggled
            //#endif
        }

        fun getWebLinks() = getSettings().chatLinks

        fun setWebLinks(toggled: Boolean) {
            getSettings().chatLinks = toggled
        }

        fun getOpacity() = getSettings().chatOpacity.toDouble()

        fun setOpacity(opacity: Double) {
            //#if MC<=11202
            getSettings().chatOpacity = opacity.toFloat()
            //#else
            //$$ getSettings().chatOpacity = opacity
            //#endif
        }

        fun getPromptOnWebLinks() = getSettings().chatLinksPrompt

        fun setPromptOnWebLinks(toggled: Boolean) {
            getSettings().chatLinksPrompt = toggled
        }

        fun getScale() = getSettings().chatScale.toDouble()

        fun setScale(scale: Double) {
            //#if MC<=11202
            getSettings().chatScale = scale.toFloat()
            //#else
            //$$ getSettings().chatScale = scale
            //#endif
        }

        fun getFocusedHeight() = getSettings().chatHeightFocused.toDouble()

        fun setFocusedHeight(height: Double) {
            //#if MC<=11202
            getSettings().chatHeightFocused = height.toFloat()
            //#else
            //$$ getSettings().chatHeightFocused = height
            //#endif
        }

        fun getUnfocusedHeight() = getSettings().chatHeightUnfocused.toDouble()

        fun setUnfocusedHeight(height: Double) {
            //#if MC<=11202
            getSettings().chatHeightUnfocused = height.toFloat()
            //#else
            //$$ getSettings().chatHeightUnfocused = height
            //#endif
        }

        fun getWidth() = getSettings().chatWidth.toDouble()

        fun setWidth(width: Double) {
            //#if MC<=11202
            getSettings().chatWidth = width.toFloat()
            //#else
            //$$ getSettings().chatWidth = width
            //#endif
        }

        fun getReducedDebugInfo() = getSettings().reducedDebugInfo

        fun setReducedDebugInfo(toggled: Boolean) {
            getSettings().reducedDebugInfo = toggled
        }
    }
}
