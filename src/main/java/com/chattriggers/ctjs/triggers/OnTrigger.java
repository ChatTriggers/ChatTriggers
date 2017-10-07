package com.chattriggers.ctjs.triggers;

public abstract class OnTrigger {
    protected String methodName;
    protected Priority priority;

    protected OnTrigger(String methodName) {
        this.methodName = methodName;
    }

    public OnTrigger(String methodName, Priority priority) {
        this(methodName);
        this.priority = priority;
    }

    public abstract void trigger(Object... args);

    public enum TriggerResult {
        CANCEL
    }
}
