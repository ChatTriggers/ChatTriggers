package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(MinecraftClient::class)
abstract class MinecraftClientMixin {
    @Accessor
    abstract fun getCurrentFps(): Int

    @Inject(method = ["setScreen"], at = [At("HEAD")], cancellable = true)
    fun injectSetScreen(client: MinecraftClient, screen: Screen?, ci: CallbackInfo) {
        if (screen == null) {
            TriggerType.GuiClosed.triggerAll(client.currentScreen, ci)
        } else {
            TriggerType.GuiOpened.triggerAll(screen, ci)
        }
    }
}
