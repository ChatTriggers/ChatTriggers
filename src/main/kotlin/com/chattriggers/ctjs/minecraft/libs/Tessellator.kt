package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.minecraft.libs.renderer.Image
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.utils.kotlin.MCTessellator
import com.chattriggers.ctjs.utils.kotlin.getRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.util.vector.Vector3f
import kotlin.math.sqrt

object Tessellator {
    @JvmStatic
    var partialTicks = 0f
        internal set

    private var tessellator = MCTessellator.getInstance()
    private var worldRenderer = tessellator.getRenderer()
    private val renderManager = Renderer.getRenderManager()

    private var firstVertex = true
    private var began = false

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

    /**
     * Begin drawing with the Tessellator
     * with default draw mode of quads and textured
     *
     * @param drawMode the GL draw mode
     * @param textured if the Tessellator is textured
     * @return the Tessellator to allow for method chaining
     * @see com.chattriggers.ctjs.minecraft.libs.renderer.Shape.setDrawMode
     */
    @JvmStatic
    @JvmOverloads
    fun begin(drawMode: Int = 7, textured: Boolean = true) = apply {
        pushMatrix()
        enableBlend()
        tryBlendFuncSeparate(770, 771, 1, 0)

        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ)

        worldRenderer.begin(
            drawMode,
            if (textured) DefaultVertexFormats.POSITION_TEX else DefaultVertexFormats.POSITION
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
        GlStateManager.color(red, green, blue, alpha)
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
        GlStateManager.rotate(angle, x, y, z)
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
    fun translate(x: Float, y: Float, z: Float) = apply {
        GlStateManager.translate(x, y, z)
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
        GlStateManager.scale(x, y, z)
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
            worldRenderer.endVertex()
        worldRenderer.pos(x.toDouble(), y.toDouble(), z.toDouble())
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
        worldRenderer.tex(u.toDouble(), v.toDouble())
    }

    /**
     * Finalizes and draws the Tessellator.
     */
    @JvmStatic
    fun draw() {
        if (!began) return

        worldRenderer.endVertex()

        tessellator.draw()

        colorize(1f, 1f, 1f, 1f)

        began = false

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
    fun getRenderPos(x: Float, y: Float, z: Float): Vector3f {
        return Vector3f(
            x - Player.getRenderX().toFloat(),
            y - Player.getRenderY().toFloat(),
            z - Player.getRenderZ().toFloat()
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
        var lScale = scale

        val fontRenderer = Renderer.getFontRenderer()

        val renderPos = getRenderPos(x, y, z)

        if (increase) {
            val distance = sqrt(renderPos.x * renderPos.x + renderPos.y * renderPos.y + renderPos.z * renderPos.z)
            val multiplier = distance / 120f //mobs only render ~120 blocks away
            lScale *= 0.45f * multiplier
        }

        val xMultiplier = if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) -1 else 1

        GlStateManager.color(1f, 1f, 1f, 0.5f)
        pushMatrix()
        translate(renderPos.x, renderPos.y, renderPos.z)
        rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        rotate(renderManager.playerViewX * xMultiplier, 1.0f, 0.0f, 0.0f)
        scale(-lScale, -lScale, lScale)
        disableLighting()
        depthMask(false)
        disableDepth()
        enableBlend()
        blendFunc(770, 771)

        val textWidth = fontRenderer.getStringWidth(text)

        if (renderBlackBox) {
            val j = textWidth / 2
            disableTexture2D()
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
            worldRenderer.pos((-j - 1).toDouble(), (-1).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldRenderer.pos((-j - 1).toDouble(), 8.toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldRenderer.pos((j + 1).toDouble(), 8.toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldRenderer.pos((j + 1).toDouble(), (-1).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            tessellator.draw()
            enableTexture2D()
        }

        fontRenderer.drawString(text, -textWidth / 2, 0, color)

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        depthMask(true)
        enableDepth()
        popMatrix()
    }
}
