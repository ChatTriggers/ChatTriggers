package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;

    @Accessor
    public abstract int getCurrentFps();

    @Inject(
        method = "setScreen",
        at = @At("HEAD"),
        cancellable = true
    )
    public void injectSetScreen(Screen screen, CallbackInfo ci) {
        if (screen == null) {
            TriggerType.GuiClosed.triggerAll(this.currentScreen, ci);
        } else {
            TriggerType.GuiOpened.triggerAll(screen, ci);
        }
    }
}
