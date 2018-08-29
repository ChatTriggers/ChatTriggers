package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.loader.ModuleManager
import io.sentry.Sentry
import io.sentry.event.Breadcrumb
import io.sentry.event.BreadcrumbBuilder

import java.util.ArrayList
import java.util.Date
import java.util.PriorityQueue

enum class TriggerType {
    // client
    CHAT,
    ACTION_BAR,
    TICK, STEP,
    GAME_UNLOAD, GAME_LOAD,
    CLICKED, DRAGGED,
    GUI_OPENED, SCREENSHOT_TAKEN,
    PICKUP_ITEM, DROP_ITEM,
    MESSAGE_SENT, TOOLTIP,

    // rendering
    RENDER_WORLD,
    BLOCK_HIGHLIGHT,
    RENDER_OVERLAY, RENDER_PLAYER_LIST, RENDER_BOSS_HEALTH, RENDER_DEBUG,
    RENDER_CROSSHAIR, RENDER_HOTBAR, RENDER_EXPERIENCE,
    RENDER_HEALTH, RENDER_FOOD, RENDER_MOUNT_HEALTH, RENDER_AIR,

    // world
    PLAYER_JOIN,
    PLAYER_LEAVE,
    SOUND_PLAY, NOTE_BLOCK_PLAY, NOTE_BLOCK_CHANGE,
    WORLD_LOAD, WORLD_UNLOAD,

    // misc
    COMMAND,
    OTHER;

    private val triggerComparator = { o1: OnTrigger, o2: OnTrigger -> Integer.compare(o2.priority.ordinal, o1.priority.ordinal) }

    val triggers = PriorityQueue<OnTrigger>(triggerComparator)

    fun clearTriggers() {
        triggers.clear()
    }

    fun addTrigger(trigger: OnTrigger) {
        triggers.add(trigger)
    }

    fun removeTrigger(trigger: OnTrigger) {
        triggers.removeIf { onTrigger -> onTrigger === trigger }
    }

    fun containsTrigger(trigger: OnTrigger): Boolean {
        return triggers.contains(trigger)
    }

    fun triggerAll(vararg args: Any) {
        if (ModuleManager.getInstance().isLoading) return

        val triggersCopy = ArrayList<OnTrigger>(triggers.size)

        val context = Sentry.getContext()
        context.recordBreadcrumb(
            BreadcrumbBuilder()
                .setCategory("generic")
                .setTimestamp(Date())
                .setType(Breadcrumb.Type.DEFAULT)
                .setLevel(Breadcrumb.Level.INFO)
                .setMessage("Trigger is of type: " + this.toString())
                .build()
        )

        while (!triggers.isEmpty()) {
            val trigger = triggers.poll() ?: continue

            triggersCopy.add(trigger)

            // run the trigger
            trigger.trigger(*args)
        }

        context.run {
            clearBreadcrumbs()
            clearExtra()
            clearTags()
        }

        triggers.addAll(triggersCopy)
    }

    companion object {
        @JvmStatic
        fun clearAllTriggers() {
            for (triggerType in TriggerType.values()) {
                triggerType.clearTriggers()
            }
        }
    }
}
