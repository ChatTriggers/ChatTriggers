package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import javax.script.ScriptException;

public class OnRenderTrigger extends OnTrigger {
    private Boolean triggerIfCanceled;

    public OnRenderTrigger(String methodName, TriggerType triggerType) {
        super(methodName, triggerType);
        triggerIfCanceled = true;
    }

    /**
     * Sets if the chat trigger should run if the chat event has already been canceled.
     * True by default.
     * @param bool Boolean to set
     * @return the trigger object for method chaining
     */
    public OnRenderTrigger triggerIfCanceled(Boolean bool) {
        this.triggerIfCanceled = bool;
        return this;
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof RenderGameOverlayEvent))
            throw new IllegalArgumentException("Argument 0 must be a RenderGameOverlayEvent");
        RenderGameOverlayEvent event = (RenderGameOverlayEvent) args[0];

        if (!triggerIfCanceled && event.isCanceled()) return;

        try {
            GlStateManager.pushMatrix();
            CTJS.getInstance().getModuleManager().invokeFunction(methodName, args);
            GlStateManager.popMatrix();
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e, this);
            type.removeTrigger(this);
        }
    }
}
