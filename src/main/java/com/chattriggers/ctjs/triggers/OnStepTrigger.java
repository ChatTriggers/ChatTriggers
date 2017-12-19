package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.libs.MinecraftVars;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.Minecraft;

import javax.script.ScriptException;

public class OnStepTrigger extends OnTrigger {
    private Long fps = 60L;
    private Long delay = null;
    private Long systemTime;

    protected OnStepTrigger(String methodName) {
        super(methodName, TriggerType.STEP);
        this.systemTime = Minecraft.getSystemTime();
    }

    /**
     * Sets the frames per second that the trigger activates.
     * This is limited to 1 step per second.
     * @param fps the frames per second to set
     * @return the trigger for method chaining
     */
    public OnStepTrigger setFps(long fps) {
        if (fps < 1)
            this.fps = 1L;
        else
            this.fps = fps;

        return this;
    }

    /**
     * Sets the delay in seconds between the trigger activation.
     * This is limited to one step every second. This will override {@link #setFps(long)}.
     * @param delay The delay in seconds
     * @return the trigger for method chaining
     */
    public OnStepTrigger setDelay(long delay) {
        if (delay < 1)
            this.delay = 1L;
        else
            this.delay = delay;

        return this;
    }

    @Override
    public void trigger(Object... args) {
        try {
            if (this.delay == null) {
                // run trigger based on set fps value (60 per second by default)
                while (this.systemTime < MinecraftVars.getSystemTime() + (1000 / this.fps)) {
                    CTJS.getInstance().getModuleManager().invokeFunction(this.methodName);
                    this.systemTime += (1000 / this.fps);
                }
            } else {
                // run trigger based on set delay in seconds
                while (MinecraftVars.getSystemTime() > this.systemTime + this.delay * 1000) {
                    CTJS.getInstance().getModuleManager().invokeFunction(this.methodName);
                    this.systemTime += this.delay * 1000;
                }

            }
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e, this);
            TriggerType.STEP.removeTrigger(this);
        }
    }
}
