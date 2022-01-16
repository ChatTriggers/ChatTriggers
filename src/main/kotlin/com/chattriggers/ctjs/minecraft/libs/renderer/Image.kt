package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO


@External
class Image constructor(imageProducer: CompletableFuture<NativeImageBackedTexture?>) {
    var textureWidth: Int = 0
        private set
    var textureHeight: Int = 0
        private set
    var textureIdentifier: Identifier? = null
        private set

    init {
        imageProducer.thenAccept {
            if (it != null) {
                Client.getMinecraft().textureManager.registerDynamicTexture("chattriggers", it)
                textureWidth = it.image?.width ?: 0
                textureHeight = it.image?.height ?: 0
            }
        }
    }

    @JvmOverloads
    constructor(name: String, url: String? = null) : this(getBufferedImage(name, url))

    constructor(image: BufferedImage) : this(CompletableFuture.supplyAsync {
        ByteArrayOutputStream().use { baos ->
            ImageIO.write(image, "png", baos)
            ByteArrayInputStream(baos.toByteArray()).use {
                NativeImageBackedTexture(NativeImage.read(it))
            }
        }
    })

    fun destroy() {
        Client.getMinecraft().textureManager.destroyTexture(textureIdentifier ?: return)
    }

    @JvmOverloads
    fun draw(
        x: Double, y: Double,
        width: Double = textureWidth.toDouble(),
        height: Double = textureHeight.toDouble()
    ) = apply {
        Renderer.drawImage(this, x, y, width, height)
    }

    companion object {
        private fun getBufferedImage(name: String, url: String? = null): CompletableFuture<NativeImageBackedTexture?> {
            return CompletableFuture.supplyAsync {
                val resourceFile = File(CTJS.assetsDir, name)

                val inputStream = if (resourceFile.exists()) {
                    resourceFile.inputStream()
                } else {
                    val conn = (URL(url).openConnection() as HttpURLConnection).apply {
                        requestMethod = "GET"
                        doOutput = true
                        setRequestProperty("User-Agent", "Mozilla/5.0 (ChatTriggers)")
                    }

                    conn.inputStream
                }

                NativeImageBackedTexture(NativeImage.read(inputStream))

            }
        }
    }
}
