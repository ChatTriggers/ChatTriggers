package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {
    @Inject(method = "renderScoreboard", at = @At("HEAD"), cancellable = true)
    void injectRenderScoreboard(ScoreObjective objective, ScaledResolution scaledRes, CallbackInfo ci) {
        TriggerType.RenderScoreboard.triggerAll(ci);
    }
}
