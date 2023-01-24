package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.MathLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.chattriggers.ctjs.utils.kotlin.getRenderer
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import java.util.*
import kotlin.math.*

object Renderer {
    var colorized: Long? = null
    private var retainTransforms = false
    private var drawMode: Int? = null

    private val tessellator = MCTessellator.getInstance()
    private val worldRenderer = tessellator.getRenderer()

    private var xScale: Float? = null
    private var yScale: Float? = null

    @JvmStatic
    val BLACK = color(0, 0, 0, 255)

    @JvmStatic
    val DARK_BLUE = color(0, 0, 190, 255)

    @JvmStatic
    val DARK_GREEN = color(0, 190, 0, 255)

    @JvmStatic
    val DARK_AQUA = color(0, 190, 190, 255)

    @JvmStatic
    val DARK_RED = color(190, 0, 0, 255)

    @JvmStatic
    val DARK_PURPLE = color(190, 0, 190, 255)

    @JvmStatic
    val GOLD = color(217, 163, 52, 255)

    @JvmStatic
    val GRAY = color(190, 190, 190, 255)

    @JvmStatic
    val DARK_GRAY = color(63, 63, 63, 255)

    @JvmStatic
    val BLUE = color(63, 63, 254, 255)

    @JvmStatic
    val GREEN = color(63, 254, 63, 255)

    @JvmStatic
    val AQUA = color(63, 254, 254, 255)

    @JvmStatic
    val RED = color(254, 63, 63, 255)

    @JvmStatic
    val LIGHT_PURPLE = color(254, 63, 254, 255)

    @JvmStatic
    val YELLOW = color(254, 254, 63, 255)

    @JvmStatic
    val WHITE = color(255, 255, 255, 255)

    @JvmStatic
    fun getColor(color: Int): Long {
        return when (color) {
            0 -> BLACK
            1 -> DARK_BLUE
            2 -> DARK_GREEN
            3 -> DARK_AQUA
            4 -> DARK_RED
            5 -> DARK_PURPLE
            6 -> GOLD
            7 -> GRAY
            8 -> DARK_GRAY
            9 -> BLUE
            10 -> GREEN
            11 -> AQUA
            12 -> RED
            13 -> LIGHT_PURPLE
            14 -> YELLOW
            else -> WHITE
        }
    }

    @JvmStatic
    fun getFontRenderer(): FontRenderer {
        //#if MC<=10809
        return Client.getMinecraft().fontRendererObj
        //#else
        //$$ return Client.getMinecraft().fontRenderer
        //#endif
    }

    @JvmStatic
    fun getRenderManager(): RenderManager {
        return Client.getMinecraft().renderManager
    }

    @JvmStatic
    fun getXScale() = xScale ?: 1f

    @JvmStatic
    fun getYScale() = yScale ?: 1f

    @JvmStatic
    @JvmOverloads
    fun getStringWidth(text: String, includeScale: Boolean = false): Int {
        if (!includeScale) return getFontRenderer().getStringWidth(ChatLib.addColor(text))
        return (getFontRenderer().getStringWidth(ChatLib.addColor(text)) * (xScale ?: 1f)).toInt()
    }

    @JvmStatic
    fun getStringHeight() = (getFontRenderer().FONT_HEIGHT * (yScale ?: 1f)).toInt()

    @JvmStatic
    @JvmOverloads
    fun color(red: Long, green: Long, blue: Long, alpha: Long = 255): Long {
        return (MathLib.clamp(alpha.toInt(), 0, 255) * 0x1000000
                + MathLib.clamp(red.toInt(), 0, 255) * 0x10000
                + MathLib.clamp(green.toInt(), 0, 255) * 0x100
                + MathLib.clamp(blue.toInt(), 0, 255)).toLong()
    }

    @JvmStatic
    @JvmOverloads
    fun getRainbow(step: Float, speed: Float = 1f): Long {
        val red = ((sin(step / speed) + 0.75) * 170).toLong()
        val green = ((sin(step / speed + 2 * PI / 3) + 0.75) * 170).toLong()
        val blue = ((sin(step / speed + 4 * PI / 3) + 0.75) * 170).toLong()
        return color(red, green, blue, 255)
    }

    @JvmStatic
    @JvmOverloads
    fun getRainbowColors(step: Float, speed: Float = 1f): IntArray {
        val red = ((sin(step / speed) + 0.75) * 170).toInt()
        val green = ((sin(step / speed + 2 * PI / 3) + 0.75) * 170).toInt()
        val blue = ((sin(step / speed + 4 * PI / 3) + 0.75) * 170).toInt()
        return intArrayOf(red, green, blue)
    }

    @JvmStatic
    fun retainTransforms(retain: Boolean) {
        retainTransforms = retain
        finishDraw()
    }

    @JvmStatic
    @JvmOverloads
    fun translate(x: Float, y: Float, z: Float = 0.0F) {
        GlStateManager.translate(x, y, z)
    }

    @JvmStatic
    fun scale(scaleX: Number, scaleY: Number) = apply {
        xScale = scaleX.toFloat(); yScale = scaleY.toFloat()
        GlStateManager.scale(xScale ?: 1f, yScale ?: 1f, 1f)
    }

