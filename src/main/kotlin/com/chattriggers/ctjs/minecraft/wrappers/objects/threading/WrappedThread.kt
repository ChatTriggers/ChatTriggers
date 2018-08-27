package com.chattriggers.ctjs.minecraft.wrappers.objects.threading

import com.chattriggers.ctjs.utils.console.Console

class WrappedThread(task: Runnable) : Thread({
    try {
        task.run()
    } catch (e: Exception) {
        Console.getInstance().printStackTrace(e)
    }
})