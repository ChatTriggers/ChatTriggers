package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.audio.SoundCategory
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.world.EnumDifficulty

//#if MC==11602
//$$ import net.minecraft.client.settings.GraphicsFanciness
//$$ import net.minecraft.client.settings.AmbientOcclusionStatus
//$$ import net.minecraft.client.settings.CloudOption
//$$ import net.minecraft.client.settings.ParticleStatus
//$$ internal typealias ChatVisibility = net.minecraft.entity.player.ChatVisibility
//#else
internal typealias ChatVisibility = net.minecraft.entity.player.EntityPlayer.EnumChatVisibility
//#endif

@External
object Settings {
    @JvmStatic
    fun getSettings() = Client.getMinecraft().gameSettings

    @JvmStatic
    fun getFOV(): Float {
        //#if MC==11602
        //$$ return getSettings().fov.toFloat()
        //#else
        return getSettings().fovSetting
        //#endif
    }

    @JvmStatic
    fun setFOV(fov: Float) {
        //#if MC==11602
        //$$ getSettings().fov = fov.toDouble()
        //#else
        getSettings().fovSetting = fov
        //#endif
    }

    @JvmStatic
    fun getDifficulty(): Int {
        //#if MC==11602
        //$$ return getSettings().difficulty.id
        //#else
        return getSettings().difficulty.difficultyId
        //#endif
    }

    @JvmStatic
    fun setDifficulty(difficulty: Int) {
        getSettings().difficulty = EnumDifficulty.getDifficultyEnum(difficulty)
    }

    object skin {
        @JvmStatic
        fun getCape() = getSettings().modelParts.contains(EnumPlayerModelParts.CAPE)

        @JvmStatic
        fun setCape(toggled: Boolean) {
            setModelPart(toggled, EnumPlayerModelParts.CAPE)
        }

        @JvmStatic
        fun getJacket() = getSettings().modelParts.contains(EnumPlayerModelParts.JACKET)

        @JvmStatic
        fun setJacket(toggled: Boolean) {
            setModelPart(toggled, EnumPlayerModelParts.JACKET)
        }

        @JvmStatic
        fun getLeftSleeve() = getSettings().modelParts.contains(EnumPlayerModelParts.LEFT_SLEEVE)

        @JvmStatic
        fun setLeftSleeve(toggled: Boolean) {
            setModelPart(toggled, EnumPlayerModelParts.LEFT_SLEEVE)
        }

        @JvmStatic
        fun getRightSleeve() = getSettings().modelParts.contains(EnumPlayerModelParts.RIGHT_SLEEVE)

        @JvmStatic
        fun setRightSleeve(toggled: Boolean) {
            setModelPart(toggled, EnumPlayerModelParts.RIGHT_SLEEVE)
        }

        @JvmStatic
        fun getLeftPantsLeg() = getSettings().modelParts.contains(EnumPlayerModelParts.LEFT_PANTS_LEG)

        @JvmStatic
        fun setLeftPantsLeg(toggled: Boolean) {
            setModelPart(toggled, EnumPlayerModelParts.LEFT_PANTS_LEG)
        }

        @JvmStatic
        fun getRightPantsLeg() = getSettings().modelParts.contains(EnumPlayerModelParts.RIGHT_PANTS_LEG)

        @JvmStatic
        fun setRightPantsLeg(toggled: Boolean) {
            setModelPart(toggled, EnumPlayerModelParts.RIGHT_PANTS_LEG)
        }

        @JvmStatic
        fun getHat() = getSettings().modelParts.contains(EnumPlayerModelParts.HAT)

        @JvmStatic
        fun setHat(toggled: Boolean) {
            setModelPart(toggled, EnumPlayerModelParts.HAT)
        }

        private fun setModelPart(toggled: Boolean, modelPart: EnumPlayerModelParts) {
            if (toggled) getSettings().modelParts.add(modelPart)
            else getSettings().modelParts.remove(modelPart)
        }
    }

    object sound {
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

        //#if MC==11602
        //$$ @JvmStatic fun getHostileCreatures() = getSettings().getSoundLevel(SoundCategory.HOSTILE)
        //$$ @JvmStatic fun setHostileCreatures(level: Float) = getSettings().setSoundLevel(SoundCategory.HOSTILE, level)
        //$$
        //$$ @JvmStatic fun getFriendlyCreatures() = getSettings().getSoundLevel(SoundCategory.NEUTRAL)
        //$$ @JvmStatic fun setFriendlyCreatures(level: Float) = getSettings().setSoundLevel(SoundCategory.NEUTRAL, level)
        //#else
        @JvmStatic
        fun getHostileCreatures() = getSettings().getSoundLevel(SoundCategory.MOBS)

