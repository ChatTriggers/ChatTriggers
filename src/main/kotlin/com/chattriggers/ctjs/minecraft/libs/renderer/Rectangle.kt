package com.chattriggers.ctjs.minecraft.libs.renderer

import com.sun.javafx.geom.Vec2d

class Rectangle(
        private var color: Int,
        private var x: Float,
        private var y: Float,
        private var width: Float,
        private var height: Float) {

    private var shadow = Shadow()
    private var outline = Outline()

    fun getColor() = this.color
    fun setColor(color: Int) = apply { this.color = color }

    fun getX() = this.x
    fun setX(x: Float) = apply { this.x = x }

    fun getY() = this.y
    fun setY(y: Float) = apply { this.y = y }

    fun getWidth() = this.width
    fun setWidth(width: Float) = apply { this.width = width }

    fun getHeight() = this.height
    fun setHeight(height: Float) = apply { this.height = height }

    fun isShadow() = this.shadow.on
    fun setShadow(shadow: Boolean) = apply { this.shadow.on = shadow }

    fun getShadowOffset() = this.shadow.offset
    fun getShadowOffsetX() = this.shadow.offset.x
    fun getShadowOffsetY() = this.shadow.offset.y
    fun setShadowOffset(x: Double, y: Double) = apply {
        this.shadow.offset.x = x
        this.shadow.offset.y = y
    }
    fun setShadowOffsetX(x: Double) = apply { this.shadow.offset.x = x }
    fun setShadowOffsetY(y: Double) = apply { this.shadow.offset.y = y }

    fun getShadowColor() = this.shadow.color
    fun setShadowColor(color: Int) = apply { this.shadow.color = color }

    fun setShadow(color: Int, x: Double, y: Double) = apply {
        setShadow(true)
        setShadowColor(color)
        setShadowOffset(x, y)
    }

    fun getOutline() = this.outline.on
    fun setOutline(outline: Boolean) = apply { this.outline.on = outline }

    fun getOutlineColor() = this.outline.color
    fun setOutlineColor(color: Int) = apply { this.outline.color = color }

    fun getThickness() = this.outline.thickness
    fun setThickness(thickness: Float) = apply { this.outline.thickness = thickness }

    fun setOutline(color: Int, thickness: Float) = apply {
        setOutline(true)
        setOutlineColor(color)
        setThickness(thickness)
    }

    fun draw() = apply {
        this.shadow.draw()
        this.outline.draw()
        Renderer.drawRect(this.color, this.x, this.y, this.width, this.height)
        Renderer.finishDraw()
    }

    private fun Shadow.draw() {
        if (!this.on) return
        Renderer.drawRect(this.color,
                this@Rectangle.x + this.offset.x.toFloat(),
                this@Rectangle.y + this@Rectangle.height,
                this@Rectangle.width,
                this.offset.y.toFloat()
        )
        Renderer.drawRect(this.color,
                this@Rectangle.x + this@Rectangle.width,
                this@Rectangle.y + this.offset.y.toFloat(),
                this.offset.x.toFloat(),
                this@Rectangle.height - this.offset.y.toFloat()
        )
    }

    private fun Outline.draw() {
        if (!this.on) return
        Renderer.drawRect(this.color,
                this@Rectangle.x - this.thickness,
                this@Rectangle.y - this.thickness,
                this@Rectangle.width + this.thickness * 2,
                this@Rectangle.height + this.thickness * 2
        )
    }

    private class Shadow(
            var on: Boolean = false,
            var color: Int = 0x50000000,
            var offset: Vec2d = Vec2d(5.0, 5.0))

    private class Outline(
            var on: Boolean = false,
            var color: Int = 0xff000000.toInt(),
            var thickness: Float = 5f)
}