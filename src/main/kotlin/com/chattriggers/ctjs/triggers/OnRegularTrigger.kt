package com.chattriggers.ctjs.triggers

class OnRegularTrigger(method: Any, triggerType: TriggerType) : OnTrigger(method, triggerType) {
    override fun trigger(vararg args: Any) {
        callMethod(*args)
    }
}
