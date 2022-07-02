package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.triggers.EventType
import net.minecraft.client.renderer.texture.DynamicTexture
import java.awt.image.BufferedImage
import java.io.File
import java.net.HttpURLConnection
import javax.imageio.ImageIO

//#if MC>=11701
//$$ import com.mojang.blaze3d.platform.NativeImage
//$$ import java.io.ByteArrayInputStream
//$$ import java.io.ByteArrayOutputStream
//#endif

class Image(var image: BufferedImage?) {
    private lateinit var texture: DynamicTexture
    private val textureWidth = image?.width ?: 0
    private val textureHeight = image?.height ?: 0
    private lateinit var unregister: () -> Unit

    init {
        CTJS.images.add(this)

        unregister = CTJS.addEventListener(EventType.RenderOverlay) {
            if (image != null) {
                texture = getDynamicTexture()
                image = null

                unregister()
            }
        }
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
                texture = getDynamicTexture()
                image = null

                unregister()
            } catch (e: Exception) {
                // Unlucky. This probably wasn't a rendering context.
                println("Trying to bake texture in a non-rendering context.")

                throw e
            }
        }

        return texture
    }

    fun getTextureId(): Int {
        //#if MC<=11202
        return getTexture().glTextureId
        //#else
        //$$ return getTexture().id
        //#endif
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

    private fun getDynamicTexture(): DynamicTexture {
        //#if MC<=11202
        return DynamicTexture(image!!)
        //#else
        //$$ return ByteArrayOutputStream().let {
        //$$     ImageIO.write(image!!, "png", it)
        //$$     ByteArrayInputStream(it.toByteArray())
        //$$ }.let(NativeImage::read).let(::DynamicTexture)
        //#endif
    }

    companion object {
        private fun getBufferedImage(name: String, url: String? = null): BufferedImage? {
            val resourceFile = File(CTJS.assetsDir, name)

            if (resourceFile.exists()) {
                return ImageIO.read(resourceFile)
            }

            val conn = (CTJS.makeWebRequest(url!!) as HttpURLConnection).apply {
                requestMethod = "GET"
                doOutput = true
            }

            val image = ImageIO.read(conn.inputStream) ?: return null
            ImageIO.write(image, "png", resourceFile)
            return image
        }
    }
}
