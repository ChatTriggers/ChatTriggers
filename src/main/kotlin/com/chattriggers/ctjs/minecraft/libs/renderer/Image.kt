package com.chattriggers.ctjs.minecraft.libs.renderer

import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraftforge.common.MinecraftForge
import java.awt.image.BufferedImage

class Image(image: BufferedImage) {
    private lateinit var texture: DynamicTexture

    private var textureWidth = 0
    private var textureHeight = 0

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun getTextureWidth() = this.textureWidth
    fun getTextureHeight() = this.textureHeight
    fun getTexture(): DynamicTexture = this.texture

    @JvmOverloads
    fun draw(x: Double, y: Double,
             width: Double = this.textureWidth.toDouble(),
             height: Double = this.textureHeight.toDouble()) = apply {
        Renderer.drawImage(this, x, y, width, height)
    }
}