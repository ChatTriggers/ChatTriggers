package com.chattriggers.ctjs.minecraft.libs.renderer

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.EntityRenderDispatcherMixin
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.MathLib
import com.chattriggers.ctjs.minecraft.libs.Tessellator
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3f
import java.util.*
import kotlin.math.*

@External
object Renderer {
    internal lateinit var boundMatrixStack: MatrixStack

    var colorized: Long? = null
    private var retainTransforms = false
    private var drawMode: Int? = null

    private val tessellator = MCTessellator.getInstance()
    private val worldRenderer = Client.getMinecraft().worldRenderer

    private val slimPlayerRenderer: CTPlayerEntityRenderer
    private val normalPlayerRenderer: CTPlayerEntityRenderer

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

    init {
        val context = EntityRendererFactory.Context(
            Client.getMinecraft().entityRenderDispatcher,
            Client.getMinecraft().itemRenderer,
            Client.getMinecraft().resourceManager,
            Client.getMinecraft().entityModelLoader,
            Client.getMinecraft().textRenderer,
        )

        slimPlayerRenderer = CTPlayerEntityRenderer(context, true)
        normalPlayerRenderer = CTPlayerEntityRenderer(context, false)
    }

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
    fun getFontRenderer(): TextRenderer = Client.getMinecraft().textRenderer

    // TODO(BREAKING): Remove this
    // @JvmStatic
    // fun getRenderManager(): RenderManager {
    //     return Client.getMinecraft().renderManager
    // }

    @JvmStatic
    fun getStringWidth(text: String) = getFontRenderer().getWidth(ChatLib.addColor(text))

    @JvmStatic
    @JvmOverloads
    fun color(red: Long, green: Long, blue: Long, alpha: Long = 255): Long {
        return ((alpha.toInt().coerceIn(0..255) shl 24)
            or (red.toInt().coerceIn(0..255) shl 16)
            or (green.toInt().coerceIn(0..255) shl 8)
            or blue.toInt().coerceIn(0..255)).toLong()
    }

    @JvmStatic
    @JvmOverloads
    fun getRainbow(step: Float, speed: Float = 1f): Long {
        val red = ((sin((step / speed).toDouble()) + 0.75) * 170).toLong()
        val green = ((sin(step / speed + 2 * PI / 3) + 0.75) * 170).toLong()
        val blue = ((sin(step / speed + 4 * PI / 3) + 0.75) * 170).toLong()
        return color(red, green, blue, 255)
    }

    @JvmStatic
    @JvmOverloads
    fun getRainbowColors(step: Float, speed: Float = 1f): IntArray {
        val red = ((sin((step / speed).toDouble()) + 0.75) * 170).toInt()
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
    fun translate(x: Double, y: Double, z: Double = 0.0) {
        boundMatrixStack.translate(x, y, z)
    }

    @JvmStatic
    @JvmOverloads
    fun scale(scaleX: Float, scaleY: Float = scaleX, scaleZ: Float = 1f) {
        boundMatrixStack.scale(scaleX, scaleY, scaleZ)
    }

    @JvmStatic
    fun rotate(angle: Float) {
        // TODO("fabric"): Verify this works properly
        boundMatrixStack.multiply(Quaternion(angle, 0f, 0f, 1f))
    }

    @JvmStatic
    @JvmOverloads
    fun colorize(red: Int, green: Int, blue: Int, alpha: Int = 255) {
        colorize(red.toFloat() / 255f, green.toFloat() / 255f, blue.toFloat() / 255f, alpha.toFloat() / 255f)
    }

    @JvmStatic
    @JvmOverloads
    fun colorize(red: Float, green: Float, blue: Float, alpha: Float = 1f) {
        colorized = fixAlpha(color(red.toLong(), green.toLong(), blue.toLong(), alpha.toLong()))

        RenderSystem.setShaderColor(
            red.coerceIn(0f..1f),
            green.coerceIn(0f..1f),
            blue.coerceIn(0f..1f),
            alpha.coerceIn(0f..1f),
        )
    }

    @JvmStatic
    fun colorize(color: Int) = colorize(
        (color shr 16) and 0xff,
        (color shr 8) and 0xff,
        color and 0xff,
        (color shr 24) and 0xff,
    )

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

        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.blendFuncSeparate(770, 771, 1, 0)
        doColor(color)

        val buffer = MCTessellator.getInstance().buffer
        buffer.begin(VertexFormat.DrawMode.values()[drawMode ?: 7], VertexFormats.POSITION)
        buffer.vertex(pos[0].toDouble(), pos[3].toDouble(), 0.0).next()
        buffer.vertex(pos[2].toDouble(), pos[3].toDouble(), 0.0).next()
        buffer.vertex(pos[2].toDouble(), pos[1].toDouble(), 0.0).next()
        buffer.vertex(pos[0].toDouble(), pos[1].toDouble(), 0.0).next()
        buffer.end()

        tessellator.draw()

        colorize(1f, 1f, 1f, 1f)
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()

        finishDraw()
    }

