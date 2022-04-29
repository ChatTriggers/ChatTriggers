package com.chattriggers.ctjs.minecraft.libs.renderer

import org.lwjgl.util.vector.Vector2f

class Rectangle(
    private var color: Long,
    private var x: Float,
    private var y: Float,
    private var width: Float,
    private var height: Float
) {

    private val shadow = Shadow(this)
    private val outline = Outline(this)

    fun getColor(): Long = color

    fun setColor(color: Long) = apply { this.color = Renderer.fixAlpha(color) }

    fun getX(): Float = x

    fun setX(x: Float) = apply { this.x = x }

    fun getY(): Float = y

    fun setY(y: Float) = apply { this.y = y }

    fun getWidth(): Float = width

    fun setWidth(width: Float) = apply { this.width = width }

    fun getHeight(): Float = height

    fun setHeight(height: Float) = apply { this.height = height }

    fun isShadow(): Boolean = shadow.on

    fun setShadow(shadow: Boolean) = apply { this.shadow.on = shadow }

    fun getShadowOffset(): Vector2f = shadow.offset

    fun getShadowOffsetX(): Float = shadow.offset.x

    fun getShadowOffsetY(): Float = shadow.offset.y

    fun setShadowOffset(x: Float, y: Float) = apply {
        shadow.offset.x = x
        shadow.offset.y = y
    }

    fun setShadowOffsetX(x: Float) = apply { shadow.offset.x = x }

    fun setShadowOffsetY(y: Float) = apply { shadow.offset.y = y }

    fun getShadowColor(): Long = shadow.color

    fun setShadowColor(color: Long) = apply { shadow.color = color }

    fun setShadow(color: Long, x: Float, y: Float) = apply {
        setShadow(true)
        setShadowColor(color)
        setShadowOffset(x, y)
    }

    fun getOutline(): Boolean = outline.on

    fun setOutline(outline: Boolean) = apply { this.outline.on = outline }

    fun getOutlineColor(): Long = outline.color

    fun setOutlineColor(color: Long) = apply { outline.color = color }

    fun getThickness(): Float = outline.thickness

    fun setThickness(thickness: Float) = apply { outline.thickness = thickness }

    fun setOutline(color: Long, thickness: Float) = apply {
        setOutline(true)
        setOutlineColor(color)
        setThickness(thickness)
    }

    fun draw() = apply {
        shadow.draw()
        outline.draw()
        Renderer.drawRect(color, x, y, width, height)
    }

    private class Shadow(
        val rect: Rectangle,
        var on: Boolean = false,
        var color: Long = 0x50000000,
        var offset: Vector2f = Vector2f(5f, 5f)
    ) {
        fun draw() {
            if (!on) return
            Renderer.drawRect(
                color,
                rect.x + offset.x,
                rect.y + rect.height,
                rect.width,
                offset.y
            )
            Renderer.drawRect(
                color,
                rect.x + rect.width,
                rect.y + offset.y,
                offset.x,
                rect.height - offset.y
            )
        }
    }

    private class Outline(
        val rect: Rectangle,
        var on: Boolean = false,
        var color: Long = 0xff000000,
        var thickness: Float = 5f
    ) {
        fun draw() {
            if (!on) return
            Renderer.drawRect(
                color,
                rect.x - thickness,
                rect.y - thickness,
                rect.width + thickness * 2,
                rect.height + thickness * 2
            )
        }
    }
}
