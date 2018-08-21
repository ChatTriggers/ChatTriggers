package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import net.minecraft.client.renderer.GlStateManager

class Text {
    private var string: String
    private var lines = mutableListOf<String>()

    private var color = 0xffffffff.toInt()
    private var formatted = true
    private var shadow = false
    private var align = DisplayHandler.Align.LEFT

    private var x: Float
    private var y: Float
    private var width = 0
    private var maxLines = 0
    private var scale = 1f

    constructor(text: String): this(text, 0f, 0f)
    constructor(text: String, x: Float, y: Float) {x
        this.string = text
        this.lines.add(this.string)
        updateFormatting()

        this.x = x
        this.y = y
    }

    fun getString() = this.string
    fun setString(string: String): Text {
        this.string = string
        return this
    }

    fun getColor() = this.color
    fun setColor(color: Int): Text {
        this.color = color
        return this
    }

    fun getFormatted() = this.formatted
    fun setFormatted(formatted: Boolean): Text {
        this.formatted = formatted
        updateFormatting()
        return this
    }

    fun getShadow() = this.shadow
    fun setShadow(shadow: Boolean): Text {
        this.shadow = shadow
        return this
    }

    fun getAlign() = this.align
    fun setAlign(align: Any): Text {
        this.align = when (align) {
            is String -> DisplayHandler.Align.valueOf(align.toUpperCase())
            is DisplayHandler.Align -> align
            else -> DisplayHandler.Align.LEFT
        }
        return this
    }

    fun getX() = this.x
    fun setX(x: Float): Text {
        this.x = x
        return this
    }

    fun getY() = this.y
    fun setY(y: Float): Text {
        this.y = y
        return this
    }

    fun getWidth() = this.width
    fun setWidth(width: Int): Text {
        this.width = width
        this.lines = Renderer.getFontRenderer().listFormattedStringToWidth(this.string, this.width)
        return this
    }

    fun getMaxLines() = this.maxLines
    fun setMaxLines(maxLines: Int): Text {
        this.maxLines = maxLines
        return this
    }

    fun getScale() = this.scale
    fun setScale(scale: Float): Text {
        this.scale = scale
        return this
    }

    fun getMaxWidth(): Int {
        if (this.width == 0) {
            return Renderer.getStringWidth(this.string)
        } else {
            var maxWidth = 0
            this.lines.forEach {
                if (Renderer.getStringWidth(it) > maxWidth)
                    maxWidth = Renderer.getStringWidth(it)
            }
            return maxWidth
        }
    }

    fun getHeight(): Float {
        return if (this.width == 0)
            this.scale * 9
            else this.lines.size * this.scale * 9
    }

    fun exceedsMaxLines(): Boolean {
        return this.width != 0 && this.lines.size > this.maxLines
    }

    fun draw(): Text {
        GlStateManager.enableBlend()
        GlStateManager.scale(this.scale, this.scale, this.scale)
        if (this.width > 0) {
            var maxLinesHolder = this.maxLines
            var yHolder = this.y
            this.lines.forEach {
                Renderer.getFontRenderer().drawString(it, getXAlign(it), yHolder / this.scale, this.color, this.shadow)
                yHolder += this.scale * 9
                maxLinesHolder--
                if (maxLinesHolder == 0)
                    return@forEach
            }
        } else {
            Renderer.getFontRenderer().drawString(this.string, getXAlign(this.string), this.y / this.scale, this.color, this.shadow)
        }
        GlStateManager.disableBlend()
        Renderer.finishDraw()

        return this
    }

    private fun updateFormatting() {
        this.string =
                if (this.formatted) ChatLib.addColor(this.string)
                else ChatLib.replaceFormatting(this.string)
    }

    private fun fixAlpha(color: Int): Int {
        val alpha = color shr 24 and 255
        return if (alpha < 10)
            Renderer.color(color shr 16 and 255, color shr 8 and 255, color and 255, 10)
            else color
    }

    private fun getXAlign(string: String): Float {
        val x = this.x / this.scale
        return when (this.align) {
            DisplayHandler.Align.CENTER -> x - Renderer.getStringWidth(string) / 2
            DisplayHandler.Align.RIGHT -> x - Renderer.getStringWidth(string)
            else -> x
        }
    }
}