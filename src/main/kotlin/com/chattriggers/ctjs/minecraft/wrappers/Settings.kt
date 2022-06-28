package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.i18Format
import net.minecraft.client.audio.SoundCategory
import net.minecraft.client.settings.GameSettings
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.world.EnumDifficulty

//#if MC>=11701
//$$ import net.minecraft.client.AmbientOcclusionStatus
//$$ import net.minecraft.client.CloudStatus
//$$ import net.minecraft.client.GraphicsStatus
//$$ import net.minecraft.client.ParticleStatus
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
        private fun getCategoryVolume(category: SoundCategory): Float {
            //#if MC<=11202
            return getSettings().getSoundLevel(category)
            //#else
            //$$ return getSettings().getSoundSourceVolume(category)
            //#endif
        }

        private fun setCategoryVolume(category: SoundCategory, volume: Float) {
            //#if MC<=11202
            getSettings().setSoundLevel(category, volume)
            //#else
            //$$ getSettings().setSoundCategoryVolume(category, volume)
            //#endif
        }

        fun getMasterVolume() = getCategoryVolume(SoundCategory.MASTER)

        fun setMasterVolume(level: Float) = setCategoryVolume(SoundCategory.MASTER, level)

        fun getMusicVolume() = getCategoryVolume(SoundCategory.MUSIC)

        fun setMusicVolume(level: Float) = setCategoryVolume(SoundCategory.MUSIC, level)

        fun getNoteblockVolume() = getCategoryVolume(SoundCategory.RECORDS)

        fun setNoteblockVolume(level: Float) = setCategoryVolume(SoundCategory.RECORDS, level)

        fun getWeather() = getCategoryVolume(SoundCategory.WEATHER)

        fun setWeather(level: Float) = setCategoryVolume(SoundCategory.WEATHER, level)

        fun getBlocks() = getCategoryVolume(SoundCategory.BLOCKS)

        fun setBlocks(level: Float) = setCategoryVolume(SoundCategory.BLOCKS, level)
        //#if MC<=11202
        fun getHostileCreatures() = getCategoryVolume(SoundCategory.MOBS)

        fun setHostileCreatures(level: Float) = setCategoryVolume(SoundCategory.MOBS, level)

        fun getFriendlyCreatures() = getCategoryVolume(SoundCategory.ANIMALS)

        fun setFriendlyCreatures(level: Float) = setCategoryVolume(SoundCategory.ANIMALS, level)

        //#elseif MC>=11701
        //$$ fun getHostileCreatures() = getCategoryVolume(SoundSource.HOSTILE)
        //$$
        //$$ fun setHostileCreatures(level: Float) = setCategoryVolume(SoundSource.HOSTILE, level)
        //$$
        //$$ fun getFriendlyCreatures() = getCategoryVolume(SoundSource.NEUTRAL)
        //$$
        //$$ fun setFriendlyCreatures(level: Float) = setCategoryVolume(SoundSource.NEUTRAL, level)
        //#endif

        fun setPlayers(level: Float) = setCategoryVolume(SoundCategory.PLAYERS, level)

        fun getAmbient() = getCategoryVolume(SoundCategory.AMBIENT)

        fun setAmbient(level: Float) = setCategoryVolume(SoundCategory.AMBIENT, level)
    }

    // TODO(BREAKING): Make enums for most of these methods
    val video = object {
        //#if MC<=11202
        fun getGraphics() = if (getSettings().fancyGraphics) {
            Graphics.Fancy
        } else {
            Graphics.Fast
        }

        fun setGraphics(mode: Graphics) {
            getSettings().fancyGraphics = mode.id != 0
        }

        fun getRenderDistance() = getSettings().renderDistanceChunks

        fun setRenderDistance(distance: Int) {
            getSettings().renderDistanceChunks = distance
        }

        fun getSmoothLighting() = SmoothLighting.fromId(getSettings().ambientOcclusion)

        fun setSmoothLighting(level: SmoothLighting) {
            getSettings().ambientOcclusion = level.id
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

        fun getClouds() = Clouds.fromId(getSettings().clouds)

        fun setClouds(clouds: Clouds) {
            getSettings().clouds = clouds.id
        }

        fun getParticles() = Particles.fromId(getSettings().particleSetting)

        fun setParticles(particles: Particles) {
            getSettings().particleSetting = particles.id
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
        //$$ fun getGraphics() = Graphics.fromId(getSettings().graphicsMode.id)
        //$$
        //$$ fun setGraphics(mode: Graphics) {
        //$$     getSettings().graphicsMode = GraphicsStatus.byId(mode.id)
        //$$ }
        //$$
        //$$ fun getRenderDistance() = getSettings().renderDistance
        //$$
        //$$ fun setRenderDistance(distance: Int) {
        //$$     getSettings().renderDistance = distance
        //$$ }
        //$$
        //$$ fun getSmoothLighting() = SmoothLighting.fromId(getSettings().ambientOcclusion.id)
        //$$
        //$$ fun setSmoothLighting(level: SmoothLighting) {
        //$$     getSettings().ambientOcclusion = AmbientOcclusionStatus.byId(level.id)
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
        //$$ fun getClouds() = Clouds.fromId(getSettings().renderClouds.ordinal)
        //$$
        //$$ fun setClouds(clouds: Clouds) {
        //$$     getSettings().renderClouds = CloudStatus.values()[clouds.id]
        //$$ }
        //$$
        //$$ fun getParticles() = Particles.fromId(getSettings().particles.id)
        //$$
        //$$ fun setParticles(particles: Particles) {
        //$$     getSettings().particles = ParticleStatus.byId(particles.id)
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
        fun getVisibility() = when (getSettings().chatVisibility) {
            //#if MC<=11202
            EntityPlayer.EnumChatVisibility.HIDDEN -> Chat.Hidden
            EntityPlayer.EnumChatVisibility.SYSTEM -> Chat.System
            EntityPlayer.EnumChatVisibility.FULL -> Chat.All
            //#elseif MC>=11701
            //$$ ChatVisibility.HIDDEN -> Chat.Hidden
            //$$ ChatVisibility.SYSTEM -> Chat.System
            //$$ ChatVisibility.FULL -> Chat.All
            //#endif
            null -> Chat.All
        }

        fun setVisibility(visibility: Chat) {
            getSettings().chatVisibility = when (visibility) {
                //#if MC<=11202
                Chat.Hidden -> EntityPlayer.EnumChatVisibility.HIDDEN
                Chat.System -> EntityPlayer.EnumChatVisibility.SYSTEM
                Chat.All -> EntityPlayer.EnumChatVisibility.FULL
                //#elseif MC>=11701
                //$$ Chat.Hidden -> ChatVisibility.HIDDEN
                //$$ Chat.System -> ChatVisibility.SYSTEM
                //$$ Chat.All -> ChatVisibility.FULL
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
            getSettings().chatOpacity = opacity
                //#if MC<=11202
                .toFloat()
                //#endif
        }

        fun getPromptOnWebLinks() = getSettings().chatLinksPrompt

        fun setPromptOnWebLinks(toggled: Boolean) {
            getSettings().chatLinksPrompt = toggled
        }

        fun getScale() = getSettings().chatScale.toDouble()

        fun setScale(scale: Double) {
            getSettings().chatScale = scale
                //#if MC<=11202
                .toFloat()
                //#endif
        }

        fun getFocusedHeight() = getSettings().chatHeightFocused.toDouble()

        fun setFocusedHeight(height: Double) {
            getSettings().chatHeightFocused = height
                //#if MC<=11202
                .toFloat()
                //#endif
        }

        fun getUnfocusedHeight() = getSettings().chatHeightUnfocused.toDouble()

        fun setUnfocusedHeight(height: Double) {
            getSettings().chatHeightUnfocused = height
                //#if MC<=11202
                .toFloat()
                //#endif
        }

        fun getWidth() = getSettings().chatWidth.toDouble()

        fun setWidth(width: Double) {
            getSettings().chatWidth = width
                //#if MC<=11202
                .toFloat()
                //#endif
        }

        fun getReducedDebugInfo() = getSettings().reducedDebugInfo

        fun setReducedDebugInfo(toggled: Boolean) {
            getSettings().reducedDebugInfo = toggled
        }
    }

    enum class Graphics(val id: Int) {
        Fast(0),
        Fancy(1),
        //#if MC>=11701
        //$$ Fabulous(2)
        //#endif
        ;

        companion object {
            fun fromId(id: Int) = values().find { it.id == id } ?: Fancy
        }
    }

    enum class SmoothLighting(val id: Int) {
        Off(0),
        Min(1),
        Max(2);

        companion object {
            fun fromId(id: Int) = values().find { it.id == id } ?: Max
        }
    }

    enum class Clouds(val id: Int) {
        Off(0),
        Fast(1),
        Fancy(2);

        companion object {
            fun fromId(id: Int) = values().find { it.id == id } ?: Fancy
        }
    }

    enum class Particles(val id: Int) {
        All(0),
        Decreased(1),
        Minimal(2);

        companion object {
            fun fromId(id: Int) = values().find { it.id == id } ?: All
        }
    }

    enum class Chat {
        Hidden,
        System,
        All
    }
}
