package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

@Accessors(chain = true)
public class Tessellator {
    @Getter
    private ArrayList<Vector3d> vertexes;
    @Getter
    @Setter
    private String resourceName = null;
    @Getter
    @Setter
    private String resourceDomain;
    @Getter
    @Setter
    private int drawMode;
    @Getter
    @Setter
    private boolean inWorld;

    public Tessellator() {
        this.vertexes = new ArrayList<>();
        this.resourceDomain = "ctjs.images";
        this.drawMode = 7;
        this.inWorld = true;
    }

    public Tessellator pos(float x, float y, float z) {
        this.vertexes.add(new Vector3d(x, y, z));
        return this;
    }

    public void draw() {
        if (resourceName == null) return;

        ResourceLocation rl = new ResourceLocation(this.resourceDomain, this.resourceName);
        Client.getMinecraft().getTextureManager().bindTexture(rl);

        net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();

        if (this.inWorld) {
            RenderManager renderManager = Client.getMinecraft().getRenderManager();
            GlStateManager.translate(-renderManager.viewerPosX, -renderManager.viewerPosY, -renderManager.viewerPosZ);
        }

        worldrenderer.begin(this.drawMode, DefaultVertexFormats.POSITION);
        for (Vector3d vertex : vertexes)
            worldrenderer.pos(vertex.getX(), vertex.getY(), vertex.getZ()).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();

        vertexes.clear();
    }
}