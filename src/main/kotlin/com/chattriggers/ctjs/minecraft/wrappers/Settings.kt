package com.chattriggers.ctjs.minecraft.wrappers

import com.chattriggers.ctjs.utils.kotlin.*
import com.chattriggers.ctjs.utils.kotlin.MCChatVisibility
import com.chattriggers.ctjs.utils.kotlin.MCCloudMode
import com.chattriggers.ctjs.utils.kotlin.MCGraphicsMode
import com.chattriggers.ctjs.utils.kotlin.MCParticleMode
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.Option
import net.minecraft.client.render.entity.PlayerModelPart
import net.minecraft.sound.SoundCategory
import net.minecraft.world.Difficulty

@External
class Settings {
    fun getSettings() = Client.getMinecraft().options

    fun getFOV() = getSettings().fov

    fun setFOV(fov: Double) {
        getSettings().fov = fov
    }

    fun getDifficulty() = getSettings().difficulty.id

    fun setDifficulty(difficulty: Int) {
        getSettings().difficulty = Difficulty.byOrdinal(difficulty)
    }

    val skin = object {
        fun getCape() = getSettings().isPlayerModelPartEnabled(PlayerModelPart.CAPE)

        fun setCape(toggled: Boolean) {
            setModelPart(toggled, PlayerModelPart.CAPE)
        }

        fun getJacket() = getSettings().isPlayerModelPartEnabled(PlayerModelPart.JACKET)

        fun setJacket(toggled: Boolean) {
            setModelPart(toggled, PlayerModelPart.JACKET)
        }

        fun getLeftSleeve() = getSettings().isPlayerModelPartEnabled(PlayerModelPart.LEFT_SLEEVE)

        fun setLeftSleeve(toggled: Boolean) {
            setModelPart(toggled, PlayerModelPart.LEFT_SLEEVE)
        }

        fun getRightSleeve() = getSettings().isPlayerModelPartEnabled(PlayerModelPart.RIGHT_SLEEVE)

        fun setRightSleeve(toggled: Boolean) {
            setModelPart(toggled, PlayerModelPart.RIGHT_SLEEVE)
        }

        fun getLeftPantsLeg() = getSettings().isPlayerModelPartEnabled(PlayerModelPart.LEFT_PANTS_LEG)

        fun setLeftPantsLeg(toggled: Boolean) {
            setModelPart(toggled, PlayerModelPart.LEFT_PANTS_LEG)
        }

        fun getRightPantsLeg() = getSettings().isPlayerModelPartEnabled(PlayerModelPart.RIGHT_PANTS_LEG)

        fun setRightPantsLeg(toggled: Boolean) {
            setModelPart(toggled, PlayerModelPart.RIGHT_PANTS_LEG)
        }

        fun getHat() = getSettings().isPlayerModelPartEnabled(PlayerModelPart.HAT)

        fun setHat(toggled: Boolean) {
            setModelPart(toggled, PlayerModelPart.HAT)
        }

        private fun setModelPart(toggled: Boolean, modelPart: PlayerModelPart) {
            getSettings().togglePlayerModelPart(modelPart, toggled)
        }
    }

    val sound = object {
        fun getMasterVolume() = getSettings().getSoundVolume(SoundCategory.MASTER)

        fun setMasterVolume(level: Float) = getSettings().setSoundVolume(SoundCategory.MASTER, level)

        fun getMusicVolume() = getSettings().getSoundVolume(SoundCategory.MUSIC)

        fun setMusicVolume(level: Float) = getSettings().setSoundVolume(SoundCategory.MUSIC, level)

        fun getNoteblockVolume() = getSettings().getSoundVolume(SoundCategory.RECORDS)

        fun setNoteblockVolume(level: Float) = getSettings().setSoundVolume(SoundCategory.RECORDS, level)

        fun getWeather() = getSettings().getSoundVolume(SoundCategory.WEATHER)

        fun setWeather(level: Float) = getSettings().setSoundVolume(SoundCategory.WEATHER, level)

        fun getBlocks() = getSettings().getSoundVolume(SoundCategory.BLOCKS)

        fun setBlocks(level: Float) = getSettings().setSoundVolume(SoundCategory.BLOCKS, level)

        //#if MC<=10809
        fun getHostileCreatures() = getSettings().getSoundVolume(SoundCategory.HOSTILE)

        fun setHostileCreatures(level: Float) = getSettings().setSoundVolume(SoundCategory.HOSTILE, level)

        fun getFriendlyCreatures() = getSettings().getSoundVolume(SoundCategory.NEUTRAL)

        fun setFriendlyCreatures(level: Float) = getSettings().setSoundVolume(SoundCategory.NEUTRAL, level)
        //#else
        //$$ fun getHostileCreatures() = getSettings().getSoundVolume(SoundCategory.HOSTILE)
        //$$ fun setHostileCreatures(level: Float) = getSettings().setSoundVolume(SoundCategory.HOSTILE, level)
        //$$
        //$$ fun getFriendlyCreatures() = getSettings().getSoundVolume(SoundCategory.NEUTRAL)
        //$$ fun setFriendlyCreatures(level: Float) = getSettings().setSoundVolume(SoundCategory.NEUTRAL, level)
        //#endif

        fun getPlayers() = getSettings().getSoundVolume(SoundCategory.PLAYERS)

        fun setPlayers(level: Float) = getSettings().setSoundVolume(SoundCategory.PLAYERS, level)

        fun getAmbient() = getSettings().getSoundVolume(SoundCategory.AMBIENT)

        fun setAmbient(level: Float) = getSettings().setSoundVolume(SoundCategory.AMBIENT, level)
    }

