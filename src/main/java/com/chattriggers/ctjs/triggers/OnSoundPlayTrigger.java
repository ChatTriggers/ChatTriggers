package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.minecraft.libs.EventLib;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

public class OnSoundPlayTrigger extends OnTrigger {
    private String soundNameCriteria = "";

    public OnSoundPlayTrigger(Object method) {
        super(method, TriggerType.SOUND_PLAY);
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

        callMethod(event);
    }
}
