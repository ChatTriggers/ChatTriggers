package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.SortedSet

class ForgeTrigger(
    method: Any,
    private val eventClass: Class<*>,
    loader: ILoader,
) : Trigger(method, TriggerType.Forge, loader) {
    init {
        require(Event::class.java.isAssignableFrom(eventClass)) {
            "ForgeTrigger expects an Event class, but found ${eventClass.simpleName}"
        }

        forgeTriggers.getOrPut(eventClass) { sortedSetOf() }.add(this)
    }

    override fun register(): Trigger {
        forgeTriggers.getOrPut(eventClass) { sortedSetOf() }.add(this)
        return super.register()
    }

    override fun unregister(): Trigger {
        forgeTriggers[eventClass]?.remove(this)
        return super.unregister()
    }

    override fun setPriority(priority: Priority): Trigger {
        super.setPriority(priority)

        // Re-register so the position in the SortedSet gets updated
        unregister()
        register()

        return this
    }

    override fun trigger(args: Array<out Any?>) {
        callMethod(args)
    }

    companion object {
        private val forgeTriggers = mutableMapOf<Class<*>, SortedSet<ForgeTrigger>>()

        fun unregisterTriggers() {
            forgeTriggers.values.flatten().forEach {
                it.unregister()
            }
            forgeTriggers.clear()
        }

        @SubscribeEvent
        fun onEvent(event: Event) {
            // Prevents double-firing in single player
            if (Thread.currentThread().name == "Server thread")
                return

            forgeTriggers[event::class.java]?.forEach {
                it.trigger(arrayOf(event))
            }
        }
    }
}
