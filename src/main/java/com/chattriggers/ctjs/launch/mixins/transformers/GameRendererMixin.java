package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.listeners.WorldListener;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    // @Inject(
    //     method = "render",
    //     at = @At(
    //         value = "INVOKE",
    //         target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"
    //     ),
    //     locals = LocalCapture.CAPTURE_FAILHARD
    // )
    @ModifyArg(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"
        ),
        index = 0
    )
    public MatrixStack onBeforeRenderScreen(MatrixStack matrices) {
        int mouseX = (int)(this.client.mouse.getX() * (double)this.client.getWindow().getScaledWidth() / (double)this.client.getWindow().getWidth());
        int mouseY = (int)(this.client.mouse.getY() * (double)this.client.getWindow().getScaledHeight() / (double)this.client.getWindow().getHeight());

        Renderer.setBoundMatrixStack(matrices);
        TriggerType.GuiRender.triggerAll(mouseX, mouseY, Client.getMinecraft().currentScreen);
        TriggerType.Step.triggerAll();
        WorldListener.onPreOverlayRender();
        return matrices;
    }

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/Screen;render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            shift = At.Shift.AFTER
        )
    )
    public void onAfterRenderScreen(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        int mouseX = (int)(this.client.mouse.getX() * (double)this.client.getWindow().getScaledWidth() / (double)this.client.getWindow().getWidth());
        int mouseY = (int)(this.client.mouse.getY() * (double)this.client.getWindow().getScaledHeight() / (double)this.client.getWindow().getHeight());

        // TODO("fabric"): If this is really an issue, save the matrix stack from above into a local
        //                 and rebind it here.
        // Renderer.setBoundMatrixStack(matrixStack);

        TriggerType.PostGuiRender.triggerAll(mouseX, mouseY, Client.getMinecraft().currentScreen);
    }
}
