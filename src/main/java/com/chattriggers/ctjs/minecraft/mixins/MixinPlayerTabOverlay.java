package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinPlayerTabOverlay {
    @Inject(method = "renderPlayerlist", at = @At("HEAD"))
    private void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn, CallbackInfo ci) {
        ChatLib.chat("LOL");
    }
}
