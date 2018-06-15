package com.chattriggers.ctjs.triggers;

public class OnRegularTrigger extends OnTrigger {

    public OnRegularTrigger(Object method, TriggerType triggerType) {
        super(method, triggerType);
    }

    @Override
    public void trigger(Object... args) {
        callMethod(args);
    }
}
