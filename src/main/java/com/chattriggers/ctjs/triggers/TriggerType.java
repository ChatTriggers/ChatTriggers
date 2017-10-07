package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.utils.console.Console;

import java.util.ArrayList;
import java.util.PriorityQueue;

public enum TriggerType {
    CHAT, WORLD_LOAD, SOUND_PLAY, TICK, STEP, RENDER_OVERLAY, WORLD_UNLOAD;

    private PriorityQueue<OnTrigger> triggers = new PriorityQueue<> (
            (obj1, obj2) -> obj1.priority.priority - obj2.priority.priority
    );

    private ArrayList<OnTrigger> triggersRemove = new ArrayList<>();

    public void clearTriggers() {
        triggers.clear();
    }

    public void addTrigger(OnTrigger trigger) {
        triggers.add(trigger);
    }

    public void removeTrigger(OnTrigger trigger) {
        // Add to removal list to avoid concurrent modification exceptions
        triggersRemove.add(trigger);
    }

    public PriorityQueue<OnTrigger> getTriggers() {
        return triggers;
    }

    public void triggerAll(Object... args) {
        for (OnTrigger trigger : triggers) {
            // Check for removal of broken trigger before running
            if (triggersRemove.contains(trigger)) {
                try {
                    triggersRemove.remove(trigger);
                    triggers.remove(trigger);
                } catch (Exception e) {
                    Console.getConsole().out.println("Failed to unregister broken function. Trying again later.");
                    Console.getConsole().out.println(trigger.methodName);
                }
                return;
            }

            // run the trigger
            trigger.trigger(args);
        }
    }

    public static void clearAllTriggers() {
        for (TriggerType triggerType : TriggerType.values()) {
            triggerType.clearTriggers();
        }
    }
}
