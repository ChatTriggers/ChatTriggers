package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.minecraft.libs.renderer.Image
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.mojang.blaze3d.systems.RenderSystem
import gg.essential.elementa.utils.Vector3f
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3f
import kotlin.math.sqrt

@External
object Tessellator {
    @JvmStatic
    lateinit var boundMatrixStack: MatrixStack

    @JvmStatic
    var partialTicks = 0f
        internal set

    private var buffer = MCTessellator.getInstance().buffer

    private var firstVertex = true
    private var began = false
    private var colorized = false

    // TODO(BREAKING: Remove this
    // @JvmStatic
    // fun disableAlpha() = apply {
    //     RenderSystem.disableAlpha()
    // }

    // TODO(BREAKING): Remove this
    // @JvmStatic
    // fun enableAlpha() = apply {
    //     RenderSystem.enableAlpha()
    // }

    // TODO(BREAKING): Remove this
    // @JvmStatic
    // fun alphaFunc(func: Int, ref: Float) = apply {
    //     RenderSystem.alphaFunc(func, ref)
    // }

    // TODO(BREAKING): Remove this
    // @JvmStatic
    // fun enableLighting() = apply {
    //     RenderSystem.enableLighting()
    // }

    // TODO(BREAKING): Remove this
    // @JvmStatic
    // fun disableLighting() = apply {
    //     RenderSystem.disableLighting()
    // }

    @JvmStatic
    fun disableDepth() = apply {
        RenderSystem.disableDepthTest()
    }

    @JvmStatic
    fun enableDepth() = apply {
        RenderSystem.enableDepthTest()
    }

    @JvmStatic
    fun depthFunc(depthFunc: Int) = apply {
        RenderSystem.depthFunc(depthFunc)
    }

    @JvmStatic
    fun depthMask(flagIn: Boolean) = apply {
        RenderSystem.depthMask(flagIn)
    }

    @JvmStatic
    fun disableBlend() = apply {
        RenderSystem.disableBlend()
    }

    @JvmStatic
    fun enableBlend() = apply {
        RenderSystem.enableBlend()
    }

    @JvmStatic
    fun blendFunc(sourceFactor: Int, destFactor: Int) = apply {
        RenderSystem.blendFunc(sourceFactor, destFactor)
    }

