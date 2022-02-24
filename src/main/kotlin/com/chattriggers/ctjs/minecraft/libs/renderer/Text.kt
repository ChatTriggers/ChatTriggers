package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.GlStateManager

@External
class Text @JvmOverloads constructor(private var string: String, private var x: Float = 0f, private var y: Float = 0f) {
    private val lines = mutableListOf<String>()

    private var color = 0xffffffff
    private var formatted = true
    private var shadow = false
    private var align = DisplayHandler.Align.LEFT

    private var width = 0f
    private var maxWidth = 0
    private var maxLines = Int.MAX_VALUE
    private var scale = 1f

    init {
        updateFormatting()
    }

    fun getString(): String = string

    fun setString(string: String) = apply {
        this.string = string
        updateFormatting()
    }

    fun getColor(): Long = color

    fun setColor(color: Long) = apply { this.color = Renderer.fixAlpha(color) }

    fun getFormatted(): Boolean = formatted

    fun setFormatted(formatted: Boolean) = apply {
        this.formatted = formatted
        updateFormatting()
    }

    fun getShadow(): Boolean = shadow

    fun setShadow(shadow: Boolean) = apply { this.shadow = shadow }

    fun getAlign(): DisplayHandler.Align = align

    fun setAlign(align: Any) = apply {
        this.align = when (align) {
            is String -> DisplayHandler.Align.valueOf(align.uppercase())
            is DisplayHandler.Align -> align
            else -> DisplayHandler.Align.LEFT
        }
    }

    fun getX(): Float = x

    fun setX(x: Float) = apply { this.x = x }

    fun getY(): Float = y

    fun setY(y: Float) = apply { this.y = y }

    /**
     * Gets the width of the text
     * This is automatically updated when the text is drawn.
     *
     * @return the width of the text
     */
    fun getWidth(): Float = width

    fun getLines(): List<String> = lines

    fun getMaxLines(): Int = maxLines

    fun setMaxLines(maxLines: Int) = apply { this.maxLines = maxLines }

    fun getScale(): Float = scale

    fun setScale(scale: Float) = apply { this.scale = scale }

    /**
     * Sets the maximum width of the text, splitting it into multiple lines if necessary.
     *
     * @param maxWidth the maximum width of the text
     * @return the Text object for method chaining
     */
    fun setMaxWidth(maxWidth: Int) = apply {
        this.maxWidth = maxWidth
        updateFormatting()
    }

    fun getMaxWidth(): Int = maxWidth

    fun getHeight(): Float {
        return if (lines.size > 1)
            lines.size.coerceAtMost(maxLines) * scale * 10
        else scale * 10
    }

    fun exceedsMaxLines(): Boolean {
        return lines.size > maxLines
    }

    @JvmOverloads
    fun draw(x: Float? = null, y: Float? = null) = apply {
        GlStateManager.enableBlend()
        GlStateManager.scale(scale, scale, scale)

        var longestLine = lines.maxOf { Renderer.getStringWidth(it) * scale }
        if (maxWidth != 0)
            longestLine = longestLine.coerceAtMost(maxWidth.toFloat())
        width = longestLine

        var yHolder = y ?: this.y

        for (i in 0 until maxLines) {
            if (i >= lines.size) break
            Renderer.getFontRenderer()
                .drawString(lines[i], getXAlign(lines[i], x ?: this.x), yHolder / scale, color.toInt(), shadow)
            yHolder += scale * 10
        }
        GlStateManager.disableBlend()
        Renderer.finishDraw()
    }

    private fun updateFormatting() {
        string =
            if (formatted) ChatLib.addColor(string)
            else ChatLib.replaceFormatting(string)

        lines.clear()

        string.split("\n").forEach {
            if (maxWidth > 0) {
                lines.addAll(Renderer.getFontRenderer().listFormattedStringToWidth(it, maxWidth))
            } else {
                lines.add(it)
            }
        }
    }

    private fun getXAlign(string: String, x: Float): Float {
        val newX = x / scale
        return when (align) {
            DisplayHandler.Align.CENTER -> newX - Renderer.getStringWidth(string) / 2
            DisplayHandler.Align.RIGHT -> newX - Renderer.getStringWidth(string)
            else -> newX
        }
    }

    override fun toString() =
        "Text{" +
                "string=$string, x=$x, y=$y, " +
                "lines=$lines, color=$color, scale=$scale, " +
                "formatted=$formatted, shadow=$shadow, align=$align, " +
                "width=$width, maxWidth=$maxWidth, maxLines=$maxLines" +
                "}"
}
