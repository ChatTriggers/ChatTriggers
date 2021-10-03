package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.minecraft.libs.renderer.Image
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import kotlin.math.sqrt

//#if MC==11602
//$$ import com.mojang.blaze3d.matrix.MatrixStack
//$$ import net.minecraft.util.math.vector.Quaternion
//$$ import net.minecraft.util.math.vector.Vector3f
//#else
import org.lwjgl.util.vector.Vector3f
//#endif

@External
object Tessellator {
    //#if MC==11602
    //$$ lateinit var boundMatrixStack: MatrixStack
    //#endif

    @JvmStatic
    var partialTicks = 0f
        internal set

    private var firstVertex = true
    private var began = false
    private var colorized = false

    @JvmStatic
    fun getTessellator(): net.minecraft.client.renderer.Tessellator {
        return net.minecraft.client.renderer.Tessellator.getInstance()
    }

    @JvmStatic
    fun getWorldRenderer(): WorldRenderer {
        return getTessellator().worldRenderer
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
        //#if MC==11602
        //$$ RenderSystem.blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha)
        //#else
        GlStateManager.tryBlendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha)
        //#endif
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
     * @see com.chattriggers.ctjs.minecraft.libs.renderer.Shape#setDrawMode(int)
     */
    @JvmStatic
    @JvmOverloads
    fun begin(drawMode: Int = GL11.GL_QUADS, textured: Boolean = true) = apply {
        GL11.glPushMatrix()

        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

        val renderManager = Client.getMinecraft().renderManager
        translate(
            -Client.camera.getX().toFloat(),
            -Client.camera.getY().toFloat(),
            -Client.camera.getZ().toFloat(),
        )

        getWorldRenderer().begin(
            drawMode,
            if (textured) DefaultVertexFormats.POSITION_TEX else DefaultVertexFormats.POSITION
        )
        this.firstVertex = true
        this.began = true
    }

    /**
     * Colorize the Tessellator.
     *
     * @param red   the red value between 0 and 1
     * @param green the green value between 0 and 1
     * @param blue  the blue value between 0 and 1
     * @param alpha the alpha value between 0 and 1
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    @JvmOverloads
    fun colorize(red: Float, green: Float, blue: Float, alpha: Float = 255f) = apply {
        GlStateManager.color(red, green, blue, alpha)
        this.colorized = true
    }

    /**
     * Rotates the Tessellator in 3d space.
     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#rotate(float)}
     *
     * @param angle the angle to rotate
     * @param x     if the rotation is around the x axis
     * @param y     if the rotation is around the y axis
     * @param z     if the rotation is around the z axis
     * @return the Tessellator to allow for method chaining
     */
    @JvmStatic
    fun rotate(angle: Float, x: Float, y: Float, z: Float) = apply {
        //#if MC==11602
        //$$ boundMatrixStack.rotate(Quaternion(Vector3f(x, y, z), angle, true))
        //#else
        GL11.glRotatef(angle, x, y, z)
        //#endif
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
        //#if MC==11602
        //$$ boundMatrixStack.translate(x.toDouble(), y.toDouble(), z.toDouble())
        //#else
        GL11.glTranslatef(x, y, z)
        //#endif
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
        //#if MC==11602
        //$$ boundMatrixStack.scale(x, y, z)
        //#else
        GL11.glScalef(x, y, z)
        //#endif
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
        if (!this.began)
            this.begin()
        if (!this.firstVertex)
            getWorldRenderer().endVertex()
        getWorldRenderer().pos(x.toDouble(), y.toDouble(), z.toDouble())
        this.firstVertex = false
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
        //#if MC==11602
        //$$ Tessellator.getWorldRenderer().tex(u, v)
        //#else
        getWorldRenderer().tex(u.toDouble(), v.toDouble())
        //#endif
    }

    /**
     * Finalizes and draws the Tessellator.
     */
    @JvmStatic
    fun draw() {
        if (!began) return

        getWorldRenderer().endVertex()

        if (!this.colorized)
            colorize(1f, 1f, 1f, 1f)

        getTessellator().draw()
        this.began = false
        this.colorized = false

        GlStateManager.disableBlend()

        GL11.glPopMatrix()
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
            x - (Player.getPlayer()!!.lastTickPosX + (Player.getPlayer()!!.posX - Player.getPlayer()!!.lastTickPosX) * partialTicks).toFloat(),
            y - (Player.getPlayer()!!.lastTickPosY + (Player.getPlayer()!!.posY - Player.getPlayer()!!.lastTickPosY) * partialTicks).toFloat(),
            z - (Player.getPlayer()!!.lastTickPosZ + (Player.getPlayer()!!.posZ - Player.getPlayer()!!.lastTickPosZ) * partialTicks).toFloat()
        )
    }

    /**
     * Renders floating lines of text in the 3D world at a specific position.
     *
     * @param text           The string array of text to render
     * @param x              X coordinate in the game world
     * @param y              Y coordinate in the game world
     * @param z              Z coordinate in the game world
     * @param color          the color of the text
     * @param renderBlackBox render a pretty black border behind the text
     * @param scale          the scale of the text
     * @param increase       whether or not to scale the text up as the player moves away
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

        val renderManager = Renderer.getRenderManager()
        val fontRenderer = Renderer.getFontRenderer()

        val renderPos = getRenderPos(x, y, z)

        if (increase) {
            val distance = sqrt(renderPos.x * renderPos.x + renderPos.y * renderPos.y + renderPos.z * renderPos.z)
            val multiplier = distance / 120f //mobs only render ~120 blocks away
            lScale *= 0.45f * multiplier
        }

        GL11.glColor4f(1f, 1f, 1f, 0.5f)
        GL11.glPushMatrix()
        GL11.glTranslatef(renderPos.x, renderPos.y, renderPos.z)
        // TODO(1.16.2)
        //#if MC==10809
        GL11.glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        //#endif
        GL11.glScalef(-lScale, -lScale, lScale)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glDepthMask(false)
        GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        val textWidth = fontRenderer.getStringWidth(text)

        if (renderBlackBox) {
            val j = textWidth / 2
            GlStateManager.disableTexture2D()
            getWorldRenderer().begin(7, DefaultVertexFormats.POSITION_COLOR)
            getWorldRenderer().pos((-j - 1).toDouble(), (-1).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            getWorldRenderer().pos((-j - 1).toDouble(), 8.toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            getWorldRenderer().pos((j + 1).toDouble(), 8.toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            getWorldRenderer().pos((j + 1).toDouble(), (-1).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            getTessellator().draw()
            GlStateManager.enableTexture2D()
        }

        //#if MC==11602
        //$$ fontRenderer.drawString(boundMatrixStack, text, -textWidth / 2f, 0f, color)
        //#else
        fontRenderer.drawString(text, -textWidth / 2, 0, color)
        //#endif

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glDepthMask(true)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glPopMatrix()
    }
}
