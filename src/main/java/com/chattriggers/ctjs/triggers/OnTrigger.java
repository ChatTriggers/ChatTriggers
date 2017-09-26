package com.chattriggers.ctjs.triggers;

public abstract class OnTrigger {
    protected String methodName;

    protected OnTrigger(String methodName) {
        this.methodName = methodName;
    }

    public abstract void trigger(Object... args);

    public enum TriggerResult {
        CANCEL
    }
}
