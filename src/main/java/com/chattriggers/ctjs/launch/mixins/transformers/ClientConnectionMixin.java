package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static void injectHandlePacket(
        Packet<?> packet,
        PacketListener listener,
        CallbackInfo ci
    ) {
        TriggerType.PacketReceived.triggerAll(packet, ci);
    }

    @Inject(
        method = "send(Lnet/minecraft/network/Packet;Lio/netty/util/concurrent/GenericFutureListener;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void injectSend(
        Packet<?> packet,
        @Nullable GenericFutureListener<? extends Future<? super Void>> callback,
        CallbackInfo ci
    ) {
        TriggerType.PacketSent.triggerAll(packet, ci);
    }
}
