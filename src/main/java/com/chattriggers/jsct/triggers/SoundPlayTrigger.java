package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.JSCT;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

import javax.script.ScriptException;

public class SoundPlayTrigger extends Trigger {
    private String soundNameCriteria;

    public SoundPlayTrigger(String methodName, String soundNameCriteria) {
        super(methodName);
        this.soundNameCriteria = soundNameCriteria;
    }

    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof PlaySoundEvent)) {
            throw new IllegalArgumentException("Argument 1 must be of type PlaySoundEvent");
        }

        PlaySoundEvent event = (PlaySoundEvent) args[0];

        if (soundNameCriteria != null && !event.name.equalsIgnoreCase(soundNameCriteria)) {
            return;
        }

        try {
            Object returned = JSCT.getInstance().getInvocableEngine().invokeFunction(methodName, event);

            if (returned.equals(TriggerResult.CANCEL)) {
                event.result = null;
            }
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
