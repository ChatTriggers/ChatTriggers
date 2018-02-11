package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;
import io.sentry.Sentry;
import io.sentry.context.Context;
import io.sentry.event.Breadcrumb;
import io.sentry.event.BreadcrumbBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.PriorityQueue;

public enum TriggerType {
    // client
    CHAT, TICK, STEP, RENDER_OVERLAY, GAME_UNLOAD, GAME_LOAD, CLICKED, GUI_OPENED,

    // world
    SOUND_PLAY, WORLD_LOAD, WORLD_UNLOAD,

    // misc
    COMMAND, OTHER;

    private Comparator<OnTrigger> triggerComparator = (o1, o2) -> {
        if (o1.priority.ordinal() > o2.priority.ordinal()) return -1;
        else if (o2.priority.ordinal() > o1.priority.ordinal()) return 1;

        return 0;
    };

    private PriorityQueue<OnTrigger> triggers = new PriorityQueue<>(triggerComparator);

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

    public boolean containsTrigger(OnTrigger trigger) {
        return triggers.contains(trigger);
    }

    public void triggerAll(Object... args) {
        if (CTJS.getInstance().getModuleManager().isLoading()) return;

        ArrayList<OnTrigger> triggersCopy = new ArrayList<>(triggers.size());

        Context context = Sentry.getContext();
        context.recordBreadcrumb(
            new BreadcrumbBuilder()
                .setCategory("generic")
                .setTimestamp(new Date())
                .setType(Breadcrumb.Type.DEFAULT)
                .setLevel(Breadcrumb.Level.INFO)
                .setMessage("Trigger is of type: " + this.toString())
                .build()
        );

        while (!triggers.isEmpty()){
            OnTrigger trigger = triggers.poll();
            triggersCopy.add(trigger);

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


        context.clearBreadcrumbs();
        context.clearExtra();
        context.clearTags();

        triggers.addAll(triggersCopy);
    }

    public static void clearAllTriggers() {
        for (TriggerType triggerType : TriggerType.values()) {
            triggerType.clearTriggers();
        }
    }
}
