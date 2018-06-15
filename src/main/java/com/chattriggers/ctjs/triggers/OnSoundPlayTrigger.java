package com.chattriggers.ctjs.triggers;

import lombok.Setter;

public class OnSoundPlayTrigger extends OnTrigger {
    @Setter
    private String soundNameCriteria = "";

    public OnSoundPlayTrigger(Object method) {
        super(method, TriggerType.SOUND_PLAY);
    }

    /**
     * Sets the sound name criteria.<br>
     * Short hand for {@link OnSoundPlayTrigger#setSoundNameCriteria(String)}.
     *
     * @param soundNameCriteria the sound name
     * @return the trigger for method chaining
     */
    public OnSoundPlayTrigger setCriteria(String soundNameCriteria) {
        this.soundNameCriteria = soundNameCriteria;

        return this;
    }

    @Override
    public void trigger(Object... args) {
        if (args[2] instanceof String
        && !soundNameCriteria.equals("")
        && !((String) args[2]).equalsIgnoreCase(soundNameCriteria))
            return;

        callMethod(args);
    }
}
