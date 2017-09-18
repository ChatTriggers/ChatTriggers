package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.JSCT;

import javax.script.ScriptException;

public class CommandTrigger extends Trigger {
    public CommandTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args instanceof String[])) throw new IllegalArgumentException("Arguments must be string array");

        try {
            JSCT.getInstance().getInvocableEngine().invokeFunction(methodName, args);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
