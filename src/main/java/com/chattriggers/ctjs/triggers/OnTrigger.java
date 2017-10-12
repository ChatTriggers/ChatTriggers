package com.chattriggers.ctjs.triggers;

public abstract class OnTrigger {
    protected String methodName;
    protected Priority priority;

    protected OnTrigger(String methodName) {
        this.methodName = methodName;
        this.priority = Priority.NORMAL;
    }

    public OnTrigger setPriority(Priority priority) {
        this.priority = priority;

        return this;
    }

    public abstract void trigger(Object... args);

    public enum TriggerResult {
        CANCEL
    }
}