    @JvmStatic
    fun scale(scale: Number) = apply {
        sameScale(scale)
        GlStateManager.scale(xScale ?: 1f, yScale ?: 1f, 1f)
    }

    @JvmStatic
    fun resetScale() = apply {
        xScale = null
        yScale = null
        GlStateManager.scale(1f, 1f, 1f)
    }

    @JvmStatic
    fun pushMatrix() = apply {
        GlStateManager.pushMatrix()
    }

    @JvmStatic
    fun popMatrix() = apply {
        GlStateManager.popMatrix()
        resetScale()
    }

    @JvmStatic
    fun rotate(angle: Float) {
        GlStateManager.rotate(angle, 0f, 0f, 1f)
    }

    @JvmStatic
    @JvmOverloads
    fun colorize(red: Float, green: Float, blue: Float, alpha: Float = 1f) {
        colorized = fixAlpha(color(red.toLong(), green.toLong(), blue.toLong(), alpha.toLong()))

        GlStateManager.color(
            MathLib.clampFloat(red, 0f, 1f),
            MathLib.clampFloat(green, 0f, 1f),
            MathLib.clampFloat(blue, 0f, 1f),
            MathLib.clampFloat(alpha, 0f, 1f)
        )
    }

    @JvmStatic
    fun setDrawMode(drawMode: Int) = apply {
        this.drawMode = drawMode
    }

    @JvmStatic
    fun getDrawMode() = drawMode

    @JvmStatic
    fun fixAlpha(color: Long): Long {
        val alpha = color shr 24 and 255
        return if (alpha < 10)
            (color and 0xFF_FF_FF) or 0xA_FF_FF_FF
        else color
    }

    @JvmStatic
    fun drawRect(color: Long, x: Float, y: Float, width: Float, height: Float) {
        val pos = mutableListOf(x, y, x + width, y + height)
        if (pos[0] > pos[2])
            Collections.swap(pos, 0, 2)
        if (pos[1] > pos[3])
            Collections.swap(pos, 1, 3)

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        doColor(color)

        worldRenderer.begin(drawMode ?: 7, DefaultVertexFormats.POSITION)
        worldRenderer.pos(pos[0].toDouble(), pos[3].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[2].toDouble(), pos[3].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[2].toDouble(), pos[1].toDouble(), 0.0).endVertex()
        worldRenderer.pos(pos[0].toDouble(), pos[1].toDouble(), 0.0).endVertex()

        tessellator.draw()

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        finishDraw()
    }

    @JvmStatic
    @JvmOverloads
    fun drawShape(color: Long, vararg vertexes: List<Float>, drawMode: Int = 9) {
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        doColor(color)

        worldRenderer.begin(this.drawMode ?: drawMode, DefaultVertexFormats.POSITION)

        if (area(vertexes) >= 0)
            vertexes.reverse()

        vertexes.forEach {
            worldRenderer.pos(it[0].toDouble(), it[1].toDouble(), 0.0).endVertex()
        }

        tessellator.draw()

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        finishDraw()
    }

    @JvmStatic
    @JvmOverloads
    fun drawLine(color: Long, x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, drawMode: Int = 7) {
        val theta = -atan2(y2 - y1, x2 - x1)
        val i = sin(theta) * (thickness / 2)
        val j = cos(theta) * (thickness / 2)

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        doColor(color)

        worldRenderer.begin(this.drawMode ?: drawMode, DefaultVertexFormats.POSITION)

        worldRenderer.pos((x1 + i).toDouble(), (y1 + j).toDouble(), 0.0).endVertex()
        worldRenderer.pos((x2 + i).toDouble(), (y2 + j).toDouble(), 0.0).endVertex()
        worldRenderer.pos((x2 - i).toDouble(), (y2 - j).toDouble(), 0.0).endVertex()
        worldRenderer.pos((x1 - i).toDouble(), (y1 - j).toDouble(), 0.0).endVertex()

        tessellator.draw()

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        finishDraw()
    }

    @JvmStatic
    @JvmOverloads
    fun drawCircle(color: Long, x: Float, y: Float, radius: Float, steps: Int, drawMode: Int = 5) {
        val theta = 2 * PI / steps
        val cos = cos(theta).toFloat()
        val sin = sin(theta).toFloat()

        var xHolder: Float
        var circleX = 1f
        var circleY = 0f

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        doColor(color)

        worldRenderer.begin(this.drawMode ?: drawMode, DefaultVertexFormats.POSITION)

        for (i in 0..steps) {
            worldRenderer.pos(x.toDouble(), y.toDouble(), 0.0).endVertex()
            worldRenderer.pos((circleX * radius + x).toDouble(), (circleY * radius + y).toDouble(), 0.0).endVertex()
            xHolder = circleX
            circleX = cos * circleX - sin * circleY
            circleY = sin * xHolder + cos * circleY
            worldRenderer.pos((circleX * radius + x).toDouble(), (circleY * radius + y).toDouble(), 0.0).endVertex()
        }

        tessellator.draw()

        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        finishDraw()
    }

