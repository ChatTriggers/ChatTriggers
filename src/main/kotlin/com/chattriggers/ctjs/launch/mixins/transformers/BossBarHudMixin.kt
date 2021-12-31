package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.gui.hud.BossBarHud
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(BossBarHud::class)
class BossBarHudMixin {
    @Inject(method = ["render"], at = [At("HEAD")], cancellable = true)
    fun injectRender(ci: CallbackInfo) {
        TriggerType.RenderBossHealth.triggerAll(ci)
    }
}
