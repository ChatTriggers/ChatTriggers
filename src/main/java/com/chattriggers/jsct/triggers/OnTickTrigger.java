package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.JSCT;

import javax.script.ScriptException;

public class OnTickTrigger extends Trigger {
    public OnTickTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof Integer)) throw new IllegalArgumentException("1st argument is not an integer");

        try {
            JSCT.getInstance().getInvocableEngine().invokeFunction(methodName, args[0]);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
