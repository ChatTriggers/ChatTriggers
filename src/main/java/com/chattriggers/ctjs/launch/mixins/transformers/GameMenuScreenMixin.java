package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin {
    @Inject(
        method = "method_19836",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;disconnect()V"
        )
    )
    public void inject_method_19836(ButtonWidget button, CallbackInfo ci) {
        // TODO("fabric"): Pass any info which is in the 1.8.9 event
        TriggerType.ServerDisconnect.triggerAll();
    }
}
