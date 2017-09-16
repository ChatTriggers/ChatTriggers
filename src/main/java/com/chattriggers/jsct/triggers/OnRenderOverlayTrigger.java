package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.JSCT;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import javax.script.ScriptException;

public class OnRenderOverlayTrigger extends Trigger {
    public OnRenderOverlayTrigger(String methodName) {
        super(methodName);
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof RenderGameOverlayEvent)) {
            throw new IllegalArgumentException("Argument 1 must be of type PlaySoundEvent");
        }

        //TODO: figure out how to use render event in js
        RenderGameOverlayEvent event = (RenderGameOverlayEvent) args[0];

        try {
            JSCT.getInstance().getScriptEngine().invokeFunction(methodName);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