    val video = object {
        // TODO(BREAKING): Returns an enum instead of a boolean
        fun getGraphics() = GraphicsMode.values().first { it.mcMode == getSettings().graphicsMode }

        fun setGraphics(graphicsMode: Any) {
            // From Option.GRAPHICS
            val mode = when (graphicsMode) {
                is MCGraphicsMode -> graphicsMode
                is GraphicsMode -> graphicsMode.mcMode
                else -> throw IllegalArgumentException("setGraphics expected a GraphicsMode")
            }

            val videoWarningManager = Client.getMinecraft().videoWarningManager
            if (graphicsMode == net.minecraft.client.option.GraphicsMode.FABULOUS && videoWarningManager.canWarn()) {
                videoWarningManager.scheduleWarning()
            } else {
                getSettings().graphicsMode = mode
                Client.getMinecraft().worldRenderer.reload()
            }

        }

        fun getRenderDistance() = Option.RENDER_DISTANCE.get(getSettings())

        fun setRenderDistance(distance: Double) {
            Option.RENDER_DISTANCE.set(getSettings(), distance)
        }

        // TODO("fabric")
        // fun getSmoothLighting() = getSettings().ambientOcclusion
        //
        // fun setSmoothLighting(level: Int) {
        //     getSettings().ambientOcclusion = level
        // }

        fun getMaxFrameRate() = Option.FRAMERATE_LIMIT.get(getSettings())

        fun setMaxFrameRate(frameRate: Double) {
            Option.FRAMERATE_LIMIT.set(getSettings(), frameRate)
        }

        // TODO("fabric")
        // fun get3dAnaglyph() = getSettings().anaglyph
        //
        // fun set3dAnaglyph(toggled: Boolean) {
        //     getSettings().anaglyph = toggled
        // }

        fun getBobbing() = getSettings().bobView

        fun setBobbing(toggled: Boolean) {
            getSettings().bobView = toggled
        }

        fun getGuiScale() = getSettings().guiScale

        fun setGuiScale(scale: Int) {
            getSettings().guiScale = scale
        }

        fun getBrightness() = Option.GAMMA.get(getSettings())

        fun setBrightness(brightness: Double) {
            Option.GAMMA.set(getSettings(), brightness)
        }

        fun getClouds() = CloudMode.values().first { it.mcMode == getSettings().cloudRenderMode }

        fun setClouds(clouds: Any) {
            val mode = when (clouds) {
                is MCCloudMode -> clouds
                is CloudMode -> clouds.mcMode
                else -> throw IllegalArgumentException("setClouds expected a CloudMode enum")
            }

            // From Option.CLOUDS
            getSettings().cloudRenderMode = mode
            if (MinecraftClient.isFabulousGraphicsOrBetter()) {
                val framebuffer = MinecraftClient.getInstance().worldRenderer.cloudsFramebuffer
                framebuffer?.clear(MinecraftClient.IS_SYSTEM_MAC)
            }
        }

        fun getParticles() = ParticleMode.values().first { it.mcMode == getSettings().particles }

        fun setParticles(particles: Any) {
            getSettings().particles = when (particles) {
                is MCParticleMode -> particles
                is ParticleMode -> particles.mcMode
                else -> throw IllegalArgumentException("setParticles expected a ParticleMode enum")
            }
        }

        fun getFullscreen() = getSettings().fullscreen

        fun setFullscreen(toggled: Boolean) {
            // From Option.FULLSCREEN
            getSettings().fullscreen = toggled
            val minecraftClient = MinecraftClient.getInstance()
            if (minecraftClient.window != null && minecraftClient.window.isFullscreen != getSettings().fullscreen) {
                minecraftClient.window.toggleFullscreen()
                getSettings().fullscreen = minecraftClient.window.isFullscreen
            }
        }

        fun getVsync() = getSettings().enableVsync

        fun setVsync(toggled: Boolean) {
            // From Option.VSYNC
            getSettings().enableVsync = toggled
            if (MinecraftClient.getInstance().window != null) {
                MinecraftClient.getInstance().window.setVsync(getSettings().enableVsync)
            }
        }

        fun getMipmapLevels() = getSettings().mipmapLevels

        fun setMipmapLevels(mipmapLevels: Int) {
            getSettings().mipmapLevels = mipmapLevels
        }

        // TODO(BREAKING): Remove this
        // //#if MC<=10809
        // fun getAlternateBlocks() = getSettings().allowBlockAlternatives
        //
        // fun setAlternateBlocks(toggled: Boolean) {
        //     getSettings().allowBlockAlternatives = toggled
        // }
        // //#else
        // //$$ fun getAlternateBlocks() = UnsupportedOperationException("1.12 has no Alternative Blocks settings")
        // //$$ fun setAlternateBlocks(toggled: Boolean) = UnsupportedOperationException("1.12 has no Alternative Blocks settings")
        // //#endif

        // TODO("fabric")
        // fun getVBOs() = getSettings().useVbo
        //
        // fun setVBOs(toggled: Boolean) {
        //     getSettings().useVbo = toggled
        // }

        fun getEntityShadows() = getSettings().entityShadows

        fun setEntityShadows(toggled: Boolean) {
            getSettings().entityShadows = toggled
        }
    }