    @JvmStatic
    @JvmOverloads
    fun drawShape(color: Long, vararg vertexes: List<Float>, drawMode: Int = 7) {
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.blendFuncSeparate(770, 771, 1, 0)
        doColor(color)

        val buffer = MCTessellator.getInstance().buffer
        buffer.begin(VertexFormat.DrawMode.values()[this.drawMode ?: drawMode], VertexFormats.POSITION)

        vertexes.forEach {
            if (it.size == 2) {
                buffer.vertex(it[0].toDouble(), it[1].toDouble(), 0.0).next()
            }
        }

        buffer.end()
        tessellator.draw()

        colorize(1f, 1f, 1f, 1f)
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()

        finishDraw()
    }

    @JvmStatic
    @JvmOverloads
    fun drawLine(color: Long, x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, drawMode: Int = 9) {
        val theta = -atan2((y2 - y1).toDouble(), (x2 - x1).toDouble())
        val i = sin(theta).toFloat() * (thickness / 2)
        val j = cos(theta).toFloat() * (thickness / 2)

        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.blendFuncSeparate(770, 771, 1, 0)
        doColor(color)

        val buffer = MCTessellator.getInstance().buffer
        buffer.begin(VertexFormat.DrawMode.values()[this.drawMode ?: drawMode], VertexFormats.POSITION)
        buffer.vertex((x1 + i).toDouble(), (y1 + j).toDouble(), 0.0).next()
        buffer.vertex((x2 + i).toDouble(), (y2 + j).toDouble(), 0.0).next()
        buffer.vertex((x2 - i).toDouble(), (y2 - j).toDouble(), 0.0).next()
        buffer.vertex((x1 - i).toDouble(), (y1 - j).toDouble(), 0.0).next()
        buffer.end()

        tessellator.draw()

        colorize(1f, 1f, 1f, 1f)
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()

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

        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.blendFuncSeparate(770, 771, 1, 0)
        doColor(color)

        val buffer = MCTessellator.getInstance().buffer
        buffer.begin(VertexFormat.DrawMode.values()[this.drawMode ?: drawMode], VertexFormats.POSITION)

        for (i in 0..steps) {
            buffer.vertex(x.toDouble(), y.toDouble(), 0.0).next()
            buffer.vertex((circleX * radius + x).toDouble(), (circleY * radius + y).toDouble(), 0.0).next()
            xHolder = circleX
            circleX = cos * circleX - sin * circleY
            circleY = sin * xHolder + cos * circleY
            buffer.vertex((circleX * radius + x).toDouble(), (circleY * radius + y).toDouble(), 0.0).next()
        }

        buffer.end()
        tessellator.draw()

        colorize(1f, 1f, 1f, 1f)
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()

        finishDraw()
    }

    fun drawString(text: String, x: Float, y: Float, shadow: Boolean) {
        if (shadow) {
            drawStringWithShadow(text, x, y)
        } else {
            drawString(text, x, y)
        }
    }

    @JvmStatic
    fun drawString(text: String, x: Float, y: Float) {
        val fr = getFontRenderer()
        var newY = y

        ChatLib.addColor(text).split("\n").forEach {
            fr.draw(boundMatrixStack, it, x, newY, colorized?.toInt() ?: WHITE.toInt())
            newY += fr.fontHeight
        }
        finishDraw()
    }

    @JvmStatic
    fun drawStringWithShadow(text: String, x: Float, y: Float) {
        getFontRenderer().drawWithShadow(boundMatrixStack, ChatLib.addColor(text), x, y, colorized?.toInt() ?: 0xffffffff.toInt())
        finishDraw()
    }

