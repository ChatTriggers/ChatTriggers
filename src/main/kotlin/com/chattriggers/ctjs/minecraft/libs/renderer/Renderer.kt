package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.MathLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.chattriggers.ctjs.utils.kotlin.getRenderer
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.UMinecraft
import gg.essential.universal.UResolution
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Vector3f
import java.awt.Color
import java.nio.FloatBuffer
import java.util.*
import kotlin.math.*

object Renderer {
    @JvmStatic
    var partialTicks = 0f
        internal set

    // TODO(BREAKING): This use to be a method called retainTransforms(),
    //     now it will just be a normal getter/setter. It also used to call
    //     finishDraw for some reason.
    private var retainTransforms = false

    // TODO(BREAKING): Was int, now an enum
    private var drawMode: DrawMode? = null

    private var vertexFormat: VertexFormat? = null

    private var vertexStarted = true
    private var began = false

    private var tessellator = MCTessellator.getInstance()
    private var worldRenderer = tessellator.getRenderer()

    private val renderManager = getRenderManager()
    private val slimCTRenderPlayer = CTRenderPlayer(renderManager, true)
    private val normalCTRenderPlayer = CTRenderPlayer(renderManager, false)

    private val colorBuffer = FloatBuffer.allocate(4)

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
    fun getFontRenderer() = UMinecraft.getFontRenderer()

    @JvmStatic
    fun getRenderManager(): RenderManager {
        //#if MC<=11202
        return Client.getMinecraft().renderManager
        //#else
        //$$ return Client.getMinecraft().gameRenderer
        //#endif
    }

    @JvmStatic
    fun getStringWidth(text: String) = getFontRenderer().getStringWidth(ChatLib.addColor(text))

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
    @JvmOverloads
    fun translate(x: Float, y: Float, z: Float = 0.0F) = apply {
        GlStateManager.translate(x, y, z)
    }

    // TODO(BREAKING): In the Tessellator, scaleZ defaulted to scaleX. Does setting it to
    //                 scaleX have any effect in 2D operations?
    @JvmStatic
    @JvmOverloads
    fun scale(scaleX: Float, scaleY: Float = scaleX, scaleZ: Float = 1f) = apply {
        GlStateManager.scale(scaleX, scaleY, scaleZ)
    }

    /**
     * Rotates the Renderer in 2D screen space
     */
    @JvmStatic
    fun rotate(angle: Float) = apply {
        GlStateManager.rotate(angle, 0f, 0f, 1f)
    }

    /**
     * Rotates the Renderer in 3D space
     */
    @JvmStatic
    fun rotate(angle: Float, x: Float, y: Float, z: Float) = apply {
        GlStateManager.rotate(angle, x, y, z)
    }

    @JvmStatic
    @JvmOverloads
    fun colorize(red: Float, green: Float, blue: Float, alpha: Float = 1f) = apply {
        GlStateManager.color(
            red.coerceIn(0f..1f),
            green.coerceIn(0f..1f),
            blue.coerceIn(0f..1f),
            alpha.coerceIn(0f..1f),
        )
    }

    @JvmStatic
    @JvmOverloads
    fun colorize(red: Int, green: Int, blue: Int, alpha: Int = 255) = apply {
        colorize(red.toFloat() / 255f, green.toFloat() / 255f, blue.toFloat() / 255f, alpha.toFloat() / 255)
    }

    @JvmStatic
    fun colorize(color: Color) = apply {
        colorize(color.red, color.green, color.blue, color.alpha)
    }

    @JvmStatic
    fun colorize(color: Long) = apply {
        colorize(
            (color shr 16 and 0xff).toFloat() / 255.0f,
            (color shr 8 and 0xff).toFloat() / 255.0f,
            (color and 0xff).toFloat() / 255.0f,
            (color shr 24 and 0xff).toFloat() / 255.0f,
        )
    }

    @JvmStatic
    fun <T> withColor(color: Long?, block: () -> T): T {
        if (color == null)
            return block()

        val old = getCurrentGlColor()
        colorize(color)
        return try {
            block()
        } finally {
            colorize(old)
        }
    }

