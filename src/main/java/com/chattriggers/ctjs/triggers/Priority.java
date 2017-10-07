package com.chattriggers.ctjs.triggers;

public enum Priority {
    //LOWEST IS RAN LAST
    LOWEST(0), LOW(1), NORMAL(2), HIGH(3), HIGHEST(4);

    public int priority;

    Priority(int priority) {
        this.priority = priority;
    }
}
