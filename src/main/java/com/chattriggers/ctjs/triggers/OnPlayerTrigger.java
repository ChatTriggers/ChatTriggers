package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import javax.script.ScriptException;

public class OnPlayerTrigger extends OnTrigger {
    public OnPlayerTrigger(String methodName) {
        super(methodName, TriggerType.PLAYER_JOIN);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof EntityOtherPlayerMP))
            throw new IllegalArgumentException("Argument 1 must be an EntityPlayerMP");

        PlayerMP player = new PlayerMP((EntityOtherPlayerMP) args[0]);

        try {
            CTJS.getInstance().getModuleManager().invokeFunction(methodName, player);
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e, this);
            type.removeTrigger(this);
        }
    }
}
