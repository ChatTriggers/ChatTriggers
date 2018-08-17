package com.chattriggers.ctjs.minecraft.wrappers.objects.threading;

import com.chattriggers.ctjs.utils.console.Console;

public class WrappedThread extends Thread {
    public WrappedThread(Runnable task) {
        super(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Console.getInstance().printStackTrace(e);

            }
        });
    }
}