    @JvmStatic
    fun tryBlendFuncSeparate(sourceFactor: Int, destFactor: Int, sourceFactorAlpha: Int, destFactorAlpha: Int) = apply {
        RenderSystem.blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha)
    }

    @JvmStatic
    fun enableTexture2D() = apply {
        RenderSystem.enableTexture()
    }

    @JvmStatic
    fun disableTexture2D() = apply {
        RenderSystem.disableTexture()
    }

    // TODO(BREAKING): Remove these
    // /**
    //  * Binds a texture to the client for the Tessellator to use.
    //  *
    //  * @param texture the texture to bind
    //  * @return the Tessellator to allow for method chaining
    //  */
    // @JvmStatic
    // fun bindTexture(texture: Image) = apply {
    //     RenderSystem.bindTexture(texture.getTexture().glTextureId)
    // }
    //
    // @JvmStatic
    // fun deleteTexture(texture: Image) = apply {
    //     RenderSystem.deleteTexture(texture.getTexture().glTextureId)
    // }

    @JvmStatic
    fun pushMatrix() = apply {
        boundMatrixStack.push()
    }

    @JvmStatic
    fun popMatrix() = apply {
        boundMatrixStack.pop()
    }

    /**
     * Begin drawing with the Tessellator
     * with default draw mode of quads and textured
     *
     * @param drawMode the GL draw mode
     * @param textured if the Tessellator is textured
     * @return the Tessellator to allow for method chaining
     * @see com.chattriggers.ctjs.minecraft.libs.renderer.Shape#setDrawMode(int)
     */
    @JvmStatic
    @JvmOverloads
    fun begin(drawMode: Int = 7, textured: Boolean = true) = apply {
        pushMatrix()
        enableBlend()
        tryBlendFuncSeparate(770, 771, 1, 0)

        val cameraPos = Client.getMinecraft().gameRenderer.camera.pos
        translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)

        buffer.begin(
            VertexFormat.DrawMode.values()[drawMode],
            if (textured) VertexFormats.POSITION_TEXTURE else VertexFormats.POSITION,
        )

        firstVertex = true
        began = true
    }

    /**
     * Colorize the Tessellator.
     *
     * @param red the red value between 0 and 1
     * @param green the green value between 0 and 1
     * @param blue the blue value between 0 and 1
     * @param alpha the alpha value between 0 and 1
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    @JvmOverloads
    fun colorize(red: Float, green: Float, blue: Float, alpha: Float = 1f) = apply {
        RenderSystem.setShaderColor(
            red.coerceIn(0f..1f),
            green.coerceIn(0f..1f),
            blue.coerceIn(0f..1f),
            alpha.coerceIn(0f..1f),
        )
        colorized = true
    }

    /**
     * Rotates the Tessellator in 3d space.
     * Similar to [com.chattriggers.ctjs.minecraft.libs.renderer.Renderer.rotate]
     *
     * @param angle the angle to rotate
     * @param x if the rotation is around the x axis
     * @param y if the rotation is around the y axis
     * @param z if the rotation is around the z axis
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    fun rotate(angle: Float, x: Float, y: Float, z: Float) = apply {
        // TODO("fabric"): Is this degrees or radians? Do we need a conversion
        boundMatrixStack.multiply(Quaternion(x, y, z, angle))
    }

    /**
     * Translates the Tessellator in 3d space.
     * Similar to [com.chattriggers.ctjs.minecraft.libs.renderer.Renderer.translate]
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    fun translate(x: Double, y: Double, z: Double) = apply {
        boundMatrixStack.translate(x, y, z)
    }

    /**
     * Scales the Tessellator in 3d space.
     * Similar to [com.chattriggers.ctjs.minecraft.libs.renderer.Renderer.scale]
     *
     * @param x scale in the x direction or all directions if y and z are not supplied
     * @param y scale in the y direction
     * @param z scale in the z direction
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    @JvmOverloads
    fun scale(x: Float, y: Float = x, z: Float = x) = apply {
        boundMatrixStack.scale(x, y, z)
    }

    /**
     * Sets a new vertex in the Tessellator.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    fun pos(x: Float, y: Float, z: Float) = apply {
        if (!began)
            begin()
        if (!firstVertex)
            buffer.next()
        buffer.vertex(x.toDouble(), y.toDouble(), z.toDouble())
        firstVertex = false
    }

    /**
     * Sets the texture location on the last defined vertex.
     * Use directly after using [Tessellator.pos]
     *
     * @param u the u position in the texture
     * @param v the v position in the texture
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    fun tex(u: Float, v: Float) = apply {
        buffer.texture(u, v)
    }

    /**
     * Finalizes and draws the Tessellator.
     */
    @JvmStatic
    fun draw() {
        if (!began)
            return

        buffer.next()

        if (!colorized)
            colorize(1f, 1f, 1f, 1f)

        MCTessellator.getInstance().draw()
        began = false
        colorized = false

        disableBlend()
        popMatrix()
    }

    /**
     * Gets a fixed render position from x, y, and z inputs adjusted with partial ticks
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the Vector3f position to render at
     */
    @JvmStatic
    fun getRenderPos(x: Float, y: Float, z: Float): Vec3d {
        return Vec3d(
            x - Player.getRenderX(),
            y - Player.getRenderY(),
            z - Player.getRenderZ()
        )
    }

    /**
     * Gets a fixed render position from x, y, and z inputs adjusted with partial ticks
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     * @return the Vector3f position to render at
     */
    @JvmStatic
    fun getRenderPos(x: Double, y: Double, z: Double): Vec3d {
        return Vec3d(
            x - Player.getRenderX(),
            y - Player.getRenderY(),
            z - Player.getRenderZ()
        )
    }

    /**
     * Renders floating lines of text in the 3D world at a specific position.
     *
     * @param text The string array of text to render
     * @param x X coordinate in the game world
     * @param y Y coordinate in the game world
     * @param z Z coordinate in the game world
     * @param color the color of the text
     * @param renderBlackBox render a pretty black border behind the text
     * @param scale the scale of the text
     * @param increase whether to scale the text up as the player moves away
     */
    @JvmStatic
    @JvmOverloads
    fun drawString(
        text: String,
        x: Float,
        y: Float,
        z: Float,
        color: Int = 0xffffffff.toInt(),
        renderBlackBox: Boolean = true,
        scale: Float = 1f,
        increase: Boolean = true
    ) {
        val player = Player.getPlayer() ?: return

        var lScale = scale
        val fontRenderer = Renderer.getFontRenderer()
        val renderPos = getRenderPos(x, y, z)

        if (increase) {
            val distance = sqrt(renderPos.x * renderPos.x + renderPos.y * renderPos.y + renderPos.z * renderPos.z)
            val multiplier = distance.toFloat() / 120f //mobs only render ~120 blocks away
            lScale *= 0.45f * multiplier
        }

        colorize(1f, 1f, 1f, 0.5f)
        pushMatrix()
        translate(renderPos.x, renderPos.y, renderPos.z)
        rotate(player.pitch, 0.0f, 1.0f, 0.0f)
        rotate(player.yaw, 1.0f, 0.0f, 0.0f)
        scale(-lScale, -lScale, lScale)
        // disableLighting()
        depthMask(false)
        disableDepth()
        enableBlend()
        blendFunc(770, 771)

        val textWidth = fontRenderer.getWidth(text)

        if (renderBlackBox) {
            val j = textWidth / 2
            disableTexture2D()
            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
            buffer.vertex((-j - 1).toDouble(), (-1).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).next()
            buffer.vertex((-j - 1).toDouble(), 8.toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).next()
            buffer.vertex((j + 1).toDouble(), 8.toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).next()
            buffer.vertex((j + 1).toDouble(), (-1).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).next()
            MCTessellator.getInstance().draw()
            enableTexture2D()
        }

        fontRenderer.draw(boundMatrixStack, text, -textWidth / 2f, 0f, color)

        colorize(1.0f, 1.0f, 1.0f, 1.0f)
        depthMask(true)
        enableDepth()
        popMatrix()
    }
}
