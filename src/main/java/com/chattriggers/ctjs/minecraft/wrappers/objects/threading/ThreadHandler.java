package com.chattriggers.ctjs.minecraft.wrappers.objects.threading;

import java.util.ArrayList;
import java.util.List;

public class ThreadHandler {
    private List<WrappedThread> activeThreads;

    public ThreadHandler() {
        this.activeThreads = new ArrayList<>();
    }

    public void addThread(WrappedThread thread) {
        this.activeThreads.add(thread);
    }

    public void removeThread(WrappedThread thread) {
        activeThreads.removeIf(thread1 -> thread1 == thread);
    }

    public void stopThreads() {
        activeThreads.forEach(Thread::interrupt);

        activeThreads.clear();
    }
}