    /**
     * Gets the current OpenGL color value
     */
    @JvmStatic
    fun getCurrentGlColor(): Color {
        // TODO(VERIFY)
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, colorBuffer)
        return Color(colorBuffer[0], colorBuffer[1], colorBuffer[2], colorBuffer[3])
    }

    /**
     * Gets the current OpenGL color value. Takes into account the alpha bug
     * (alphas less than 10 are bugged, so they are rounded up to 10)
     */
    @JvmStatic
    fun getCurrentGlColorAlphaFixed() = getCurrentGlColor().let {
        if (it.alpha < 10) it.withAlpha(10) else it
    }

    @JvmStatic
    fun fixAlpha(color: Long): Long {
        val alpha = color shr 24 and 255
        return if (alpha < 10)
            (color and 0xFF_FF_FF) or 0xA_FF_FF_FF
        else color
    }

    @JvmStatic
    fun disableAlpha() = apply {
        GlStateManager.disableAlpha()
    }

    @JvmStatic
    fun enableAlpha() = apply {
        GlStateManager.enableAlpha()
    }

    @JvmStatic
    fun alphaFunc(func: Int, ref: Float) = apply {
        GlStateManager.alphaFunc(func, ref)
    }

    @JvmStatic
    fun enableLighting() = apply {
        GlStateManager.enableLighting()
    }

    @JvmStatic
    fun disableLighting() = apply {
        GlStateManager.disableLighting()
    }

    @JvmStatic
    fun disableDepth() = apply {
        GlStateManager.disableDepth()
    }

    @JvmStatic
    fun enableDepth() = apply {
        GlStateManager.enableDepth()
    }

    @JvmStatic
    fun depthFunc(depthFunc: Int) = apply {
        GlStateManager.depthFunc(depthFunc)
    }

    @JvmStatic
    fun depthMask(flagIn: Boolean) = apply {
        GlStateManager.depthMask(flagIn)
    }

    @JvmStatic
    fun disableBlend() = apply {
        GlStateManager.disableBlend()
    }

    @JvmStatic
    fun enableBlend() = apply {
        GlStateManager.enableBlend()
    }

    @JvmStatic
    fun blendFunc(sourceFactor: Int, destFactor: Int) = apply {
        GlStateManager.blendFunc(sourceFactor, destFactor)
    }

    @JvmStatic
    fun tryBlendFuncSeparate(sourceFactor: Int, destFactor: Int, sourceFactorAlpha: Int, destFactorAlpha: Int) = apply {
        GlStateManager.tryBlendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha)
    }

    @JvmStatic
    fun enableTexture2D() = apply {
        GlStateManager.enableTexture2D()
    }

    @JvmStatic
    fun disableTexture2D() = apply {
        GlStateManager.disableTexture2D()
    }

    /**
     * Binds a texture to the client for the Tessellator to use.
     *
     * @param texture the texture to bind
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    fun bindTexture(texture: Image) = apply {
        GlStateManager.bindTexture(texture.getTexture().glTextureId)
    }

    @JvmStatic
    fun deleteTexture(texture: Image) = apply {
        GlStateManager.deleteTexture(texture.getTexture().glTextureId)
    }

    @JvmStatic
    fun pushMatrix() = apply {
        GlStateManager.pushMatrix()
    }

    @JvmStatic
    fun popMatrix() = apply {
        GlStateManager.popMatrix()
    }

    fun getRetainTransforms() = retainTransforms

    fun setRetainTransforms(retain: Boolean) = apply {
        retainTransforms = retain
    }

    fun getDrawMode() = drawMode

    fun setDrawMode(mode: DrawMode) = apply {
        drawMode = mode
    }

    fun getVertexFormat() = vertexFormat

    fun setVertexFormat(format: VertexFormat) = apply {
        vertexFormat = format
    }

    /**
     * Gets a fixed render position from x, y, and z inputs adjusted with partial ticks
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the Vector3f position to render at
     */
    @JvmStatic
    fun getRenderPos(x: Float, y: Float, z: Float): Vector3f {
        return Vector3f(
            x - Player.getRenderX().toFloat(),
            y - Player.getRenderY().toFloat(),
            z - Player.getRenderZ().toFloat()
        )
    }

    // TODO(BREAKING): Takes enum as first param, and VertexFormat and second (not boolean)
    // TODO(BREAKING): No longer translates the player's camera position (see translateCamera)
    // TODO(BREAKING): Second argument use to default to PositionTex in the Tessellator (now
    //                 defaults indirectly to Position)
    // TODO(BREAKING): Will throw if either parameter is not given and the corresponding Renderer
    //                 field is null
    @JvmStatic
    @JvmOverloads
    fun beginVertices(drawMode: DrawMode? = null, vertexFormat: VertexFormat? = null) = apply {
        pushMatrix()
        enableBlend()
        tryBlendFuncSeparate(770, 771, 1, 0)

        val theDrawMode = drawMode ?: this.drawMode ?: throw IllegalStateException(
            "Call to Renderer.beginVertices() without a drawMode argument or drawMode set via Renderer.setDrawMode()"
        )
        val theVertexFormat = vertexFormat ?: this.vertexFormat ?: throw IllegalStateException(
            "Call to Renderer.beginVertices() without a vertexFormat argument or vertexFormat set via Renderer.setVertexFormat()"
        )

        worldRenderer.begin(theDrawMode.toMCDrawMode(), theVertexFormat.mcVertexFormat)
        vertexStarted = false
        began = true
    }

    // TODO(BREAKING): Use to be called draw() in Tessellator
    @JvmStatic
    fun endVertices() {
        if (!began)
            throw IllegalStateException("Call to endVertices() without corresponding call to beginVertices()")

        if (vertexStarted)
            throw IllegalStateException("Call to endVertex() while in vertex. Did you forget to call endVertex()?")

        began = false
        tessellator.draw()
        disableBlend()
        popMatrix()

        finishDraw()
    }

    @JvmStatic
    fun finishDraw() {
        if (!retainTransforms) {
            drawMode = null
            vertexFormat = null
        }
    }

    @JvmStatic
    fun translateCamera() = apply {
        val renderManager = getRenderManager()
        translate(
            -renderManager.viewerPosX.toFloat(),
            -renderManager.viewerPosY.toFloat(),
            -renderManager.viewerPosZ.toFloat(),
        )
    }

    @JvmStatic
    @JvmOverloads
    fun pos(x: Number, y: Number, z: Number = 0.0) = apply {
        // TODO(BREAKING): This use to call begin() automatically
        if (!began)
            throw IllegalStateException("Call to Renderer.pos() without a corresponding call to Renderer.beginVertices()")

        // TODO(BREAKING): This use to call endVertex() automatically
        if (vertexStarted)
            throw IllegalStateException("Call to Renderer.pos() while in vertex. Did you forget to call Renderer.endVertex()?")

        worldRenderer.pos(x.toDouble(), y.toDouble(), z.toDouble())
        vertexStarted = true
    }

    @JvmStatic
    fun tex(u: Number, v: Number) = apply {
        // TODO(BREAKING): This check didn't use to be here
        if (!began)
            throw IllegalStateException("Call to Renderer.tex() without a corresponding call to Renderer.beginVertices()")

        worldRenderer.tex(u.toDouble(), v.toDouble())
    }

    /**
     * Calls WorldRenderer.color(). Named col to avoid confusion with colorize() and be
     * as close to pos() and tex() as possible.
     */
    @JvmStatic
    fun col(r: Float, g: Float, b: Float, a: Float) = apply {
        if (!began)
            throw IllegalStateException("Call to Renderer.col() without a corresponding call to Renderer.beginVertices()")

        worldRenderer.color(r, g, b, a)
    }

    @JvmStatic
    fun col(color: Color) = apply {
        col(
            color.red.toFloat() / 255f,
            color.green.toFloat() / 255f,
            color.blue.toFloat() / 255f,
            color.alpha.toFloat() / 255f,
        )
    }

    @JvmStatic
    fun endVertex() = apply {
        if (!vertexStarted)
            throw IllegalStateException("Call to endVertex() with no active vertex")
        vertexStarted = false
        worldRenderer.endVertex()
    }

    // TODO(BREAKING): Color arg moved to the last position (since it can be omitted in favor of colorize())
    // TODO(BREAKING): Unsure if this is breaking, but this function used to reset color to white
    //     for some reason (also applies to all draw helpers below)
    @JvmStatic
    @JvmOverloads
    fun drawRect(x: Float, y: Float, width: Float, height: Float, color: Long? = null) = apply {
        val pos = mutableListOf(x, y, x + width, y + height)
        if (pos[0] > pos[2])
            Collections.swap(pos, 0, 2)
        if (pos[1] > pos[3])
            Collections.swap(pos, 1, 3)

        withColor(color) {
            disableTexture2D()
            beginVertices(drawMode ?: DrawMode.Quads, vertexFormat ?: VertexFormat.Position)

            pos(pos[0], pos[3], 0f).endVertex()
            pos(pos[2], pos[3], 0f).endVertex()
            pos(pos[2], pos[1], 0f).endVertex()
            pos(pos[0], pos[1], 0f).endVertex()

            endVertices()
            enableTexture2D()
        }
    }

    @JvmStatic
    fun drawRect(x: Float, y: Float, width: Float, height: Float, color: Color) =
        drawRect(x, y, width, height, color.rgb.toLong())

    // TODO(BREAKING): Removed drawShape in favor of the more optimal Shape class

    // TODO(BREAKING): Color arg moved to the last position (since it can be omitted in favor of colorize())
    // TODO(BREAKING): Removed drawMode argument. Users should use setDrawMode()
    @JvmStatic
    @JvmOverloads
    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, color: Long? = null) = apply {
        val theta = -atan2(y2 - y1, x2 - x1)
        val i = sin(theta) * (thickness / 2)
        val j = cos(theta) * (thickness / 2)

        withColor(color) {
            disableTexture2D()
            beginVertices(drawMode ?: DrawMode.Quads, vertexFormat ?: VertexFormat.Position)

            pos((x1 + i).toDouble(), (y1 + j).toDouble(), 0.0).endVertex()
            pos((x2 + i).toDouble(), (y2 + j).toDouble(), 0.0).endVertex()
            pos((x2 - i).toDouble(), (y2 - j).toDouble(), 0.0).endVertex()
            pos((x1 - i).toDouble(), (y1 - j).toDouble(), 0.0).endVertex()

            endVertices()
            enableTexture2D()
        }
    }

    @JvmStatic
    fun drawLine(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, color: Color) =
        drawLine(x1, y1, x2, y2, thickness, color.rgb.toLong())

    // TODO(BREAKING): Color arg moved to the last position (since it can be omitted in favor of colorize())
    // TODO(BREAKING): Remove drawMode argument. Users should use setDrawMode()
    @JvmStatic
    @JvmOverloads
    fun drawCircle(x: Float, y: Float, radius: Float, steps: Int, color: Long? = null) = apply {
        val theta = 2 * PI / steps
        val cos = cos(theta).toFloat()
        val sin = sin(theta).toFloat()

        var xHolder: Float
        var circleX = 1f
        var circleY = 0f

        withColor(color) {
            disableTexture2D()
            beginVertices(drawMode ?: DrawMode.TriangleStrip, vertexFormat ?: VertexFormat.Position)

            for (i in 0..steps) {
                pos(x.toDouble(), y.toDouble(), 0.0).endVertex()
                pos((circleX * radius + x).toDouble(), (circleY * radius + y).toDouble(), 0.0).endVertex()
                xHolder = circleX
                circleX = cos * circleX - sin * circleY
                circleY = sin * xHolder + cos * circleY
                pos((circleX * radius + x).toDouble(), (circleY * radius + y).toDouble(), 0.0).endVertex()
            }

            endVertices()
            enableTexture2D()
        }
    }

    @JvmStatic
    fun drawCircle(x: Float, y: Float, radius: Float, steps: Int, color: Color) =
        drawCircle(x, y, radius, steps, color.rgb.toLong())

    @JvmOverloads
    @JvmStatic
    fun drawString(text: String, x: Float, y: Float, shadow: Boolean = false) = apply {
        val fr = getFontRenderer()
        var newY = y

        ChatLib.addColor(text).split("\n").forEach {
            fr.drawString(it, x, newY, getCurrentGlColorAlphaFixed().rgb, shadow)

            newY += fr.FONT_HEIGHT
        }

        finishDraw()
    }

    @JvmStatic
    fun drawStringWithShadow(text: String, x: Float, y: Float) = drawString(text, x, y, true)

    /**
     * Renders floating lines of text in the 3D world at a specific position.
     *
     * @param text The string array of text to render
     * @param x X coordinate in the game world
     * @param y Y coordinate in the game world
     * @param z Z coordinate in the game world
     * @param renderBlackBox render a pretty black border behind the text
     * @param scale the scale of the text
     * @param increase whether to scale the text up as the player moves away
     */
    // TODO(BREAKING): Rename from drawString (in Tessellator)
    // TODO(BREAKING): Remove color argument. Users should use colorize()
    // TODO(CONVERT): Is the scale argument necessary? Does scale() affect this function?
    @JvmStatic
    @JvmOverloads
    fun drawStringInWorld(
        text: String,
        x: Float,
        y: Float,
        z: Float,
        renderBlackBox: Boolean = true,
        scale: Float = 1f,
        increase: Boolean = true,
    ) {
        var lScale = scale

        val fontRenderer = getFontRenderer()

        val renderPos = getRenderPos(x, y, z)

        if (increase) {
            val distance = sqrt(renderPos.x * renderPos.x + renderPos.y * renderPos.y + renderPos.z * renderPos.z)
            val multiplier = distance / 120f //mobs only render ~120 blocks away
            lScale *= 0.45f * multiplier
        }

        val xMultiplier = if (renderManager.options.thirdPersonView == 2) -1 else 1

        val oldColor = getCurrentGlColorAlphaFixed()
        colorize(1f, 1f, 1f, 0.5f)
        pushMatrix()
        translate(renderPos.x, renderPos.y, renderPos.z)
        rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        rotate(renderManager.playerViewX * xMultiplier, 1.0f, 0.0f, 0.0f)
        scale(-lScale, -lScale, lScale)
        disableLighting()
        depthMask(false)
        disableDepth()
        enableBlend()
        // TODO(VERIFY)
        // blendFunc(770, 771)

        val textWidth = fontRenderer.getStringWidth(text)

        if (renderBlackBox) {
            val j = textWidth / 2
            disableTexture2D()
            beginVertices(drawMode ?: DrawMode.Quads, vertexFormat ?: VertexFormat.PositionColor)
            pos((-j - 1).toDouble(), (-1).toDouble(), 0.0).col(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            pos((-j - 1).toDouble(), 8.toDouble(), 0.0).col(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            pos((j + 1).toDouble(), 8.toDouble(), 0.0).col(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            pos((j + 1).toDouble(), (-1).toDouble(), 0.0).col(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            endVertices()
            enableTexture2D()
        }

        fontRenderer.drawString(text, -textWidth / 2, 0, getCurrentGlColorAlphaFixed().rgb)

        colorize(oldColor)
        depthMask(true)
        enableDepth()
        popMatrix()

    }

    // TODO(BREAKING): Doesn't set color to white if colorized() hasn't been called
    @JvmStatic
    fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double) = apply {
        GlStateManager.enableBlend()
        GlStateManager.scale(1f, 1f, 50f)
        GlStateManager.bindTexture(image.getTexture().glTextureId)
        GlStateManager.enableTexture2D()

        enableTexture2D()
        beginVertices(drawMode ?: DrawMode.Quads, vertexFormat ?: VertexFormat.PositionTex)

        pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex()
        pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex()
        pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex()
        pos(x, y, 0.0).tex(0.0, 0.0).endVertex()

        endVertices()
        enableTexture2D()
    }

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

        pushMatrix()
        enableTexture2D()
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
        disableTexture2D()
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)

        popMatrix()
        finishDraw()
    }

    fun shapeArea(points: Array<out List<Float>>): Float {
        var area = 0f

        for (i in points.indices) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[(i + 1) % points.size]

            area += x1 * y2 - x2 * y1
        }

        return area / 2
    }

    object screen {
        @JvmStatic
        fun getWidth(): Int = UResolution.scaledWidth

        @JvmStatic
        fun getHeight(): Int = UResolution.scaledHeight

        @JvmStatic
        fun getScale(): Double = UResolution.scaleFactor
    }

    //#if MC<=11202
    enum class DrawMode {
        Lines,
        LineStrip,
        DebugLines,
        DebugLineStrip,
        Triangles,
        TriangleStrip,
        TriangleFan,
        Quads;

        fun toMCDrawMode() =
            //#if MC<=11202
            ordinal
        //#else
        //$$ VertexFormat.Mode.values()[ordinal]
        //#endif
    }
    //#else
    //$$ TODO()
    //#endif

    enum class VertexFormat(val mcVertexFormat: net.minecraft.client.renderer.vertex.VertexFormat) {
        Block(DefaultVertexFormats.BLOCK),
        Item(DefaultVertexFormats.ITEM),
        Position(DefaultVertexFormats.POSITION),
        PositionColor(DefaultVertexFormats.POSITION_COLOR),
        PositionTex(DefaultVertexFormats.POSITION_TEX),
        PositionNormal(DefaultVertexFormats.POSITION_NORMAL),
        PositionTexColor(DefaultVertexFormats.POSITION_TEX_COLOR),
        PositionTexNormal(DefaultVertexFormats.POSITION_TEX_NORMAL),
        PositionTexLmapColor(DefaultVertexFormats.POSITION_TEX_LMAP_COLOR),
        PositionTexColorNormal(DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL),
    }
}