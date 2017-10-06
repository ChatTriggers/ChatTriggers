package com.chattriggers.ctjs.triggers;

import java.util.ArrayList;

public enum TriggerType {
    CHAT, WORLD_LOAD, SOUND_PLAY, TICK, STEP, RENDER_OVERLAY, RENDER_IMAGE,
    WORLD_UNLOAD;

    private ArrayList<OnTrigger> triggers = new ArrayList<>();

    public void clearTriggers() {
        triggers.clear();
    }

    public void addTrigger(OnTrigger trigger) {
        triggers.add(trigger);
    }

    public void removeTrigger(OnTrigger trigger) {
        triggers.remove(trigger);
    }

    public ArrayList<OnTrigger> getTriggers() {
        return triggers;
    }

    public void triggerAll(Object... args) {
        for (OnTrigger trigger : triggers) {
            trigger.trigger(args);
        }
    }

    public static void clearAllTriggers() {
        for (TriggerType triggerType : TriggerType.values()) {
            triggerType.clearTriggers();
        }
    }
}
