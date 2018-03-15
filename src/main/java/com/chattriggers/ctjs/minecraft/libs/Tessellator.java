package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Tessellator {
    private net.minecraft.client.renderer.Tessellator tessellator;
    private WorldRenderer worldRenderer;
    private boolean firstVertex;

    public Tessellator() {
        this.tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        this.worldRenderer = this.tessellator.getWorldRenderer();
        this.firstVertex = true;
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
        //GlStateManager.disableTexture2D();

        // TODO: NEEDS OWN FUNC
        GL11.glColor4f(1F, 1F, 1F, 1F);

        RenderManager renderManager = Client.getMinecraft().getRenderManager();
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);

        this.worldRenderer.begin(drawMode, textured ? DefaultVertexFormats.POSITION_TEX : DefaultVertexFormats.POSITION);
        this.firstVertex = true;

        return this;
    }

    public Tessellator begin() {
        return begin(GL11.GL_QUADS, true);
    }

    public Tessellator pos(float x, float y, float z) {
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
        this.tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }
}