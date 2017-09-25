package com.chattriggers.ctjs.triggers;

public abstract class Trigger {
    protected String methodName;

    protected Trigger(String methodName) {
        this.methodName = methodName;
    }

    public abstract void trigger(Object... args);

    public enum TriggerResult {
        CANCEL
    }
}
