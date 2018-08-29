package com.chattriggers.ctjs.triggers

class OnSoundPlayTrigger(method: Any) : OnTrigger(method, TriggerType.SOUND_PLAY) {
    var soundNameCriteria = ""

    /**
     * Sets the sound name criteria.<br></br>
     * Short hand for [OnSoundPlayTrigger.setSoundNameCriteria].
     *
     * @param soundNameCriteria the sound name
     * @return the trigger for method chaining
     */
    fun setCriteria(soundNameCriteria: String) = apply { this.soundNameCriteria = soundNameCriteria }

    override fun trigger(vararg args: Any) {
        if (args[2] is String
            && soundNameCriteria != ""
            && !(args[2] as String).equals(soundNameCriteria, ignoreCase = true))
            return

        callMethod(*args)
    }
}
