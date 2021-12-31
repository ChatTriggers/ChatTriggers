package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
    @Accessor
    public abstract boolean isVisible();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void injectRender(
        MatrixStack matrices,
        int scaledWindowWidth,
        Scoreboard scoreboard,
        ScoreboardObjective objective,
        CallbackInfo ci
    ) {
        TriggerType.RenderPlayerList.triggerAll(ci);
    }
}