        @JvmStatic
        fun setHostileCreatures(level: Float) = getSettings().setSoundLevel(SoundCategory.MOBS, level)

        @JvmStatic
        fun getFriendlyCreatures() = getSettings().getSoundLevel(SoundCategory.ANIMALS)

        @JvmStatic
        fun setFriendlyCreatures(level: Float) = getSettings().setSoundLevel(SoundCategory.ANIMALS, level)
        //#endif

        @JvmStatic
        fun getPlayers() = getSettings().getSoundLevel(SoundCategory.PLAYERS)

        @JvmStatic
        fun setPlayers(level: Float) = getSettings().setSoundLevel(SoundCategory.PLAYERS, level)

        @JvmStatic
        fun getAmbient() = getSettings().getSoundLevel(SoundCategory.AMBIENT)

        @JvmStatic
        fun setAmbient(level: Float) = getSettings().setSoundLevel(SoundCategory.AMBIENT, level)
    }

    enum class GraphicsLevel {
        FAST,
        FANCY,
        FABULOUS,
    }

    object video {
        @JvmStatic
        fun getGraphics(): GraphicsLevel {
            //#if MC==11602
            //$$ return when (getSettings().graphicFanciness) {
            //$$     GraphicsFanciness.FAST -> GraphicsLevel.FAST
            //$$     GraphicsFanciness.FANCY -> GraphicsLevel.FANCY
            //$$     GraphicsFanciness.FABULOUS -> GraphicsLevel.FABULOUS
            //$$     null -> GraphicsLevel.FAST
            //$$ }
            //#else
            return if (getSettings().fancyGraphics) {
                GraphicsLevel.FANCY
            } else GraphicsLevel.FAST
            //#endif
        }

        @JvmStatic
        fun setGraphics(level: GraphicsLevel) {
            //#if MC==11602
            //$$ getSettings().graphicFanciness = when (level) {
            //$$     GraphicsLevel.FAST -> GraphicsFanciness.FAST
            //$$     GraphicsLevel.FANCY -> GraphicsFanciness.FANCY
            //$$     GraphicsLevel.FABULOUS -> GraphicsFanciness.FABULOUS
            //$$ }
            //#else
            getSettings().fancyGraphics = level != GraphicsLevel.FAST
            //#endif
        }

        @JvmStatic
        fun getRenderDistance() = getSettings().renderDistanceChunks

        @JvmStatic
        fun setRenderDistance(distance: Int) {
            getSettings().renderDistanceChunks = distance
        }

        @JvmStatic
        fun getSmoothLighting(): Int {
            //#if MC==11602
            //$$ return getSettings().ambientOcclusionStatus.ordinal
            //#else
            return getSettings().ambientOcclusion
            //#endif
        }

        @JvmStatic
        fun setSmoothLighting(level: Int) {
            //#if MC==11602
            //$$ getSettings().ambientOcclusionStatus = AmbientOcclusionStatus.values()[level]
            //#else
            getSettings().ambientOcclusion = level
            //#endif
        }

        @JvmStatic
        fun getMaxFrameRate(): Int {
            //#if MC==11602
            //$$ return getSettings().framerateLimit
            //#else
            return getSettings().limitFramerate
            //#endif
        }

        @JvmStatic
        fun setMaxFrameRate(frameRate: Int) {
            //#if MC==11602
            //$$ getSettings().framerateLimit = frameRate
            //#else
            getSettings().limitFramerate = frameRate
            //#endif
        }

        // TODO(1.16.2): Remove?
        //#if MC==10809
        @JvmStatic
        fun get3dAnaglyph() = getSettings().anaglyph

        @JvmStatic
        fun set3dAnaglyph(toggled: Boolean) {
            getSettings().anaglyph = toggled
        }
        //#endif

        @JvmStatic
        fun getBobbing() = getSettings().viewBobbing

        @JvmStatic
        fun setBobbing(toggled: Boolean) {
            getSettings().viewBobbing = toggled
        }

        @JvmStatic
        fun getGuiScale() = getSettings().guiScale

        @JvmStatic
        fun setGuiScale(scale: Int) {
            getSettings().guiScale = scale
        }

        @JvmStatic
        fun getBrightness(): Double {
            //#if MC==11602
            //$$ return getSettings().gamma
            //#else
            return getSettings().gammaSetting.toDouble()
            //#endif
        }

        @JvmStatic
        fun setBrightness(brightness: Double) {
            //#if MC==11602
            //$$ getSettings().gamma = brightness
            //#else
            getSettings().gammaSetting = brightness.toFloat()
            //#endif
        }

