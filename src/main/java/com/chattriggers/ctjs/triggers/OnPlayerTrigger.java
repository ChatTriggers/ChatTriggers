package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP;
import com.chattriggers.ctjs.utils.console.Console;

import javax.script.ScriptException;

public class OnPlayerTrigger extends OnTrigger {
    public OnPlayerTrigger(String methodName) {
        super(methodName, TriggerType.PLAYER_JOIN);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof PlayerMP))
            throw new IllegalArgumentException("Argument 1 must be an EntityPlayerMP");

        PlayerMP player = (PlayerMP) args[0];

        try {
            CTJS.getInstance().getModuleManager().invokeFunction(methodName, player);
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e, this);
            type.removeTrigger(this);
        }
    }
}
