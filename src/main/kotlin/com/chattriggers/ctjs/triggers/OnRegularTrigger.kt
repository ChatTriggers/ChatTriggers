package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.utils.kotlin.External

@External
class OnRegularTrigger(method: Any, triggerType: TriggerType, owningModule: Module?, loader: ILoader) : OnTrigger(method, triggerType, owningModule, loader) {
    override fun trigger(vararg args: Any?) {
        callMethod(*args)
    }
}
