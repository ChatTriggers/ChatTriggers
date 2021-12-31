package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
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
        target = { @Desc(value = "send", args = { Packet.class, GenericFutureListener.class }) },
        at = { @At("HEAD") },
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void injectSend(
        ClientConnection connection,
        Packet<?> packet,
        GenericFutureListener<?> listener,
        CallbackInfo ci
    ) {
        TriggerType.PacketSent.triggerAll(packet, ci);
    }
}
