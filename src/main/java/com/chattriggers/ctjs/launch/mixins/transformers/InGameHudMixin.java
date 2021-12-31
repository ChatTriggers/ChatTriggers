package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    public void injectRenderCrosshair(MatrixStack matrices, CallbackInfo ci) {
        Renderer.setBoundMatrixStack(matrices);
        TriggerType.RenderCrosshair.triggerAll(ci);
    }

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    public void injectRenderHealthBar(MatrixStack matrices, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        Renderer.setBoundMatrixStack(matrices);
        TriggerType.RenderHealth.triggerAll(ci);
    }

    @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    public void injectRenderMountHealth(MatrixStack matrices, CallbackInfo ci) {
        Renderer.setBoundMatrixStack(matrices);
        TriggerType.RenderMountHealth.triggerAll(ci);
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void injectRenderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci) {
        Renderer.setBoundMatrixStack(matrices);
        TriggerType.RenderExperience.triggerAll(ci);
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
    public void injectRenderHotbar(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        Renderer.setBoundMatrixStack(matrices);
        TriggerType.RenderHotbar.triggerAll(ci);
    }

    @Redirect(
        method = "renderStatusBars",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"
            )
        )
    )
    public void redirectArmorDrawTextureCalls(
        InGameHud instance,
        MatrixStack matrixStack,
        int x,
        int y,
        int u,
        int v,
        int width,
        int height
    ) {
        Renderer.setBoundMatrixStack(matrixStack);
        CancellableEvent event = new CancellableEvent();
        TriggerType.RenderArmor.triggerAll(event);
        if (!event.isCancelled())
            instance.drawTexture(matrixStack, x, y, u, v, width, height);
    }

    @Redirect(
        method = "renderStatusBars",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/hud/InGameHud;getRiddenEntity()Lnet/minecraft/entity/LivingEntity;"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/player/PlayerEntity;getMaxAir()I"
            )
        )
    )
    public void redirectFoodDrawTextureCalls(
        InGameHud instance,
        MatrixStack matrixStack,
        int x,
        int y,
        int u,
        int v,
        int width,
        int height
    ) {
        Renderer.setBoundMatrixStack(matrixStack);
        CancellableEvent event = new CancellableEvent();
        TriggerType.RenderFood.triggerAll(event);
        if (!event.isCancelled())
            instance.drawTexture(matrixStack, x, y, u, v, width, height);
    }

    @Redirect(
        method = "renderStatusBars",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/player/PlayerEntity;getMaxAir()I"
            )
        )
    )
    public void redirectAirDrawTextureCalls(
        InGameHud instance,
        MatrixStack matrixStack,
        int x,
        int y,
        int u,
        int v,
        int width,
        int height
    ) {
        Renderer.setBoundMatrixStack(matrixStack);
        CancellableEvent event = new CancellableEvent();
        TriggerType.RenderAir.triggerAll(event);
        if (!event.isCancelled())
            instance.drawTexture(matrixStack, x, y, u, v, width, height);
    }

    @Inject(method = "render", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void injectRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Renderer.setBoundMatrixStack(matrices);
        TriggerType.RenderOverlay.triggerAll();
    }
}
