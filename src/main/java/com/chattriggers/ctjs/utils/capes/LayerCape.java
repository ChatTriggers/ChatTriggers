package com.chattriggers.ctjs.utils.capes;

import com.chattriggers.ctjs.CTJS;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class LayerCape implements LayerRenderer<AbstractClientPlayer> {
    private final String[] developers = {
            "f746f8682c834f9da885a9b40dcf9ac7",
            "02f62a6be7484546b9ff26e3ab4b1076",
            "c8f5b6e569d54ea38a956546511916e4",
            "00ee4df3834440e89ef52cf9e4d24a24"
    };

    // TODO move to online repo
    private final String[] creators = {
            "06777508a0144cbdaa3c179758b5e277",
            "ef962ec2df6e48a2ac9d6062c1b84652",
            "7beb3348294f44d8b967cdf0d872aff8",
            "5166f54fd6eb470faf1b0fcbee2de7eb",
            "e2db3b87ae5c4b91a04f7d6f5ef51e27",
            "d4482b99cb6d4ecf9f74c78e7d5ffdfc",
            "c64eae97fae04bdfb09cbef807073554",
            "63fd5c413cd641e987d473df9c0e1e56",
            "9f9a11f272ff444992dd19df4b35b6c9",
            "756808b77b274e35839f9029d97ae743",
            "8aec666ac3de4dfba7b64322ff96233e",
            "de4f9385fd8746049dd0a98e1ce91631",
            "6f31c9cade4f4ad6ad0e0c0eecbb0458",
            "5887350330aa49c2b7d1e31cc69141a7",
            "b4da325c543f488aaeb62276a1383984",
            "9e0e8ec355234d49b30873de119a8d02",
            "222fb704c0f141e8992f9238e35d1a4e",
            "57c5d01a179447d3925b040b51964101",
            "d4482b99cb6d4ecf9f74c78e7d5ffdfc",
            "fcb5f8cb61154c8b934bcd1cef21d6b9",
            "532de453ac214ddeaec9a18a24a79e27",
            "8a065437832f48f6aa127b246f8400b6",
            "b0b8ec4d8c7d4cb588a945548d8f822d"
    };

    private final RenderPlayer playerRenderer;

    public LayerCape(RenderPlayer playerRendererIn) {
        this.playerRenderer = playerRendererIn;
    }

    private ResourceLocation getResourceLocation(String uuid) {
        for (String developer : developers) {
            if (developer.equals(uuid)) {
                return new ResourceLocation("capes/ct/developer");
            }
        }

        for (String creator : creators) {
            if (creator.equals(uuid)) {
                return new ResourceLocation("capes/ct/creator");
            }
        }

        return null;
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!CTJS.getInstance().getConfig().getShowCapes()) return;
        if (!entitylivingbaseIn.hasPlayerInfo()) return;
        if (entitylivingbaseIn.isInvisible()) return;
        if (!entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE)) return;


        ResourceLocation rl = getResourceLocation(entitylivingbaseIn.getUniqueID().toString().replace("-", ""));
        if (rl == null) return;


        float f9 = 0.14F;
        float f10 = 0.0F;
        if (entitylivingbaseIn.isSneaking()) {
            f9 = 0.1F;
            f10 = 0.09F;
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.playerRenderer.bindTexture(rl);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, f10, f9);
        double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double)partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks);
        double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double)partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks);
        double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double)partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks);
        float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
        double d3 = MathHelper.sin(f * 0.01745329F);
        double d4 = -MathHelper.cos(f * 0.01745329F);
        float f1 = (float)d1 * 10.0F;
        f1 = MathHelper.clamp_float(f1, 3.0F, 32.0F);
        float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
        float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
        f1 += MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;
        if (entitylivingbaseIn.isSneaking()) {
            f1 += 20.0F;
        }
        GlStateManager.rotate(5.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.playerRenderer.getMainModel().renderCape(0.0625F);
        GlStateManager.popMatrix();
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
