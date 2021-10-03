package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.GlStateManager

@External
class Text @JvmOverloads constructor(
    private var string: String,
    private var x: Float = 0f,
    private var y: Float = 0f,
) {
    private var lines = listOf(string)

    private var color = 0xffffffff
    private var formatted = true
    private var shadow = false
    private var align = DisplayHandler.Align.LEFT

    private var width = 0
    private var maxLines = 0
    private var scale = 1f

    init {
        updateFormatting()
    }

    fun getString(): String = string
    fun setString(string: String) = apply { this.string = string }

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

    fun getWidth(): Int = width
    fun setWidth(width: Int) = apply {
        this.width = width
        lines = formatStringToList(string, width)
    }

    fun getLines(): List<String> = lines

    fun getMaxLines(): Int = maxLines
    fun setMaxLines(maxLines: Int) = apply { this.maxLines = maxLines }

    fun getScale(): Float = scale
    fun setScale(scale: Float) = apply { this.scale = scale }

    fun getMaxWidth(): Int {
        return if (width == 0) {
            Renderer.getStringWidth(string)
        } else lines.maxOf(Renderer::getStringWidth)
    }

    fun getHeight(): Float {
        return if (width == 0) {
            scale * 9
        } else lines.size * scale * 9
    }

    fun exceedsMaxLines(): Boolean {
        return width != 0 && lines.size > maxLines
    }

    @JvmOverloads
    fun draw(x: Float = this.x, y: Float = this.y) = apply {
        GlStateManager.enableBlend()
        GlStateManager.scale(scale, scale, scale)
        if (width > 0) {
            var maxLinesHolder = maxLines
            var yHolder = y
            lines.forEach {
                //#if MC==11602
                //$$ if (shadow) {
                //$$     Renderer.getFontRenderer().drawStringWithShadow(
                //$$         Renderer.getMatrixStack(),
                //$$         it,
                //$$         getXAlign(it, x),
                //$$         yHolder / scale,
                //$$         color.toInt(),
                //$$      )
                //$$ } else {
                //$$     Renderer.getFontRenderer().drawString(
                //$$         Renderer.getMatrixStack(),
                //$$         it,
                //$$         getXAlign(it, x),
                //$$         yHolder / scale,
                //$$         color.toInt(),
                //$$     )
                //$$ }
                //#else
                Renderer.getFontRenderer().drawString(
                    it,
                    getXAlign(it, x),
                    yHolder / scale,
                    color.toInt(),
                    shadow,
                )
                //#endif
                yHolder += scale * 9
                maxLinesHolder--
                if (maxLinesHolder == 0)
                    return@forEach
            }
        } else {
            //#if MC==11602
            //$$ if (shadow) {
            //$$     Renderer.getFontRenderer().drawStringWithShadow(
            //$$         Renderer.getMatrixStack(),
            //$$         string,
            //$$         getXAlign(string, x),
            //$$         y / scale,
            //$$         color.toInt(),
            //$$      )
            //$$ } else {
            //$$     Renderer.getFontRenderer().drawString(
            //$$         Renderer.getMatrixStack(),
            //$$         string,
            //$$         getXAlign(string, x),
            //$$         y / scale,
            //$$         color.toInt(),
            //$$     )
            //$$ }
            //#else
            Renderer.getFontRenderer().drawString(
                string,
                getXAlign(string, x),
                y / scale,
                color.toInt(),
                shadow,
            )
            //#endif
        }
        GlStateManager.disableBlend()
        Renderer.finishDraw()
    }

    private fun updateFormatting() {
        string = if (formatted) {
            ChatLib.addColor(string)
        } else ChatLib.replaceFormatting(string)
    }

    private fun getXAlign(string: String, x: Float): Float {
        val newX = x / scale
        return when (align) {
            DisplayHandler.Align.CENTER -> newX - Renderer.getStringWidth(string) / 2
            DisplayHandler.Align.RIGHT -> newX - Renderer.getStringWidth(string)
            else -> newX
        }
    }

    private fun formatStringToList(string_: String, maxLineWidth: Int): List<String> {
        //#if MC==11602
        //$$ val lines = mutableListOf<String>()
        //$$ var string = string_
        //$$
        //$$ while (true) {
        //$$     var i = 0
        //$$     val line = string.takeWhile {
        //$$         Renderer.getStringWidth(string.substring(0, i)) < maxLineWidth
        //$$     }
        //$$     if (line.length <= 1) {
        //$$         // maxLineWidth is too small
        //$$         lines.add(string)
        //$$         break
        //$$     }
        //$$
        //$$     lines.add(line.dropLast(1))
        //$$     string = string.substring(line.length - 1)
        //$$ }
        //$$
        //$$ return lines
        //#else
        return Renderer.getFontRenderer().listFormattedStringToWidth(string_, maxLineWidth)
        //#endif
    }

    override fun toString() =
        "Text{" +
            "string=$string, x=$x, y=$y, " +
            "lines=$lines, color=$color, scale=$scale" +
            "formatted=$formatted, shadow=$shadow, align=$align, " +
            "width=$width, maxLines=$maxLines" +
            "}"
}
