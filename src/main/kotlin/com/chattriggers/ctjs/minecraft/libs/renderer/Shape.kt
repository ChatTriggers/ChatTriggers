package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.chattriggers.ctjs.utils.kotlin.getRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.util.vector.Vector2f
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@External
class Shape(private var color: Long) {
    private val vertexes = mutableListOf<Vector2f>()
    private val reversedVertexes = vertexes.asReversed()
    private var drawMode = 9
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

    fun getDrawMode(): Int = drawMode

    /**
     * Sets the GL draw mode of the shape. Possible draw modes are:
     * 0 = points
     * 1 = lines
     * 2 = line loop
     * 3 = line strip
     * 5 = triangles
     * 5 = triangle strip
     * 6 = triangle fan
     * 7 = quads
     * 8 = quad strip
     * 9 = polygon
     */
    fun setDrawMode(drawMode: Int) = apply { this.drawMode = drawMode }

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

        drawMode = 7
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

        drawMode = 5
    }

    fun draw() = apply {
        val tessellator = MCTessellator.getInstance()
        val worldRenderer = tessellator.getRenderer()

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        Renderer.doColor(color)

        worldRenderer.begin(drawMode, DefaultVertexFormats.POSITION)

        if (area < 0) {
            vertexes.forEach {
                worldRenderer.pos(it.x.toDouble(), it.y.toDouble(), 0.0).endVertex()
            }
        } else {
            reversedVertexes.forEach {
                worldRenderer.pos(it.x.toDouble(), it.y.toDouble(), 0.0).endVertex()
            }
        }

        tessellator.draw()
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        Renderer.finishDraw()
    }

    private fun updateArea() {
        area = 0f

        for (i in vertexes.indices) {
            val p1 = vertexes[i]
            val p2 = vertexes[(i + 1) % vertexes.size]

            area += p1.x * p2.y - p2.x * p1.y
        }

        area /= 2
    }
}
