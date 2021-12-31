package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Inject(
        method = "render",
        at = @At("HEAD"),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void injectRender(MatrixStack matrices, CallbackInfo ci) {
        Renderer.setBoundMatrixStack$ctjs(matrices);
        TriggerType.RenderBossHealth.triggerAll(ci);
    }
}
