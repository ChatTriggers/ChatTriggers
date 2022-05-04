package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader

class SoundPlayTrigger(method: Any, loader: ILoader) : Trigger(method, TriggerType.SoundPlay, loader) {
    private var soundNameCriteria = ""

    /**
     * Sets the sound name criteria.
     *
     * @param soundNameCriteria the sound name
     * @return the trigger for method chaining
     */
    fun setCriteria(soundNameCriteria: String) = apply { this.soundNameCriteria = soundNameCriteria }

    override fun trigger(args: Array<out Any?>) {
        if (args[1] is String
            && soundNameCriteria != ""
            && !(args[1] as String).equals(soundNameCriteria, ignoreCase = true)
        )
            return

        callMethod(args)
    }
}