    val chat = object {
        fun getVisibility() = ChatVisibility.values().first { it.mcMode == getSettings().chatVisibility }

        fun setVisibility(visibility: Any) {
            getSettings().chatVisibility = when (visibility) {
                is MCChatVisibility -> visibility
                is ChatVisibility -> visibility.mcMode
                else -> throw IllegalArgumentException("setVisibility expects a ChatVisibility enum")
            }
        }

        fun getColors() = getSettings().chatColors

        fun setColors(toggled: Boolean) {
            getSettings().chatColors = toggled
        }

        fun getWebLinks() = getSettings().chatLinks

        fun setWebLinks(toggled: Boolean) {
            getSettings().chatLinks = toggled
        }

        fun getOpacity() = getSettings().chatOpacity

        fun setOpacity(opacity: Double) {
            // From Option.CHAT_OPACITY
            getSettings().chatOpacity = opacity
            Client.getMinecraft().inGameHud.chatHud.reset()
        }

        fun getPromptOnWebLinks() = getSettings().chatLinksPrompt

        fun setPromptOnWebLinks(toggled: Boolean) {
            getSettings().chatLinksPrompt = toggled
        }

        fun getScale() = getSettings().chatScale

        fun setScale(scale: Double) {
            // From Option.CHAT_SCALE
            getSettings().chatScale = scale
            Client.getMinecraft().inGameHud.chatHud.reset()
        }

        fun getFocusedHeight() = getSettings().chatHeightFocused

        fun setFocusedHeight(height: Double) {
            // From Option.CHAT_HEIGHT_FOCUSED
            getSettings().chatHeightFocused = height
            Client.getMinecraft().inGameHud.chatHud.reset()
        }

        fun getUnfocusedHeight() = getSettings().chatHeightUnfocused

        fun setUnfocusedHeight(height: Double) {
            getSettings().chatHeightUnfocused = height
            Client.getMinecraft().inGameHud.chatHud.reset()
        }

        fun getWidth() = getSettings().chatWidth

        fun setWidth(width: Double) {
            getSettings().chatWidth = width
            Client.getMinecraft().inGameHud.chatHud.reset()
        }

        fun getReducedDebugInfo() = getSettings().reducedDebugInfo

        fun setReducedDebugInfo(toggled: Boolean) {
            getSettings().reducedDebugInfo = toggled
        }
    }

    enum class GraphicsMode(val mcMode: MCGraphicsMode) {
        Fast(MCGraphicsMode.FAST),
        Fancy(MCGraphicsMode.FANCY),
        Fabulous(MCGraphicsMode.FABULOUS),
    }

    enum class CloudMode(val mcMode: MCCloudMode) {
        Off(MCCloudMode.OFF),
        Fast(MCCloudMode.FAST),
        Fancy(MCCloudMode.FANCY),
    }

    enum class ParticleMode(val mcMode: MCParticleMode) {
        All(MCParticleMode.ALL),
        Decreased(MCParticleMode.DECREASED),
        Minimal(MCParticleMode.MINIMAL),
    }

    enum class ChatVisibility(val mcMode: MCChatVisibility) {
        Full(MCChatVisibility.FULL),
        System(MCChatVisibility.SYSTEM),
        Hidden(MCChatVisibility.HIDDEN),
    }
}
