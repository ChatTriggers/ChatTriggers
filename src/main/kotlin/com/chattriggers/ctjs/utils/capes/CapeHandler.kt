package com.chattriggers.ctjs.utils.capes

import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.google.gson.Gson
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.IImageBuffer
import net.minecraft.client.renderer.ThreadDownloadImageData
import net.minecraft.util.ResourceLocation
import java.awt.image.BufferedImage

object CapeHandler {
    private val special: Special

    init {
        this.special = Gson().fromJson(
                FileLib.getUrlContent("https://www.chattriggers.com/assets/capes/capes.json"),
                Special::class.java
        )

        bindTexture("https://imgur.com/9O3o7DM.png", "capes/ct/developer")
        bindTexture("https://imgur.com/5I2hF2q.png", "capes/ct/supporter")
        bindTexture("https://imgur.com/U5P61R0.png", "capes/ct/creator")
        bindTexture("https://imgur.com/gbGbZo6.png", "capes/ct/special")
    }

    fun getCapeResource(player: AbstractClientPlayer): ResourceLocation? {
        return when (player.uniqueID.toString()) {
            in this.special.developers -> ResourceLocation("capes/ct/developer")
            in this.special.supporters -> ResourceLocation("capes/ct/supporter")
            in this.special.creators -> ResourceLocation("capes/ct/creator")
            in this.special.special -> ResourceLocation("capes/ct/special")
            else -> null
        }
    }

    private fun bindTexture(url: String, resource: String) {
        val iib = object : IImageBuffer {
            override fun parseUserSkin(var1: BufferedImage) = parseCape(var1)
            override fun skinAvailable() {}
        }

        val rl = ResourceLocation(resource)
        val textureManager = Minecraft.getMinecraft().textureManager
        textureManager.getTexture(rl)
        val textureCape = ThreadDownloadImageData(null, url, null, iib)
        textureManager.loadTexture(rl, textureCape)
    }

    private fun parseCape(img: BufferedImage): BufferedImage {
        var imageWidth = 64
        var imageHeight = 32

        val srcWidth = img.width
        val srcHeight = img.height
        while (imageWidth < srcWidth || imageHeight < srcHeight) {
            imageWidth *= 2
            imageHeight *= 2
        }
        val imgNew = BufferedImage(imageWidth, imageHeight, 2)
        val g = imgNew.graphics
        g.drawImage(img, 0, 0, null)
        g.dispose()
        return imgNew
    }

    class Special(val supporters: Array<String>, val developers: Array<String>, val creators: Array<String>, val special: Array<String>)
}