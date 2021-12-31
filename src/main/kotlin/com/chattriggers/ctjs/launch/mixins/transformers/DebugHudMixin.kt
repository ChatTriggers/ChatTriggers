package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.gui.hud.DebugHud
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(DebugHud::class)
class DebugHudMixin {
    @Inject(method = ["render"], at = [At("HEAD")], cancellable = true)
    fun injectRender(ci: CallbackInfo) {
        TriggerType.RenderDebug.triggerAll(ci)
    }
}
