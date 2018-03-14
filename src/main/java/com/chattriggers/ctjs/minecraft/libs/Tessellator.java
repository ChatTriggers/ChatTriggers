package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Tessellator {
    private net.minecraft.client.renderer.Tessellator tessellator;
    private WorldRenderer worldRenderer;


    public Tessellator() {
        this.tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        worldRenderer = tessellator.getWorldRenderer();
    }

    public Tessellator bindTexture(String texture, String domain) {
        ResourceLocation rl = new ResourceLocation(domain, texture);
        Client.getMinecraft().getTextureManager().bindTexture(rl);

        return this;
    }

    public Tessellator bindTexture(String texture) {
        return bindTexture(texture, "ctjs.images");
    }

    public Tessellator begin(int drawMode) {
        GlStateManager.pushMatrix();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();

        RenderManager renderManager = Client.getMinecraft().getRenderManager();
        GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);

        this.worldRenderer.begin(drawMode, DefaultVertexFormats.POSITION);

        return this;
    }

    public Tessellator begin() {
        return begin(7);
    }

    public Tessellator pos(float x, float y, float z) {
        this.worldRenderer.pos(x, y, z).endVertex();

        return this;
    }

    public void draw() {
        this.tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
    }
}