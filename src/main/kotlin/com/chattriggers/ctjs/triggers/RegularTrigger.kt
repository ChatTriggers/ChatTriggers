package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader

class RegularTrigger(method: Any, triggerType: TriggerType, loader: ILoader) :
    Trigger(method, triggerType, loader) {
    override fun trigger(args: Array<out Any?>) {
        callMethod(args)
    }
}
