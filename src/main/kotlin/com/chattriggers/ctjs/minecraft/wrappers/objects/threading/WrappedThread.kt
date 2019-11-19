package com.chattriggers.ctjs.minecraft.wrappers.objects.threading

import com.chattriggers.ctjs.engine.langs.js.JSContextFactory
import com.chattriggers.ctjs.engine.langs.js.JSLoader
import org.mozilla.javascript.Context

class WrappedThread(task: Runnable) : Thread({
    try {
        JSContextFactory.enterContext()
        task.run()
        Context.exit()
    } catch (e: Throwable) {
        JSLoader.console.printStackTrace(e)
    }
})