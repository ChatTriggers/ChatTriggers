package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({net.minecraft.client.network.NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient {
    @Inject(
            method = "addToSendQueue",
            at=@At("HEAD"),
            cancellable = true
    )
    private void interceptPacket(Packet packet, CallbackInfo ci) {
        TriggerType.PACKET_SENT.triggerAll(packet, ci );
    }
}
