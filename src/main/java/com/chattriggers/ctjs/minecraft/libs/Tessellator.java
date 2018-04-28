package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.minecraft.libs.renderer.Image;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class Tessellator {
    @Getter
    private static Tessellator instance;

    private net.minecraft.client.renderer.Tessellator tessellator;
    private WorldRenderer worldRenderer;
    private boolean firstVertex;
    private boolean began;
    private boolean colorized;

    public Tessellator() {
        instance = this;

        this.tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        this.worldRenderer = this.tessellator.getWorldRenderer();
        this.firstVertex = true;
        this.began = false;
        this.colorized = false;
    }

    /**
     * Binds a texture to the client for the Tessellator to use.
     *
     * @param texture the texture to bind
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator bindTexture(Image texture) {
        GlStateManager.bindTexture(texture.getTexture().getGlTextureId());

        return this;
    }

    /**
     * Begin drawing with the Tessellator.
     *
     * @see com.chattriggers.ctjs.minecraft.libs.renderer.Shape#setDrawMode(int)
     * @param drawMode the GL draw mode
     * @param textured if the Tessellator is textured
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator begin(int drawMode, boolean textured) {
        GlStateManager.pushMatrix();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        RenderManager renderManager = Client.getMinecraft().getRenderManager();
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);

        this.worldRenderer.begin(drawMode, textured ? DefaultVertexFormats.POSITION_TEX : DefaultVertexFormats.POSITION);
        this.firstVertex = true;
        this.began = true;

        return this;
    }

    /**
     * Begin drawing with the Tessellator
     * with default draw mode of quads and textured.
     *
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator begin() {
        return begin(GL11.GL_QUADS, true);
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
    public Tessellator colorize(float red, float green, float blue, float alpha) {
        GlStateManager.color(red, green, blue, alpha);
        this.colorized = true;

        return this;
    }

    /**
     * Rotates the Tessellator in 3d space.
     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#rotate(float)}
     *
     * @param angle the angle to rotate
     * @param x if the rotation is around the x axis
     * @param y if the rotation is around the y axis
     * @param z if the rotation is around the z axis
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator rotate(float angle, float x, float y, float z) {
        GlStateManager.rotate(angle, x, y, z);

        return this;
    }

    /**
     * Translates the Tessellator in 3d space.
     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#translate(float, float)}
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator translate(float x, float y, float z) {
        GlStateManager.translate(x, y, z);

        return this;
    }

    /**
     * Scales the Tessellator in 3d space.
     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#scale(float, float)}
     *
     * @param x scale in the x direction
     * @param y scale in the y direction
     * @param z scale in the z direction
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator scale(float x, float y, float z) {
        GlStateManager.scale(x, y, z);

        return this;
    }

    /**
     * Scales the Tessellator in equally 3d space.
     * Similar to {@link com.chattriggers.ctjs.minecraft.libs.renderer.Renderer#scale(float)}
     *
     * @param scale the scale
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator scale(float scale) {
        return scale(scale, scale, scale);
    }

    /**
     * Sets a new vertex in the Tessellator.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator pos(float x, float y, float z) {
        if (!this.began)
            this.begin();
        if (!this.firstVertex)
            this.worldRenderer.endVertex();
        this.worldRenderer.pos(x, y, z);
        this.firstVertex = false;

        return this;
    }

    /**
     * Sets the texture location on the last defined vertex.
     * Use directly after using {@link Tessellator#pos(float, float, float)}
     *
     * @param u the u position in the texture
     * @param v the v position in the texture
     * @return the Tessellator to allow for method chaining
     */
    public Tessellator tex(float u, float v) {
        this.worldRenderer.tex(u, v);

        return this;
    }

    /**
     * Finalizes and draws the Tessellator.
     */
    public void draw() {
        if (!began) return;

        this.worldRenderer.endVertex();

        if (!this.colorized)
            colorize(1F, 1F, 1F, 1F);

        this.tessellator.draw();
        this.began = false;
        this.colorized = false;

        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }
}