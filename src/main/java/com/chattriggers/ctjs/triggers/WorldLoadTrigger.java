package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;

import javax.script.ScriptException;

public class WorldLoadTrigger extends Trigger {

    public WorldLoadTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        try {
            CTJS.getInstance().getInvocableEngine().invokeFunction(methodName);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
