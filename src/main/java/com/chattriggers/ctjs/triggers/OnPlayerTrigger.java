package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP;

public class OnPlayerTrigger extends OnTrigger {
    public OnPlayerTrigger(Object method) {
        super(method, TriggerType.PLAYER_JOIN);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof PlayerMP))
            throw new IllegalArgumentException("Argument 1 must be an EntityPlayerMP");

        PlayerMP player = (PlayerMP) args[0];

        callMethod(player);

    }
}
