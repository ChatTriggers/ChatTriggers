package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.utils.kotlin.External

@External
class OnSoundPlayTrigger(method: Any, loader: ILoader) : OnTrigger(method, TriggerType.SoundPlay, loader) {
    private var soundNameCriteria = ""

    /**
     * Sets the sound name criteria.
     *
     * @param soundNameCriteria the sound name
     * @return the trigger for method chaining
     */
    fun setCriteria(soundNameCriteria: String) = apply { this.soundNameCriteria = soundNameCriteria }

    override fun trigger(args: Array<out Any?>) {
        if (args[2] is String
            && soundNameCriteria != ""
            && !(args[2] as String).equals(soundNameCriteria, ignoreCase = true)
        )
            return

        callMethod(args)
    }
}
