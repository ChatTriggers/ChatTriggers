package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.minecraft.listeners.WorldListener
import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.world.ClientWorld
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Desc
import org.spongepowered.asm.mixin.injection.Inject

@Mixin(ClientPlayNetworkHandler::class)
class ClientPlayNetworkHandlerMixin {
    @Inject(
        method = ["onGameJoin"],
        at = [At(
            value = "INVOKE",
            desc = Desc(value = "joinWorld", owner = MinecraftClient::class, args = [ClientWorld::class]),
        )],
    )
    fun injectOnGameJoin() {
        WorldListener.shouldTriggerWorldLoad = true
        WorldListener.playerList.clear()
    }

    @Inject(method = ["onDisconnect"], at = [At("HEAD")])
    fun injectOnDisconnect() {
        TriggerType.WorldUnload.triggerAll()
    }
}
