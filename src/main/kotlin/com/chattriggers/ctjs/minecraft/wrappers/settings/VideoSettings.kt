package com.chattriggers.ctjs.minecraft.wrappers.settings

import com.chattriggers.ctjs.minecraft.wrappers.Client

/**
 * Used from [Settings.video] to get video settings
 */
class VideoSettings {
    private val settings = Client.settings.getSettings()

    fun getGraphics() = settings.fancyGraphics
    fun setGraphics(fancy: Boolean) {
        settings.fancyGraphics = fancy
    }

    fun getRenderDistance() = settings.renderDistanceChunks
    fun setRenderDistance(distance: Int) {
        settings.renderDistanceChunks = distance
    }

    fun getSmoothLighting() = settings.ambientOcclusion
    fun setSmoothLighting(level: Int) {
        settings.ambientOcclusion = level
    }

    fun getMaxFrameRate() = settings.limitFramerate
    fun setMaxFrameRate(frameRate: Int) {
        settings.limitFramerate = frameRate
    }

    fun get3dAnaglyph() = settings.anaglyph
    fun set3dAnaglyph(toggled: Boolean) {
        settings.anaglyph = toggled
    }

    fun getBobbing() = settings.viewBobbing
    fun setBobbing(toggled: Boolean) {
        settings.viewBobbing = toggled
    }

    fun getGuiScale() = settings.guiScale
    fun setGuiScale(scale: Int) {
        settings.guiScale = scale
    }

    fun getBrightness() = settings.gammaSetting
    fun setBrightness(brightness: Float) {
        settings.gammaSetting = brightness
    }

    fun getClouds() = settings.clouds
    fun setClouds(clouds: Int) {
        settings.clouds = clouds
    }

    fun getParticles() = settings.particleSetting
    fun setParticles(particles: Int) {
        settings.particleSetting = particles
    }

    fun getFullscreen() = settings.fullScreen
    fun setFullscreen(toggled: Boolean) {
        settings.fullScreen = toggled
    }

    fun getVsync() = settings.enableVsync
    fun setVsync(toggled: Boolean) {
        settings.enableVsync = toggled
    }

    fun getMipmapLevels() = settings.mipmapLevels
    fun setMipmapLevels(mipmapLevels: Int) {
        settings.mipmapLevels = mipmapLevels
    }

    //#if MC<=10809
    fun getAlternateBlocks() = settings.allowBlockAlternatives
    fun setAlternateBlocks(toggled: Boolean) {
        settings.allowBlockAlternatives = toggled
    }
    //#else
    //$$ fun getAlternateBlocks() = UnsupportedOperationException("1.12 has no Alternative Blocks settings")
    //$$ fun setAlternateBlocks(toggled: Boolean) = UnsupportedOperationException("1.12 has no Alternative Blocks settings")
    //#endif

    fun getVBOs() = settings.useVbo
    fun setVBOs(toggled: Boolean) {
        settings.useVbo = toggled
    }

    fun getEntityShadows() = settings.entityShadows
    fun setEntityShadows(toggled: Boolean) {
        settings.entityShadows = toggled
    }
}