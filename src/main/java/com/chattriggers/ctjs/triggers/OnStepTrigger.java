package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import net.minecraft.client.Minecraft;

import javax.script.ScriptException;

public class OnStepTrigger extends OnTrigger {
    private long fps;
    private long systemTime;

    protected OnStepTrigger(String methodName, long fps) {
        super(methodName);
        this.fps = fps;
        this.systemTime = Minecraft.getSystemTime();
    }

    @Override
    public void trigger(Object... args) {
        try {
            while (this.systemTime < Minecraft.getSystemTime() + (1000 / fps)) {
                CTJS.getInstance().getInvocableEngine().invokeFunction(methodName);
                this.systemTime += (1000 / fps);
            }
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
