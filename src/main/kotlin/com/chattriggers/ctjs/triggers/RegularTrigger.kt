package com.chattriggers.ctjs.triggers

class RegularTrigger(method: Any, triggerType: TriggerType) : Trigger(method, triggerType) {
    override fun trigger(args: Array<out Any?>) {
        callMethod(args)
    }
}
