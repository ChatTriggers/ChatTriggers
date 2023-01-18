package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity

class EntityRenderTrigger(method: Any, triggerType: TriggerType, loader: ILoader): Trigger(method, triggerType, loader) {
    private var triggerClasses: List<Class<*>> = emptyList()

    /**
     * Alias for `setEntityClasses([class_])`
     */
    fun setEntityClass(class_: Class<*>?) = setEntityClasses(listOfNotNull(class_))

    /**
     * Sets which classes this Trigger should run for. If the list is empty, it runs
     * for every entity class.
     *
     * @param classes The classes for which this trigger should run for
     * @return This trigger object for entity chaining
     */
    fun setEntityClasses(classes: List<Class<*>>) = apply { triggerClasses = classes }

    override fun trigger(args: Array<out Any?>) {
        val entity = args.getOrNull(0) as? Entity
            ?: error("Expected first argument of renderEntity trigger to be instance of Entity<*> class")
        if(triggerClasses.isEmpty() || triggerClasses.any { it.isInstance(entity.entity) })
            callMethod(args)
    }
}