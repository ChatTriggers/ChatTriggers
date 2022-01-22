package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Inject(
        method = "render",
        at = @At("HEAD"),
        cancellable = true
    )
    public void injectRender(MatrixStack matrices, CallbackInfo ci) {
        Renderer.setBoundMatrixStack(matrices);
        TriggerType.RenderDebug.triggerAll(ci);
    }
}
