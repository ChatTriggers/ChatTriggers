package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Tessellator {
    private net.minecraft.client.renderer.Tessellator tessellator;
    private WorldRenderer worldRenderer;
    private boolean firstVertex;
    private boolean began;
    private boolean colorized;

    public Tessellator() {
        this.tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        this.worldRenderer = this.tessellator.getWorldRenderer();
        this.firstVertex = true;
        this.began = false;
        this.colorized = false;
    }

    public Tessellator bindTexture(String texture, String domain) {
        ResourceLocation rl = new ResourceLocation(domain, texture);
        Client.getMinecraft().getTextureManager().bindTexture(rl);

        return this;
    }

    public Tessellator bindTexture(String texture) {
        return bindTexture(texture, "ctjs.images");
    }

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

    public Tessellator begin() {
        return begin(GL11.GL_QUADS, true);
    }

    public Tessellator colorize(float red, float green, float blue, float alpha) {
        GlStateManager.color(red, green, blue, alpha);
        this.colorized = true;

        return this;
    }

    public Tessellator rotate(float angle, float x, float y, float z) {
        GlStateManager.rotate(angle, x, y, z);

        return this;
    }

    public Tessellator translate(float x, float y, float z) {
        GlStateManager.translate(x, y, z);

        return this;
    }

    public Tessellator scale(float x, float y, float z) {
        GlStateManager.scale(x, y, z);

        return this;
    }

    public Tessellator scale(float scale) {
        return scale(scale, scale, scale);
    }

    public Tessellator pos(float x, float y, float z) {
        if (!this.began)
            this.begin();
        if (!this.firstVertex)
            this.worldRenderer.endVertex();
        this.worldRenderer.pos(x, y, z);
        this.firstVertex = false;

        return this;
    }

    public Tessellator tex(float u, float v) {
        this.worldRenderer.tex(u, v);

        return this;
    }

    public void draw() {
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