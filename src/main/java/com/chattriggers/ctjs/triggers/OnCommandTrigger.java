package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;

import javax.script.ScriptException;

public class OnCommandTrigger extends OnTrigger {
    public OnCommandTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args instanceof String[])) throw new IllegalArgumentException("Arguments must be string array");

        try {
            CTJS.getInstance().getInvocableEngine().invokeFunction(methodName, args);
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e);
        }
    }
}
