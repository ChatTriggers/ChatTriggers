package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;

import javax.script.ScriptException;

public class CommandTrigger extends Trigger {
    public CommandTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args instanceof String[])) throw new IllegalArgumentException("Arguments must be string array");

        try {
            CTJS.getInstance().getInvocableEngine().invokeFunction(methodName, args);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
