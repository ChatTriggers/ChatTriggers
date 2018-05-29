package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.loader.ModuleManager;
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
    CHAT, TICK, STEP,
    GAME_UNLOAD, GAME_LOAD,
    CLICKED, DRAGGED,
    GUI_OPENED, SCREENSHOT_TAKEN,
    PICKUP_ITEM, DROP_ITEM,
    MESSAGE_SENT,

    // rendering
    RENDER_WORLD, BLOCK_HIGHLIGHT,
    RENDER_OVERLAY, RENDER_PLAYER_LIST, RENDER_BOSS_HEALTH, RENDER_DEBUG,
    RENDER_CROSSHAIR, RENDER_HOTBAR, RENDER_EXPERIENCE,
    RENDER_HEALTH, RENDER_FOOD, RENDER_MOUNT_HEALTH, RENDER_AIR,

    // world
    PLAYER_JOIN, PLAYER_LEAVE,
    SOUND_PLAY, NOTE_BLOCK_PLAY, NOTE_BLOCK_CHANGE,
    WORLD_LOAD, WORLD_UNLOAD,

    // misc
    COMMAND, OTHER;

    private Comparator<OnTrigger> triggerComparator = (o1, o2)
            -> Integer.compare(o2.priority.ordinal(), o1.priority.ordinal());

    private PriorityQueue<OnTrigger> triggers = new PriorityQueue<>(triggerComparator);

    public void clearTriggers() {
        triggers.clear();
    }

    public void addTrigger(OnTrigger trigger) {
        triggers.add(trigger);
    }

    public void removeTrigger(OnTrigger trigger) {
        triggers.removeIf(onTrigger -> onTrigger == trigger);
    }

    public PriorityQueue<OnTrigger> getTriggers() {
        return triggers;
    }

    public boolean containsTrigger(OnTrigger trigger) {
        return triggers.contains(trigger);
    }

    public void triggerAll(Object... args) {
        if (ModuleManager.getInstance().isLoading()) return;

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

        while (!triggers.isEmpty()) {
            OnTrigger trigger = triggers.poll();

            if (trigger == null) continue;

            triggersCopy.add(trigger);

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
