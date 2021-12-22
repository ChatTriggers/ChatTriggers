package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.image.BufferedImage
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import javax.imageio.ImageIO

@External
class Image constructor(var image: BufferedImage?) {
    private lateinit var texture: DynamicTexture
    private val textureWidth = image?.width ?: 0
    private val textureHeight = image?.height ?: 0

    init {
        MinecraftForge.EVENT_BUS.register(this)
        CTJS.images.add(this)
    }

    @JvmOverloads
    constructor(name: String, url: String? = null) : this(getBufferedImage(name, url))

    fun getTextureWidth(): Int = textureWidth

    fun getTextureHeight(): Int = textureHeight

    fun getTexture(): DynamicTexture {
        if (!::texture.isInitialized) {
            // We're trying to access the texture before initialization. Presumably, the game overlay render event
            // hasn't fired yet so we haven't loaded the texture. Let's hope this is a rendering context!
            try {
                texture = DynamicTexture(image)
                image = null

                MinecraftForge.EVENT_BUS.unregister(this)
            } catch (e: Exception) {
                // Unlucky. This probably wasn't a rendering context.
                println("Trying to bake texture in a non-rendering context.")

                throw e
            }
        }

        return texture
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Pre) {
        if (image != null) {
            texture = DynamicTexture(image)
            image = null

            MinecraftForge.EVENT_BUS.unregister(this)
        }
    }

    @JvmOverloads
    fun draw(
        x: Double, y: Double,
        width: Double = textureWidth.toDouble(),
        height: Double = textureHeight.toDouble()
    ) = apply {
        if (image != null) return@apply

        Renderer.drawImage(this, x, y, width, height)
    }

    companion object {
        private fun getBufferedImage(name: String, url: String? = null): BufferedImage? {
            val resourceFile = File(CTJS.assetsDir, name)

            if (resourceFile.exists()) {
                return ImageIO.read(resourceFile)
            }

            val conn = (URL(url).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                doOutput = true
                setRequestProperty("User-Agent", "Mozilla/5.0 (ChatTriggers)")
            }

            val image = ImageIO.read(conn.inputStream) ?: return null
            ImageIO.write(image, "png", resourceFile)
            return image
        }
    }
}
