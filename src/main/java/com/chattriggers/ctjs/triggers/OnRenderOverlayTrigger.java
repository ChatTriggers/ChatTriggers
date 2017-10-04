package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import javax.script.ScriptException;

public class OnRenderOverlayTrigger extends OnTrigger {
    public OnRenderOverlayTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof RenderGameOverlayEvent)) {
            throw new IllegalArgumentException("Argument 1 must be of type RenderGameOverlayEvent");
        }

        RenderGameOverlayEvent event = (RenderGameOverlayEvent) args[0];

        try {
            CTJS.getInstance().getInvocableEngine().invokeFunction(methodName, event);
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e);
            TriggerRegister.TriggerTypes.RENDER_OVERLAY.removeTrigger(this);
        }
    }
}
