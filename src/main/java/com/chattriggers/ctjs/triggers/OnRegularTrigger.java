package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;

import javax.script.ScriptException;

public class OnRegularTrigger extends OnTrigger {

    public OnRegularTrigger(String methodName, TriggerType triggerType) {
        super(methodName, triggerType);
    }

    @Override
    public void trigger(Object... args) {
        try {
            CTJS.getInstance().getModuleManager().invokeFunction(methodName, args);
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e);
            type.removeTrigger(this);
        }
    }
}