        @JvmStatic
        fun getClouds(): Int {
            //#if MC==11602
            //$$ return getSettings().cloudOption.ordinal
            //#else
            return getSettings().clouds
            //#endif
        }

        @JvmStatic
        fun setClouds(clouds: Int) {
            //#if MC==11602
            //$$ getSettings().cloudOption = CloudOption.values()[clouds]
            //#else
            getSettings().clouds = clouds
            //#endif
        }

        @JvmStatic
        fun getParticles(): Int {
            //#if MC==11602
            //$$ return getSettings().particles.ordinal
            //#else
            return getSettings().particleSetting
            //#endif
        }

        @JvmStatic
        fun setParticles(particles: Int) {
            //#if MC==11602
            //$$ getSettings().particles = ParticleStatus.values()[particles]
            //#else
            getSettings().particleSetting = particles
            //#endif
        }

        @JvmStatic
        fun getFullscreen(): Boolean {
            //#if MC==11602
            //$$ return getSettings().fullscreen
            //#else
            return getSettings().fullScreen
            //#endif
        }

        @JvmStatic
        fun setFullscreen(toggled: Boolean) {
            //#if MC==11602
            //$$ getSettings().fullscreen = toggled
            //#else
            getSettings().fullScreen = toggled
            //#endif
        }

        @JvmStatic
        fun getVsync(): Boolean {
            //#if MC==11602
            //$$ return getSettings().vsync
            //#else
            return getSettings().enableVsync
            //#endif
        }

        @JvmStatic
        fun setVsync(toggled: Boolean) {
            //#if MC==11602
            //$$ getSettings().vsync = toggled
            //#else
            getSettings().enableVsync = toggled
            //#endif
        }

        @JvmStatic
        fun getMipmapLevels() = getSettings().mipmapLevels

        @JvmStatic
        fun setMipmapLevels(mipmapLevels: Int) {
            getSettings().mipmapLevels = mipmapLevels
        }

        //#if MC==11602
        //$$ @JvmStatic fun getAlternateBlocks() = UnsupportedOperationException("1.16 has no Alternative Blocks settings")
        //$$ @JvmStatic fun setAlternateBlocks(toggled: Boolean) = UnsupportedOperationException("1.16 has no Alternative Blocks settings")
        //#else
        @JvmStatic
        fun getAlternateBlocks() = getSettings().allowBlockAlternatives

        @JvmStatic
        fun setAlternateBlocks(toggled: Boolean) {
            getSettings().allowBlockAlternatives = toggled
        }
        //#endif

        // TODO(1.16.2)
        //#if MC==10809
        @JvmStatic
        fun getVBOs() = getSettings().useVbo

        @JvmStatic
        fun setVBOs(toggled: Boolean) {
            getSettings().useVbo = toggled
        }
        //#endif

        @JvmStatic
        fun getEntityShadows() = getSettings().entityShadows

        @JvmStatic
        fun setEntityShadows(toggled: Boolean) {
            getSettings().entityShadows = toggled
        }
    }

    object chat {
        // show chat
        @JvmStatic
        fun getVisibility() = getSettings().chatVisibility

        @JvmStatic
        fun setVisibility(visibility: String) {
            when (visibility.lowercase()) {
                "hidden" -> getSettings().chatVisibility = ChatVisibility.HIDDEN
                "commands", "system" -> getSettings().chatVisibility = ChatVisibility.SYSTEM
                else -> getSettings().chatVisibility = ChatVisibility.FULL
            }
        }

        // colors
        @JvmStatic
        fun getColors(): Boolean {
            //#if MC==11602
            //$$ return getSettings().chatColor
            //#else
            return getSettings().chatColours
            //#endif
        }

        @JvmStatic
        fun setColors(toggled: Boolean) {
            //#if MC==11602
            //$$ getSettings().chatColor = toggled
            //#else
            getSettings().chatColours = toggled
            //#endif
        }

        // web links
        @JvmStatic
        fun getWebLinks() = getSettings().chatLinks

        @JvmStatic
        fun setWebLinks(toggled: Boolean) {
            getSettings().chatLinks = toggled
        }

        // opacity
        @JvmStatic
        fun getOpacity() = getSettings().chatOpacity

        @JvmStatic
        fun setOpacity(opacity: Double) {
            //#if MC==11602
            //$$ getSettings().chatOpacity = opacity
            //#else
            getSettings().chatOpacity = opacity.toFloat()
            //#endif
        }

        // prompt on links
        @JvmStatic
        fun getPromptOnWebLinks() = getSettings().chatLinksPrompt

        @JvmStatic
        fun setPromptOnWebLinks(toggled: Boolean) {
            getSettings().chatLinksPrompt = toggled
        }
    }
}
