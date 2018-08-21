package com.chattriggers.ctjs.minecraft.libs.renderer

import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraftforge.common.MinecraftForge
import java.awt.image.BufferedImage

class Image {
    private var imageToLoad: BufferedImage
    private lateinit var texture: DynamicTexture

    private var textureWidth = 0
    private var textureHeight = 0

    constructor(image: BufferedImage) {
        this.imageToLoad = image
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun getTextureWidth() = this.textureWidth
    fun getTextureHeight() = this.textureHeight
    fun getTexture(): DynamicTexture = this.texture

    fun draw(x: Double, y: Double): Image {
        Renderer.drawImage(this, x, y, this.textureWidth.toDouble(), this.textureHeight.toDouble())
        return this
    }

    fun draw(x: Double, y: Double, width: Double, height: Double): Image {
        Renderer.drawImage(this, x, y, width, height)
        return this
    }
}