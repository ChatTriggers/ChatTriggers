package com.chattriggers.ctjs.engine.langs.js

import com.reevajs.reeva.core.Agent
import com.reevajs.reeva.core.realm.Realm
import com.reevajs.reeva.utils.expect
import java.util.*
import java.util.concurrent.LinkedBlockingDeque

class EventLoop(private val agent: Agent) {
    private val initializationThread = Thread.currentThread()

    private val taskQueue = LinkedBlockingDeque<Task>()
    private val scheduledTasks = Collections.synchronizedSortedSet(
        TreeSet<DelayedTask> { a, b -> a.targetTime.compareTo(b.targetTime) }
    )

    fun tick() {
        expect(Thread.currentThread() == initializationThread)

        val scheduledTask = if (scheduledTasks.isNotEmpty()) scheduledTasks.first() else null

        if (scheduledTask != null && scheduledTasks.first().targetTime <= System.currentTimeMillis()) {
            scheduledTasks.remove(scheduledTask)
            synchronized(scheduledTask.realm) {
                scheduledTask.task()
            }
        } else if (taskQueue.isNotEmpty()) {
            val task = taskQueue.removeFirst()
            synchronized(task.realm) {
                task.task()
            }
        }

        // TODO: Synchronize on the task's realm?
        agent.microtaskQueue.checkpoint()
    }

    fun submitTask(realm: Realm, task: () -> Unit) {
        synchronized(realm) {
            taskQueue.addLast(Task(realm, task))
        }
    }

    fun scheduleTask(realm: Realm, delay: Long, task: () -> Unit) {
        scheduledTasks.add(DelayedTask(realm, System.currentTimeMillis() + delay, task))
    }

    data class Task(val realm: Realm, val task: () -> Unit)

    data class DelayedTask(val realm: Realm, val targetTime: Long, val task: () -> Unit)
}