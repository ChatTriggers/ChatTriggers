package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.minecraft.listeners.WorldListener;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerNetworkHandlerMixin {
    @Inject(
        method = "onGameJoin",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/MinecraftClient;joinWorld(Lnet/minecraft/client/world/ClientWorld;)V"
        )
    )
    public void injectOnGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        WorldListener.setShouldTriggerWorldLoad(true);
        WorldListener.getPlayerList().clear();
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    public void injectOnDisconnect(DisconnectS2CPacket packet, CallbackInfo ci) {
        TriggerType.WorldUnload.triggerAll();
    }
}
