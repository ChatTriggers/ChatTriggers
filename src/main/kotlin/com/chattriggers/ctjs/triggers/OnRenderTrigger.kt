package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.utils.kotlin.External

@External
class OnRenderTrigger(method: Any, triggerType: TriggerType, loader: ILoader) : OnTrigger(method, triggerType, loader) {
    private var triggerIfCanceled: Boolean = true

    /**
     * Sets if the render trigger should run if the event has already been canceled.
     * True by default.
     * @param bool Boolean to set
     * @return the trigger object for method chaining
     */
    fun triggerIfCanceled(bool: Boolean) = apply { triggerIfCanceled = bool }

    override fun trigger(args: Array<out Any?>) {
        if (args[0] !is CancellableEvent)
            throw IllegalArgumentException("Argument 0 must be a RenderGameOverlayEvent")

        val event = args[0] as CancellableEvent
        if (!triggerIfCanceled && event.isCanceled())
            return

        callMethod(args)
    }
}
