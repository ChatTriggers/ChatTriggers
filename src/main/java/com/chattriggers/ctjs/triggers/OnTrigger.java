package com.chattriggers.ctjs.triggers;

import lombok.Getter;
import lombok.Setter;

public abstract class OnTrigger {
    protected String methodName;
    @Getter @Setter
    protected Priority priority;
    @Getter
    protected TriggerType type;

    protected OnTrigger(String methodName, TriggerType type) {
        this.methodName = methodName;
        this.priority = Priority.NORMAL;
        this.type = type;

        this.getType().addTrigger(this);
    }

    public OnTrigger setPriority(Priority priority) {
        this.priority = priority;

        return this;
    }

    public OnTrigger register() {
        this.getType().addTrigger(this);
        return this;
    }

    public OnTrigger unregister() {
        this.getType().removeTrigger(this);
        return this;
    }

    public boolean isRegistered() {
        return getType().containsTrigger(this);
    }

    public abstract void trigger(Object... args);

    public enum TriggerResult {
        CANCEL
    }
}
