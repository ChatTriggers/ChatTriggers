package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import io.netty.util.concurrent.GenericFutureListener
import net.minecraft.network.ClientConnection
import net.minecraft.network.Packet
import net.minecraft.network.listener.PacketListener
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Desc
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

@Mixin(ClientConnection::class)
class ClientConnectionMixin {
    @Inject(method = ["handlePacket"], at = [At("HEAD")], cancellable = true)
    fun <T : PacketListener> injectHandlePacket(packet: Packet<T>, listener: PacketListener, ci: CallbackInfo) {
        TriggerType.PacketReceived.triggerAll(packet, ci)
    }

    @Inject(
        target = [Desc(
            value = "send",
            args = [Packet::class, GenericFutureListener::class]
        )],
        at = [At("HEAD")],
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun injectSend(connection: ClientConnection, packet: Packet<*>, listener: GenericFutureListener<*>, ci: CallbackInfo) {
        TriggerType.PacketSent.triggerAll(packet, ci)
    }
}
