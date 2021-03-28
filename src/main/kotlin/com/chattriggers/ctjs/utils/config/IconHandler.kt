package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.minecraft.libs.renderer.Image
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.printTraceToConsole
import java.awt.Desktop
import java.net.URL
import javax.imageio.ImageIO

object IconHandler {
    private var icons = mutableListOf<Icon>()

    init {
        this.icons.add(Icon(
            Image(ImageIO.read(IconHandler.javaClass.classLoader.getResourceAsStream("images/CT_logo.png"))),
            "https://chattriggers.com/"
        ))

        this.icons.add(Icon(
            Image(ImageIO.read(IconHandler.javaClass.classLoader.getResourceAsStream("images/CT_patreon.png"))),
            "https://www.patreon.com/ChatTriggers",
            1
        ))

        this.icons.add(Icon(
            Image(ImageIO.read(IconHandler.javaClass.classLoader.getResourceAsStream("images/CT_github.png"))),
            "https://github.com/ChatTriggers/ct.js",
            2
        ))

        this.icons.add(Icon(
            Image(ImageIO.read(IconHandler.javaClass.classLoader.getResourceAsStream("images/CT_discord.png"))),
            "https://discordapp.com/invite/0fNjZyopOvBHZyG8",
            3
        ))
    }

    fun drawIcons() = icons.forEach { it.draw() }
    fun clickIcons(x: Int, y: Int) = icons.forEach { it.click(x, y) }

    private class Icon(private var image: Image, private var url: String, private var y: Int = -1) {
        fun draw() {
            if (this.y == -1)
                this.image.draw(0.0, (Renderer.screen.getHeight() - 65).toDouble(), 64.0, 64.0)
            else
                this.image.draw(
                    65.0,
                    (Renderer.screen.getHeight() - this.y * 21.3f).toInt().toDouble(),
                    (64 / 3).toDouble(),
                    (64 / 3).toDouble()
                )
        }

        fun click(x: Int, y: Int) {
            var ix = 65f
            var iy = Renderer.screen.getHeight() - this.y * 21.3f
            var size = 64f / 3f

            if (this.y == -1) {
                ix = 0f
                iy = Renderer.screen.getHeight() - 65f
                size = 64f
            }

            if (x > ix && x < ix + size && y > iy && y < iy + size) {
                try {
                    Desktop.getDesktop().browse(URL(this.url).toURI())
                    World.playSound("gui.button.press", 100f, 1f)
                } catch (exception: Exception) {
                    exception.printTraceToConsole()
                }
            }
        }
    }
}