    @JvmStatic
    fun drawImage(image: Image, x: Double, y: Double, width: Double, height: Double) {
        if (colorized == null)
            colorize(1f, 1f, 1f, 1f)
        RenderSystem.enableBlend()
        boundMatrixStack.scale(1f, 1f, 50f)
        RenderSystem.bindTexture(image.getTexture().glTextureId)
        RenderSystem.enableTexture()

        val buffer = MCTessellator.getInstance().buffer
        buffer.begin(VertexFormat.DrawMode.values()[drawMode ?: 7], VertexFormats.POSITION_TEXTURE)
        buffer.vertex(x, y + height, 0.0).texture(0f, 1f).next()
        buffer.vertex(x + width, y + height, 0.0).texture(1f, 1f).next()
        buffer.vertex(x + width, y, 0.0).texture(1f, 0f).next()
        buffer.vertex(x, y, 0.0).texture(0f, 0f).next()
        buffer.end()
        tessellator.draw()

        finishDraw()
    }

    // TODO(BREAKING): Takes PlayerEntity instead of Any
    @JvmStatic
    @JvmOverloads
    fun drawEntity(
        entity: AbstractClientPlayerEntity,
        x: Int,
        y: Int,
        rotate: Boolean = false,
        showNametag: Boolean = false,
        showArmor: Boolean = true,
        showCape: Boolean = true,
        showHeldItem: Boolean = true,
        showArrows: Boolean = true
    ) {
        val size = 40
        val mouseX = -30f
        val mouseY = 0

        // Taken from InventoryScreen::drawEntity
        val f = atan((mouseX / 40.0f).toDouble()).toFloat()
        val g = atan((mouseY / 40.0f).toDouble()).toFloat()
        val matrixStack = RenderSystem.getModelViewStack()
        matrixStack.push()
        matrixStack.translate(x.toDouble(), y.toDouble(), 1050.0)
        matrixStack.scale(1.0f, 1.0f, -1.0f)
        RenderSystem.applyModelViewMatrix()
        val matrixStack2 = MatrixStack()
        matrixStack2.translate(0.0, 0.0, 1000.0)
        matrixStack2.scale(size.toFloat(), size.toFloat(), size.toFloat())
        val quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f)
        val quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0f)
        quaternion.hamiltonProduct(quaternion2)
        matrixStack2.multiply(quaternion)
        val h = entity.bodyYaw
        val i = entity.yaw
        val j = entity.pitch
        val k = entity.prevHeadYaw
        val l = entity.headYaw
        entity.bodyYaw = 180.0f + f * 20.0f
        entity.yaw = 180.0f + f * 40.0f
        entity.pitch = -g * 20.0f
        entity.headYaw = entity.yaw
        entity.prevHeadYaw = entity.yaw
        DiffuseLighting.method_34742()
        val entityRenderDispatcher = MinecraftClient.getInstance().entityRenderDispatcher
        quaternion2.conjugate()
        entityRenderDispatcher.rotation = quaternion2
        entityRenderDispatcher.setRenderShadows(false)
        val immediate = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
        RenderSystem.runAsFancy {
            val dispatcher = Client.getMinecraft().entityRenderDispatcher.asMixin<EntityRenderDispatcherMixin>()
            dispatcher.customEntityRenderer = if (entity.model == "slim") slimPlayerRenderer else normalPlayerRenderer
            Client.getMinecraft().entityRenderDispatcher.render(
                entity, 0.0, 0.0, 0.0, 0f, 1f, matrixStack2, immediate, 15278880,
            )
        }
        immediate.draw()
        entityRenderDispatcher.setRenderShadows(true)
        entity.bodyYaw = h
        entity.yaw = i
        entity.pitch = j
        entity.prevHeadYaw = k
        entity.headYaw = l
        matrixStack.pop()
        RenderSystem.applyModelViewMatrix()
        DiffuseLighting.enableGuiDepthLighting()
    }

    private fun doColor(longColor: Long) {
        val color = longColor.toInt()

        if (colorized == null) {
            val a = (color shr 24 and 255).toFloat() / 255.0f
            val r = (color shr 16 and 255).toFloat() / 255.0f
            val g = (color shr 8 and 255).toFloat() / 255.0f
            val b = (color and 255).toFloat() / 255.0f
            RenderSystem.setShaderColor(r, g, b, a)
        }
    }

    @JvmStatic
    fun finishDraw() {
        if (!retainTransforms) {
            colorized = null
            drawMode = null
            boundMatrixStack.pop()
            boundMatrixStack.push()
        }
    }

    object screen {
        @JvmStatic
        fun getWidth(): Int = Client.getMinecraft().window.scaledWidth

        @JvmStatic
        fun getHeight(): Int = Client.getMinecraft().window.scaledHeight

        @JvmStatic
        fun getScale(): Double = Client.getMinecraft().window.scaleFactor
    }
}
