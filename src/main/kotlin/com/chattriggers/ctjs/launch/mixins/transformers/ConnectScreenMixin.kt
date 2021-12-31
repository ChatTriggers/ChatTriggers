package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ConnectScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.ServerAddress
import net.minecraft.client.network.ServerInfo
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Desc
import org.spongepowered.asm.mixin.injection.Inject

@Mixin(ConnectScreen::class)
class ConnectScreenMixin {
    @Inject(
        target = [Desc(
            value = "connect",
            args = [
                Screen::class, MinecraftClient::class, ServerAddress::class, ServerInfo::class
            ],
        )],
        at = [At("HEAD")],
    )
    fun injectConnect() {
        // TODO(FEATURE): Pass in some args?
        TriggerType.ServerConnect.triggerAll()
    }
}
