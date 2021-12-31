package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.listeners.WorldListener;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onBeforeRenderScreen(
        float tickDelta,
        long startTime,
        boolean tick,
        CallbackInfo ci,
        boolean tick2,
        int mouseX,
        int mouseY,
        Window window,
        Matrix4f matrix4f,
        MatrixStack matrixStack,
        MatrixStack matrixStack2
    ) {
        Renderer.setBoundMatrixStack(matrixStack2);
        TriggerType.GuiRender.triggerAll(mouseX, mouseY, Client.getMinecraft().currentScreen);
        TriggerType.Step.triggerAll();
        WorldListener.onPreOverlayRender();
    }

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void onAfterRenderScreen(
        float tickDelta,
        long startTime,
        boolean tick,
        CallbackInfo ci,
        boolean tick2,
        int mouseX,
        int mouseY,
        Window window,
        Matrix4f matrix4f,
        MatrixStack matrixStack,
        MatrixStack matrixStack2
    ) {
        Renderer.setBoundMatrixStack(matrixStack2);
        TriggerType.PostGuiRender.triggerAll(mouseX, mouseY, Client.getMinecraft().currentScreen);
    }
}
