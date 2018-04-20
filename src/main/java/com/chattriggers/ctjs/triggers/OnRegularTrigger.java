package com.chattriggers.ctjs.triggers;

public class OnRegularTrigger extends OnTrigger {

    public OnRegularTrigger(Object method, TriggerType triggerType) {
        super(method, triggerType);
    }

    @Override
    public void trigger(Object... args) {
        //System.out.println("Triggering p2: " + Arrays.toString(args));
        callMethod(args);
    }
}
