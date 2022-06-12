package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.triggers.EventType;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerLoginClient.class)
public class NetHandlerLoginClientMixin {
    @Final
    @Shadow
    //#if MC<=11202
    private NetworkManager networkManager;
    //#elseif MC>=11701
    //$$ private Connection connection;
    //#endif

    @Inject(
            //#if MC<=11202
            method = "handleLoginSuccess",
            //#elseif MC>=11701
            //$$ method = "handleGameProfile",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC<=11202
                    target = "Lnet/minecraft/network/NetworkManager;setConnectionState(Lnet/minecraft/network/EnumConnectionState;)V",
                    //#elseif MC>=11701
                    //$$ target = "Lnet/minecraft/network/Connection;setProtocol(Lnet/minecraft/network/ConnectionProtocol;)V",
                    //#endif
                    shift = At.Shift.AFTER
            )
    )
    private void chattriggers_serverConnectTrigger(CallbackInfo ci) {
        CTJS.Companion.getEventListeners(EventType.ServerConnect).forEach(listener -> {
            //#if MC<=11202
            listener.invoke(new Object[]{networkManager});
            //#elseif MC>=11701
            //$$ listener.invoke(new Object[]{connection});
            //#endif
        });
    }

    @Inject(method = "handleDisconnect", at = @At("HEAD"))
    private void chattriggers_serverDisconnectTrigger(CallbackInfo ci) {
        TriggerType.ServerDisconnect.triggerAll();
    }
}
