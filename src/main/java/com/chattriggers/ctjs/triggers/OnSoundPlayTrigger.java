package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.libs.EventLib;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

import javax.script.ScriptException;

public class OnSoundPlayTrigger extends OnTrigger {
    private String soundNameCriteria = "";

    public OnSoundPlayTrigger(String methodName) {
        super(methodName, TriggerType.SOUND_PLAY);
    }

    /**
     * Sets the sound name criteria.
     * @param soundNameCriteria the sound name
     * @return the trigger for method chaining
     */
    public OnSoundPlayTrigger setSoundNameCriteria(String soundNameCriteria) {
        this.soundNameCriteria = soundNameCriteria;

        return this;
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof PlaySoundEvent)) {
            throw new IllegalArgumentException("Argument 1 must be of type PlaySoundEvent");
        }

        PlaySoundEvent event = (PlaySoundEvent) args[0];

        if (soundNameCriteria != null && !EventLib.getName(event).equalsIgnoreCase(soundNameCriteria)) {
            return;
        }

        try {
            CTJS.getInstance().getModuleManager().invokeFunction(methodName, event);
        } catch (ScriptException | NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e);
            TriggerType.SOUND_PLAY.removeTrigger(this);
        }
    }
}
