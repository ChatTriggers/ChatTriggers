package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.ScaledResolution

/**
 * Used from [Renderer.screen] to get screen properties
 */
class Screen {
    private val scaledResolution = ScaledResolution(Client.getMinecraft())

    /**
     * @return the width of the screen in pixels
     */
    fun getWidth(): Int = scaledResolution.scaledWidth

    /**
     * @return the height of the screen in pixels
     */
    fun getHeight(): Int = scaledResolution.scaledHeight

    /**
     * @return the scale factor of the screen
     */
    fun getScale(): Int = scaledResolution.scaleFactor
}