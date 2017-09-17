package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.JSCT;

import javax.script.ScriptException;

public class WorldLoadTrigger extends Trigger {

    public WorldLoadTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        try {
            JSCT.getInstance().getInvocableEngine().invokeFunction(methodName);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
