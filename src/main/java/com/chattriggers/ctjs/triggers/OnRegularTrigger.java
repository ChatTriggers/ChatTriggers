package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;

import javax.script.ScriptException;

public class OnRegularTrigger extends OnTrigger {
    private TriggerType triggerType;

    public OnRegularTrigger(String methodName, TriggerType triggerType) {
        super(methodName);
        this.triggerType = triggerType;
    }

    public OnRegularTrigger(String methodName, TriggerType triggerType, Priority priority) {
        this(methodName, triggerType);
        this.priority = priority;
    }

    @Override
    public void trigger(Object... args) {
        try {
            CTJS.getInstance().getInvocableEngine().invokeFunction(methodName, args);
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e);
            triggerType.removeTrigger(this);
        }
    }
}
