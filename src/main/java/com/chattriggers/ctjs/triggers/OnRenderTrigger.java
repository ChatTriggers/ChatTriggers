package com.chattriggers.ctjs.triggers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class OnRenderTrigger extends OnTrigger {
    private boolean triggerIfCanceled;

    public OnRenderTrigger(Object method, TriggerType triggerType) {
        super(method, triggerType);
        triggerIfCanceled = true;
    }

    /**
     * Sets if the render trigger should run if the event has already been canceled.
     * True by default.
     * @param bool Boolean to set
     * @return the trigger object for method chaining
     */
    public OnRenderTrigger triggerIfCanceled(boolean bool) {
        this.triggerIfCanceled = bool;
        return this;
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof RenderGameOverlayEvent))
            throw new IllegalArgumentException("Argument 0 must be a RenderGameOverlayEvent");
        RenderGameOverlayEvent event = (RenderGameOverlayEvent) args[0];

        if (!triggerIfCanceled && event.isCanceled()) return;

        GlStateManager.pushMatrix();
        callMethod(args);
        GlStateManager.popMatrix();
    }
}
