package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.listeners.events.ChatEvent;
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Inventory;
import com.chattriggers.ctjs.triggers.EventType;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    @Inject(
            //#if MC<=11202
            method = "handleJoinGame",
            //#elseif MC>=11701
            //$$ method = "handleLogin",
            //#endif
            at = @At("TAIL")
    )
    private void chattriggers_worldLoadTrigger(S01PacketJoinGame packetIn, CallbackInfo ci) {
        CTJS.Companion.getEventListeners(EventType.WorldLoad).forEach(listener -> {
            listener.invoke(null);
        });
    }

    @Inject(method = "cleanup", at = @At("TAIL"))
    private void chattriggers_worldUnloadTrigger(CallbackInfo ci) {
        TriggerType.WorldUnload.triggerAll();
    }

    @Inject(
            method = "handleChat",
            at = @At(
                value = "INVOKE",
                //#if MC<=11202
                target = "Lnet/minecraft/network/PacketThreadUtil;checkThreadAndEnqueue(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;Lnet/minecraft/util/IThreadListener;)V",
                //#elseif MC>=11701
                //$$ target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
                //#endif
                shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void chattriggers_chatTrigger(S02PacketChat packetIn, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(
                //#if MC<=11202
                packetIn.getChatComponent(),
                packetIn.getType()
                //#elseif MC>=11701
                //$$ packetIn.getMessage(),
                //$$ packetIn.getType().getIndex(),
                //$$ packetIn.getSender()
                //#endif
        );

        CTJS.Companion.getEventListeners(EventType.Chat).forEach((listener) -> {
            listener.invoke(new Object[]{event});
        });

        if (event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(
            //#if MC<=11202
            method = "handleOpenWindow",
            //#elseif MC>=11701
            //$$ method = "handleOpenScreen",
            //#endif
            at = @At("HEAD")
    )
    private void chattriggers_getInventoryName(S2DPacketOpenWindow packetIn, CallbackInfo ci) {
        Inventory.Companion.setOpenedInventoryName$chattriggers(
                //#if MC<=11202
                packetIn.getWindowTitle().getFormattedText()
                //#elseif MC>=11701
                //$$ packetIn.getTitle().getString()
                //#endif
        );
    }
}
