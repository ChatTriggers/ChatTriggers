package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.gui.screen.GameMenuScreen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject

@Mixin(GameMenuScreen::class)
class GameMenuScreenMixin {
    @Inject(
        method = ["method_19836"],
        at = [At(
            value = "INVOKE",
            target = "net/minecraft/client/MinecraftClient.disconnect()V",
        )],
    )
    fun injectMethod_19836() { // Last button lambda in initWidgets
        // TODO("fabric"): Pass any info which is in the 1.8.9 event
        TriggerType.ServerDisconnect.triggerAll()
    }
}
