package com.chattriggers.ctjs.utils.capes;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;

//#if MC<=10809
import net.minecraft.util.MathHelper;
//#else
//$$ import net.minecraft.util.math.MathHelper;
//#endif

public class LayerCape implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerCape(RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }

    public void doRenderLayer(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ResourceLocation rl = CapeHandler.INSTANCE.getCapeResource(player);

        if (!player.hasPlayerInfo()) return;
        if (player.isInvisible()) return;
        if (!player.isWearing(EnumPlayerModelParts.CAPE)) return;
        if (rl == null) return;


        float f9 = 0.14F;
        float f10 = 0.0F;
        if (player.isSneaking()) {
            f9 = 0.1F;
            f10 = 0.09F;
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.playerRenderer.bindTexture(rl);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, f10, f9);
        double d0 = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * (double)partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks);
        double d1 = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * (double)partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks);
        double d2 = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * (double)partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks);
        float f = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        double d3 = MathHelper.sin(f * 0.01745329F);
        double d4 = -MathHelper.cos(f * 0.01745329F);
        float f1 = (float)d1 * 10.0F;
        //#if MC<=10809
        f1 = MathHelper.clamp_float(f1, 3.0F, 32.0F);
        //#else
        //$$ f1 = MathHelper.clamp(f1, 3.0F, 32.0F);
        //#endif
        float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
        float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f4 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
        f1 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;
        if (player.isSneaking()) {
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