    @JvmOverloads
    @JvmStatic
    fun drawString(text: String, x: Float, y: Float, shadow: Boolean = false) {
        val fr = getFontRenderer()
        var newY = y

        ChatLib.addColor(text).split("\n").forEach {
            fr.drawString(it, x, newY, colorized?.toInt() ?: WHITE.toInt(), shadow)

            newY += fr.FONT_HEIGHT
        }
        finishDraw()
    }

    @JvmStatic
    fun drawStringWithShadow(text: String, x: Float, y: Float) = drawString(text, x, y, true)

    @JvmStatic
    fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double) {
        if (colorized == null)
            GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.enableBlend()
        GlStateManager.scale(1f, 1f, 50f)
        GlStateManager.bindTexture(image.getTexture().glTextureId)
        GlStateManager.enableTexture2D()

        worldRenderer.begin(drawMode ?: 7, DefaultVertexFormats.POSITION_TEX)

        worldRenderer.pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex()
        worldRenderer.pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex()
        worldRenderer.pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex()
        worldRenderer.pos(x, y, 0.0).tex(0.0, 0.0).endVertex()
        tessellator.draw()

        finishDraw()
    }

    private val renderManager = getRenderManager()
    private val slimCTRenderPlayer = CTRenderPlayer(renderManager, true)
    private val normalCTRenderPlayer = CTRenderPlayer(renderManager, false)

    @JvmStatic
    @JvmOverloads
    fun drawPlayer(
        player: Any,
        x: Int,
        y: Int,
        rotate: Boolean = false,
        showNametag: Boolean = false,
        showArmor: Boolean = true,
        showCape: Boolean = true,
        showHeldItem: Boolean = true,
        showArrows: Boolean = true
    ) {
        val mouseX = -30f
        val mouseY = 0f

        val ent = if (player is PlayerMP) player.player else Player.getPlayer()!!

        GlStateManager.enableColorMaterial()
        RenderHelper.enableStandardItemLighting()

        val f = ent.renderYawOffset
        val f1 = ent.rotationYaw
        val f2 = ent.rotationPitch
        val f3 = ent.prevRotationYawHead
        val f4 = ent.rotationYawHead

        translate(x.toFloat(), y.toFloat(), 50.0f)
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f)
        GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-45.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f)
        scale(-1f, 1f)
        if (!rotate) {
            ent.renderYawOffset = atan(mouseX / 40.0f) * 20.0f
            ent.rotationYaw = atan(mouseX / 40.0f) * 40.0f
            ent.rotationPitch = -atan(mouseY / 40.0f) * 20.0f
            ent.rotationYawHead = ent.rotationYaw
            ent.prevRotationYawHead = ent.rotationYaw
        }

        renderManager.playerViewY = 180.0f
        renderManager.isRenderShadow = false
        //#if MC<=10809
        val isSmall = (ent as AbstractClientPlayer).skinType == "slim"
        val ctRenderPlayer = if (isSmall) slimCTRenderPlayer else normalCTRenderPlayer

        ctRenderPlayer.setOptions(showNametag, showArmor, showCape, showHeldItem, showArrows)
        ctRenderPlayer.doRender(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f)
        //#else
        //$$ renderManager.doRenderEntity(ent, 0.0, 0.0, 0.0, 0.0F, 1.0F, false)
        //#endif
        renderManager.isRenderShadow = true

        ent.renderYawOffset = f
        ent.rotationYaw = f1
        ent.rotationPitch = f2
        ent.prevRotationYawHead = f3
        ent.rotationYawHead = f4

        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
        GlStateManager.disableTexture2D()
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)

        finishDraw()
    }

    internal fun doColor(longColor: Long) {
        val color = longColor.toInt()

        if (colorized == null) {
            val a = (color shr 24 and 255).toFloat() / 255.0f
            val r = (color shr 16 and 255).toFloat() / 255.0f
            val g = (color shr 8 and 255).toFloat() / 255.0f
            val b = (color and 255).toFloat() / 255.0f
            GlStateManager.color(r, g, b, a)
        }
    }

    @JvmStatic
    fun finishDraw() {
        if (!retainTransforms) {
            colorized = null
            drawMode = null
            resetScale()
            GlStateManager.popMatrix()
            GlStateManager.pushMatrix()
        }
    }

    private fun area(points: Array<out List<Float>>): Float {
        var area = 0f

        for (i in points.indices) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[(i + 1) % points.size]

            area += x1 * y2 - x2 * y1
        }

        return area / 2
    }

    private fun sameScale(scale: Number) {
        xScale = scale.toFloat()
        yScale = scale.toFloat()
    }

    object screen {
        @JvmStatic
        fun getWidth(): Int = ScaledResolution(Client.getMinecraft()).scaledWidth

        @JvmStatic
        fun getHeight(): Int = ScaledResolution(Client.getMinecraft()).scaledHeight

        @JvmStatic
        fun getScale(): Int = ScaledResolution(Client.getMinecraft()).scaleFactor
    }
}
