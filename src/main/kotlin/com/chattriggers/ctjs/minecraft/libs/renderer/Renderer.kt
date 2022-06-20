package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.MathLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP
import com.chattriggers.ctjs.utils.kotlin.getRenderer
import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UResolution
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Vector3f
import java.awt.Color
import java.util.*
import kotlin.math.*

//#if MC<=11202
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
//#else
//$$ import com.chattriggers.ctjs.launch.mixins.transformers.entity.AbstractClientPlayerAccessor
//$$ import com.chattriggers.ctjs.utils.kotlin.asMixin
//$$ import com.mojang.blaze3d.platform.Lighting
//$$ import com.mojang.blaze3d.vertex.PoseStack
//$$ import com.mojang.blaze3d.systems.RenderSystem
//$$ import com.mojang.math.Quaternion
//$$ import net.minecraft.client.CameraType
//$$ import net.minecraft.client.renderer.entity.EntityRendererProvider
//#endif

object Renderer {
    val matrixStack = UMatrixStack()

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

    private var tessellator = Tessellator.getInstance()
    private var worldRenderer = tessellator.getRenderer()

    private val colorBuffer = BufferUtils.createFloatBuffer(16)

    //#if MC<=11202
    private val renderManager = getRenderManager()
    private val slimCTRenderPlayer = CTRenderPlayer(renderManager, true)
    private val normalCTRenderPlayer = CTRenderPlayer(renderManager, false)
    //#else
    //$$ private lateinit var slimCTRenderPlayer: CTRenderPlayer
    //$$ private lateinit var normalCTRenderPlayer: CTRenderPlayer
    //$$
    //$$ @JvmStatic
    //$$ internal fun initializeRenderPlayers(context: EntityRendererProvider.Context) {
    //$$     slimCTRenderPlayer = CTRenderPlayer(context, true)
    //$$     normalCTRenderPlayer = CTRenderPlayer(context, false)
    //$$ }
    //#endif

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
        //#if MC<=11202
        return Client.getMinecraft().fontRendererObj
        //#elseif MC>=11701
        //$$ return Client.getMinecraft().font
        //#endif
    }

    @JvmStatic
    fun getRenderManager(): RenderManager {
        //#if MC<=11202
        return Client.getMinecraft().renderManager
        //#else
        //$$ return Client.getMinecraft().gameRenderer
        //#endif
    }

    @JvmStatic
    fun getStringWidth(text: String) = UGraphics.getStringWidth(ChatLib.addColor(text))

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
        //#if MC<=11202
        GlStateManager.translate(x, y, z)
        //#else
        //$$ matrixStack.translate(x.toDouble(), y.toDouble(), z.toDouble())
        //#endif
    }

    // TODO(BREAKING): In the Tessellator, scaleZ defaulted to scaleX. Does setting it to
    //                 scaleX have any effect in 2D operations?
    @JvmStatic
    @JvmOverloads
    fun scale(scaleX: Float, scaleY: Float = scaleX, scaleZ: Float = 1f) = apply {
        //#if MC<=11202
        GlStateManager.scale(scaleX, scaleY, scaleZ)
        //#else
        //$$ matrixStack.scale(scaleX.toDouble(), scaleY.toDouble(), scaleZ.toDouble())
        //#endif
    }

    /**
     * Rotates the Renderer in 2D screen space
     */
    @JvmStatic
    fun rotate(angle: Float) = rotate(angle, 0f, 0f, 1f)

    /**
     * Rotates the Renderer in 3D space
     */
    @JvmStatic
    fun rotate(angle: Float, x: Float, y: Float, z: Float) = apply {
        //#if MC<=11202
        GlStateManager.rotate(angle, x, y, z)
        //#else
        //$$ matrixStack.rotate(angle, x, y, z)
        //#endif
    }

    @JvmStatic
    @JvmOverloads
    fun colorize(red: Float, green: Float, blue: Float, alpha: Float = 1f) = apply {
        colorBuffer.clear()
        colorBuffer.put(red.coerceIn(0f..1f))
        colorBuffer.put(green.coerceIn(0f..1f))
        colorBuffer.put(blue.coerceIn(0f..1f))
        colorBuffer.put(alpha.coerceIn(0f..1f))
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
        //#if MC<=11202
        GL11.glGetFloat(GL11.GL_CURRENT_COLOR, colorBuffer)
        //#else
        //$$ GL11.glGetFloatv(GL11.GL_CURRENT_COLOR, colorBuffer)
        //#endif
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
        UGraphics.disableAlpha()
    }

    @JvmStatic
    fun enableAlpha() = apply {
        UGraphics.enableAlpha()
    }

    @JvmStatic
    fun alphaFunc(func: Int, ref: Float) = apply {
        // TODO(CONVERT)
        //#if MC<=11202
        GlStateManager.alphaFunc(func, ref)
        //#endif
    }

    @JvmStatic
    fun enableLighting() = apply {
        UGraphics.enableLighting()
    }

    @JvmStatic
    fun disableLighting() = apply {
        UGraphics.disableLighting()
    }

    @JvmStatic
    fun disableDepth() = apply {
        UGraphics.disableDepth()
    }

    @JvmStatic
    fun enableDepth() = apply {
        UGraphics.enableDepth()
    }

    @JvmStatic
    fun depthFunc(depthFunc: Int) = apply {
        UGraphics.depthFunc(depthFunc)
    }

    @JvmStatic
    fun depthMask(flagIn: Boolean) = apply {
        UGraphics.depthMask(flagIn)
    }

    @JvmStatic
    fun disableBlend() = apply {
        UGraphics.disableBlend()
    }

    @JvmStatic
    fun enableBlend() = apply {
        UGraphics.enableBlend()
    }

    @JvmStatic
    fun blendFunc(sourceFactor: Int, destFactor: Int) = apply {
        //#if MC<=11202
        GlStateManager.blendFunc(sourceFactor, destFactor)
        //#else
        //$$ RenderSystem.blendFunc(sourceFactor, destFactor)
        //#endif
    }

    @JvmStatic
    fun tryBlendFuncSeparate(sourceFactor: Int, destFactor: Int, sourceFactorAlpha: Int, destFactorAlpha: Int) = apply {
        UGraphics.tryBlendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha)
    }

    @JvmStatic
    fun enableTexture2D() = apply {
        UGraphics.enableTexture2D()
    }

    @JvmStatic
    fun disableTexture2D() = apply {
        UGraphics.disableTexture2D()
    }

    /**
     * Binds a texture to the client for the Tessellator to use.
     *
     * @param texture the texture to bind
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    fun bindTexture(texture: Image) = apply {
        UGraphics.bindTexture(texture.getTextureId())
    }

    @JvmStatic
    fun deleteTexture(texture: Image) = apply {
        UGraphics.deleteTexture(texture.getTextureId())
    }

    @JvmStatic
    fun pushMatrix() = apply {
        //#if MC<=11202
        GlStateManager.pushMatrix()
        //#else
        //$$ matrixStack.push()
        //#endif
    }

    @JvmStatic
    fun popMatrix() = apply {
        //#if MC<=11202
        GlStateManager.popMatrix()
        //#else
        //$$ matrixStack.pop()
        //#endif
    }

    @JvmStatic
    fun getRetainTransforms() = retainTransforms

    @JvmStatic
    fun setRetainTransforms(retain: Boolean) = apply {
        retainTransforms = retain
    }

    @JvmStatic
    fun getDrawMode() = drawMode

    @JvmStatic
    fun setDrawMode(mode: DrawMode) = apply {
        drawMode = mode
    }

    @JvmStatic
    fun getVertexFormat() = vertexFormat

    @JvmStatic
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
        //#if MC<=11202
        tessellator.draw()
        //#else
        //$$ tessellator.end()
        //#endif
        disableBlend()
        popMatrix()

        finishDraw()
    }

    @JvmStatic
    fun finishDraw() {
        if (!retainTransforms) {
            colorBuffer.clear()
            drawMode = null
            vertexFormat = null
        }
    }

    @JvmStatic
    fun translateCamera() = apply {
        val renderManager = getRenderManager()
        translate(
            //#if MC<=11202
            -renderManager.viewerPosX.toFloat(),
            -renderManager.viewerPosY.toFloat(),
            -renderManager.viewerPosZ.toFloat(),
            //#else
            //$$ renderManager.mainCamera.position.x.toFloat(),
            //$$ renderManager.mainCamera.position.y.toFloat(),
            //$$ renderManager.mainCamera.position.z.toFloat(),
            //$$
            //$$
            //#endif
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

        //#if MC<=11202
        worldRenderer.pos(x.toDouble(), y.toDouble(), z.toDouble())
        //#else
        //$$ worldRenderer.vertex(x.toDouble(), y.toDouble(), z.toDouble())
        //#endif
        vertexStarted = true
    }

    @JvmStatic
    fun tex(u: Number, v: Number) = apply {
        // TODO(BREAKING): This check didn't use to be here
        if (!began)
            throw IllegalStateException("Call to Renderer.tex() without a corresponding call to Renderer.beginVertices()")

        //#if MC<=11202
        worldRenderer.tex(u.toDouble(), v.toDouble())
        //#else
        //$$ worldRenderer.uv(u.toFloat(), v.toFloat())
        //#endif
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
    fun drawString(text: String, x: Float, y: Float, shadow: Boolean = false, color: Long? = null) = apply {
        val fr = getFontRenderer()
        var newY = y

        ChatLib.addColor(text).split("\n").forEach {
            //#if MC<=11202
            fr.drawString(it, x, newY, color?.toInt() ?: getCurrentGlColorAlphaFixed().rgb, shadow)
            newY += fr.FONT_HEIGHT
            //#else
            //$$ if (shadow) {
            //$$     fr.drawShadow(matrixStack.toMC(), it, x, newY, color?.toInt() ?: getCurrentGlColorAlphaFixed().rgb)
            //$$ } else {
            //$$     fr.draw(matrixStack.toMC(), it, x, newY, color?.toInt() ?: getCurrentGlColorAlphaFixed().rgb)
            //$$ }
            //$$
            //$$ newY += fr.lineHeight
            //#endif
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
        val renderPos = getRenderPos(x, y, z)

        if (increase) {
            //#if MC<=11202
            val distance = sqrt(renderPos.x * renderPos.x + renderPos.y * renderPos.y + renderPos.z * renderPos.z)
            //#else
            //$$ val distance = sqrt(renderPos.x() * renderPos.x() + renderPos.y() * renderPos.y() + renderPos.z() * renderPos.z())
            //#endif
            val multiplier = distance / 120f //mobs only render ~120 blocks away
            lScale *= 0.45f * multiplier
        }

        //#if MC<=11202
        val xMultiplier = if (Client.settings.getSettings().thirdPersonView == 2) -1 else 1
        //#else
        //$$ val xMultiplier = if (Client.settings.getSettings().cameraType == CameraType.THIRD_PERSON_FRONT) -1 else 1
        //#endif

        val oldColor = getCurrentGlColorAlphaFixed()
        colorize(1f, 1f, 1f, 0.5f)
        pushMatrix()
        //#if MC<=11202
        translate(renderPos.x, renderPos.y, renderPos.z)
        //#else
        //$$ translate(renderPos.x(), renderPos.y(), renderPos.z())
        //#endif

        // TODO(CONVERT): See InventoryScreen::renderEntityInInventory (line 138???)
        //#if MC<=11202
        rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        rotate(renderManager.playerViewX * xMultiplier, 1.0f, 0.0f, 0.0f)
        //#endif

        scale(-lScale, -lScale, lScale)
        disableLighting()
        depthMask(false)
        disableDepth()
        enableBlend()
        // TODO(VERIFY)
        // blendFunc(770, 771)

        val textWidth = getStringWidth(text)

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

        drawString(text, -textWidth / 2f, 0f)

        colorize(oldColor)
        depthMask(true)
        enableDepth()
        popMatrix()

    }

    // TODO(BREAKING): Doesn't set color to white if colorized() hasn't been called
    @JvmStatic
    fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double) = apply {
        scale(1f, 1f, 50f)
        bindTexture(image)
        enableTexture2D()

        beginVertices(drawMode ?: DrawMode.Quads, vertexFormat ?: VertexFormat.PositionTex)

        pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex()
        pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex()
        pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex()
        pos(x, y, 0.0).tex(0.0, 0.0).endVertex()

        endVertices()
        enableTexture2D()
    }

    // TODO(VERIFY)
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
        showArrows: Boolean = true,
        // TODO(CONVERT) Add the rest of the layers as options in an object.
        //#if MC>=11701
        //$$ showParrot: Boolean,
        //$$ showSpinAttack: Boolean,
        //$$ showBeeStinger: Boolean,
        //#endif
    ) {
        val mouseX = -30f
        val mouseY = 0f

        //#if MC<=11202
        val entity = if (player is PlayerMP) player.player else Player.getPlayer()!!

        pushMatrix()
        enableTexture2D()
        GlStateManager.enableColorMaterial()
        RenderHelper.enableStandardItemLighting()

        val yawOffset = entity.renderYawOffset
        val yaw = entity.rotationYaw
        val pitch = entity.rotationPitch
        val prevYawHead = entity.prevRotationYawHead
        val yawHead = entity.rotationYawHead

        translate(x.toFloat(), y.toFloat(), 50.0f)
        rotate(180.0f, 0.0f, 0.0f, 1.0f)
        rotate(45.0f, 0.0f, 1.0f, 0.0f)
        rotate(-45.0f, 0.0f, 1.0f, 0.0f)
        rotate(-atan(mouseY / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f)
        scale(-1f, 1f)
        if (!rotate) {
            entity.renderYawOffset = atan(mouseX / 40.0f) * 20.0f
            entity.rotationYaw = atan(mouseX / 40.0f) * 40.0f
            entity.rotationPitch = -atan(mouseY / 40.0f) * 20.0f
            entity.rotationYawHead = entity.rotationYaw
            entity.prevRotationYawHead = entity.rotationYaw
        }

        renderManager.playerViewY = 180.0f
        renderManager.isRenderShadow = false
        val isSmall = (entity as AbstractClientPlayer).skinType == "slim"
        val ctRenderPlayer = if (isSmall) slimCTRenderPlayer else normalCTRenderPlayer

        ctRenderPlayer.setOptions(showNametag, showArmor, showCape, showHeldItem, showArrows)
        ctRenderPlayer.doRender(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f)
        renderManager.isRenderShadow = true

        entity.renderYawOffset = yawOffset
        entity.rotationYaw = yaw
        entity.rotationPitch = pitch
        entity.prevRotationYawHead = prevYawHead
        entity.rotationYawHead = yawHead

        RenderHelper.disableStandardItemLighting()

        GlStateManager.disableRescaleNormal()
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
        disableTexture2D()
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)

        popMatrix()
        finishDraw()
        //#else
        //$$ val entity = ((player as? PlayerMP)?.player as? AbstractClientPlayer) ?: Player.getPlayer()!!
        //$$
        //$$ val newYaw = atan(mouseY / 40.0).toFloat()
        //$$ val newPitch = atan(mouseX / 40.0).toFloat()
        //$$
        //$$ val stack = RenderSystem.getModelViewStack()
        //$$ stack.pushPose()
        //$$ stack.translate(x.toDouble(), y.toDouble(), 1050.0)
        //$$ stack.scale(1.0f, 1.0f, -1.0f)
        //$$ RenderSystem.applyModelViewMatrix()
        //$$ val newStack = PoseStack()
        //$$ newStack.translate(0.0, 0.0, 1000.0)
        //$$ newStack.scale(-1f, 1f, 1f)
        //$$
        //$$ val zRot = Vector3f.ZP.rotationDegrees(180.0f)
        //$$ val xRot = Vector3f.XP.rotationDegrees(newPitch * 20.0f)
        //$$ zRot.mul(xRot)
        //$$ newStack.mulPose(zRot)
        //$$
        //$$ val yawOffset = entity.yBodyRot
        //$$ val yaw = entity.yRot
        //$$ val pitch = entity.xRot
        //$$ val prevYawHead = entity.yHeadRotO
        //$$ val yawHead = entity.yHeadRot
        //$$
        //$$ if (!rotate) {
        //$$     entity.yBodyRot = 180.0f + newYaw * 20.0f
        //$$     entity.yRot = 180.0f + newYaw * 40.0f
        //$$     entity.xRot = -newPitch * 20.0f
        //$$     entity.yHeadRot = entity.yRot
        //$$     entity.yHeadRotO = entity.yRot
        //$$ }
        //$$
        //$$ Lighting.setupForEntityInInventory()
        //$$
        //$$ val entityRenderDispatcher = Client.getMinecraft().entityRenderDispatcher
        //$$ xRot.conj()
        //$$ entityRenderDispatcher.overrideCameraOrientation(xRot)
        //$$ entityRenderDispatcher.setRenderShadow(false)
        //$$ val lv6 = Client.getMinecraft().renderBuffers().bufferSource()
        //$$
        //$$ val isSmall = entity.asMixin<AbstractClientPlayerAccessor>().playerInfo.modelName == "slim"
        //$$ val ctRenderPlayer = if (isSmall) slimCTRenderPlayer else normalCTRenderPlayer
        //$$
        //$$ ctRenderPlayer.setOptions(showNametag, showArmor, showCape, showHeldItem, showArrows, showParrot, showSpinAttack, showBeeStinger)
        //$$ ctRenderPlayer.render(entity, 0f, 0f, newStack, lv6, 0xf000f0)
        //$$
        //$$ lv6.endBatch()
        //$$ entityRenderDispatcher.setRenderShadow(true)
        //$$
        //$$ entity.yBodyRot = yawOffset
        //$$ entity.yRot = yaw
        //$$ entity.xRot = pitch
        //$$ entity.yHeadRotO = prevYawHead
        //$$ entity.yHeadRot = yawHead
        //$$
        //$$ stack.popPose()
        //$$ RenderSystem.applyModelViewMatrix()
        //$$ Lighting.setupFor3DItems()
        //#endif
    }

    object screen {
        @JvmStatic
        fun getWidth(): Int = UResolution.scaledWidth

        @JvmStatic
        fun getHeight(): Int = UResolution.scaledHeight

        @JvmStatic
        fun getScale(): Double = UResolution.scaleFactor
    }

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
            //$$ com.mojang.blaze3d.vertex.VertexFormat.Mode.values()[ordinal]
            //#endif
    }

    enum class VertexFormat(val mcVertexFormat: net.minecraft.client.renderer.vertex.VertexFormat) {
        Block(DefaultVertexFormats.BLOCK),

        //#if MC<=11202
        Particle(DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP),
        //#else
        //$$ Particle(DefaultVertexFormat.PARTICLE),
        //#endif
        Position(DefaultVertexFormats.POSITION),
        PositionColor(DefaultVertexFormats.POSITION_COLOR),
        PositionTex(DefaultVertexFormats.POSITION_TEX),
        PositionTexColor(DefaultVertexFormats.POSITION_TEX_COLOR),
        PositionTexColorNormal(DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL),
    }
}