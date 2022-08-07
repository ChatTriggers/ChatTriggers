package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.CTJS
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.image.BufferedImage
import java.io.File
import java.net.HttpURLConnection
import javax.imageio.ImageIO

class Image(var image: BufferedImage?) {
    private lateinit var texture: DynamicTexture
    private val textureWidth = image?.width ?: 0
    private val textureHeight = image?.height ?: 0

    init {
        MinecraftForge.EVENT_BUS.register(this)
        CTJS.images.add(this)
    }

    @Deprecated(
        message = "API is ambiguous, especially when called from JavaScript, and is relative to the assets directory",
        replaceWith = ReplaceWith("Image.fromFile() /* or Image.fromUrl() */"),
    )
    @JvmOverloads
    constructor(name: String, url: String? = null) : this(getBufferedImage(name, url))

    @Deprecated(
        message = "Use static method for consistency",
        replaceWith = ReplaceWith("Image.fromFile()")
    )
    constructor(file: File) : this(ImageIO.read(file))

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

    /**
     * Clears the image from GPU memory and removes its references CT side
     * that way it can be garbage collected if not referenced in js code.
     */
    fun destroy() {
        texture.deleteGlTexture()
        CTJS.images.remove(this)
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

    @Suppress("DEPRECATION")
    companion object {
        /**
         * Create an image object from a java.io.File object. Throws an exception
         * if the file cannot be found.
         */
        @JvmStatic
        fun fromFile(file: File) =  Image(file)

        /**
         * Create an image object from a file path. Throws an exception
         * if the file cannot be found.
         */
        @JvmStatic
        fun fromFile(file: String) = Image(File(file))

        /**
         * Create an image object from a file path, relative to the assets directory.
         * Throws an exception if the file cannot be found.
         */
        @JvmStatic
        fun fromAsset(name: String) = Image(File(CTJS.assetsDir, name))

        /**
         * Creates an image object from a URL. Throws an exception if an image
         * cannot be created from the URL. Will cache the image in the assets
         */
        @JvmStatic
        @JvmOverloads
        fun fromUrl(url: String, cachedImageName: String? = null): Image {
            if (cachedImageName == null)
                return Image(getImageFromUrl(url))
            return Image(cachedImageName, url)
        }

        private fun getBufferedImage(name: String, url: String? = null): BufferedImage? {
            val resourceFile = File(CTJS.assetsDir, name)

            if (resourceFile.exists())
                return ImageIO.read(resourceFile)

            val image = getImageFromUrl(url!!)
            ImageIO.write(image, "png", resourceFile)
            return image
        }

        private fun getImageFromUrl(url: String): BufferedImage {
            val conn = (CTJS.makeWebRequest(url) as HttpURLConnection).apply {
                requestMethod = "GET"
                doOutput = true
            }

            return ImageIO.read(conn.inputStream)
        }
    }
}
