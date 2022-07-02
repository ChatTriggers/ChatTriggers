package com.chattriggers.ctjs.minecraft.libs.renderer

import gg.essential.elementa.utils.Vector2f
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Shape(private var color: Long) {
    private val vertexes = mutableListOf<Vector2f>()
    private val reversedVertexes = vertexes.asReversed()
    private var drawMode = Renderer.DrawMode.Quads
    private var area = 0f

    fun copy(): Shape = clone()

    fun clone(): Shape {
        val clone = Shape(color)
        clone.vertexes.addAll(vertexes)
        clone.setDrawMode(drawMode)
        return clone
    }

    fun getColor(): Long = color

    fun setColor(color: Long) = apply { this.color = Renderer.fixAlpha(color) }

    fun getDrawMode() = drawMode

    /**
     * Sets the GL draw mode of the shape
     */
    fun setDrawMode(drawMode: Renderer.DrawMode) = apply { this.drawMode = drawMode }

    fun getVertexes(): List<Vector2f> = vertexes

    fun addVertex(x: Float, y: Float) = apply {
        vertexes.add(Vector2f(x, y))
        updateArea()
    }

    fun insertVertex(index: Int, x: Float, y: Float) = apply {
        vertexes.add(index, Vector2f(x, y))
        updateArea()
    }

    fun removeVertex(index: Int) = apply {
        vertexes.removeAt(index)
        updateArea()
    }

    /**
     * Sets the shape as a line pointing from [x1, y1] to [x2, y2] with a thickness
     */
    fun setLine(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float) = apply {
        vertexes.clear()

        val theta = -atan2(y2 - y1, x2 - x1)
        val i = sin(theta) * (thickness / 2)
        val j = cos(theta) * (thickness / 2)

        addVertex(x1 + i, y1 + j)
        addVertex(x2 + i, y2 + j)
        addVertex(x2 - i, y2 - j)
        addVertex(x1 - i, y1 - j)

        drawMode = Renderer.DrawMode.Quads
    }

    /**
     * Sets the shape as a circle with a center at [x, y]
     * with radius and number of steps around the circle
     */
    fun setCircle(x: Float, y: Float, radius: Float, steps: Int) = apply {
        vertexes.clear()

        val theta = 2 * PI / steps
        val cos = cos(theta).toFloat()
        val sin = sin(theta).toFloat()

        var xHolder: Float
        var circleX = 1f
        var circleY = 0f

        for (i in 0..steps) {
            addVertex(x, y)
            addVertex(circleX * radius + x, circleY * radius + y)
            xHolder = circleX
            circleX = cos * circleX - sin * circleY
            circleY = sin * xHolder + cos * circleY
            addVertex(circleX * radius + x, circleY * radius + y)
        }

        drawMode = Renderer.DrawMode.TriangleStrip
    }

    fun draw() = apply {
        Renderer.withColor(color) {
            Renderer.disableTexture2D()
            Renderer.beginVertices(drawMode, Renderer.VertexFormat.PositionColor)

            if (area < 0) {
                vertexes.forEach { Renderer.pos(it.x, it.y).col(color).endVertex() }
            } else {
                reversedVertexes.forEach { Renderer.pos(it.x, it.y).col(color).endVertex() }
            }

            Renderer.endVertices()
            Renderer.enableTexture2D()
        }
    }

    private fun updateArea() {
        var calculatedArea = 0f

        for (i in vertexes.indices) {
            val (x1, y1) = vertexes[i]
            val (x2, y2) = vertexes[(i + 1) % vertexes.size]

            calculatedArea += x1 * y2 - x2 * y1
        }

        area = calculatedArea / 2
    }
}
