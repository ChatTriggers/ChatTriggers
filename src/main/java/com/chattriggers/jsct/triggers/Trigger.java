package com.chattriggers.jsct.triggers;

public abstract class Trigger {
    protected String methodName;

    protected Trigger(String methodName) {
        this.methodName = methodName;
    }

    public abstract void trigger(Object... args);
}
