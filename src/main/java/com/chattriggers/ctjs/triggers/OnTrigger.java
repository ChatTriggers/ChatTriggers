//package com.chattriggers.ctjs.triggers;
//
//import com.chattriggers.ctjs.engine.ILoader;
//import com.chattriggers.ctjs.engine.module.Module;
//import jdk.nashorn.internal.objects.Global;
//
//public abstract class OnTrigger {
//    protected Object method;
//    protected Priority priority;
//    protected TriggerType type;
//    protected Module owningModule;
//    protected ILoader loader;
//
//    public Module getOwningModule() {
//        return owningModule;
//    }
//
//    public void setOwningModule(Module owningModule) {
//        this.owningModule = owningModule;
//    }
//
//    public Object getMethod() {
//        return method;
//    }
//
//    public Priority getPriority() {
//        return priority;
//    }
//
//    public TriggerType getType() {
//        return type;
//    }
//
//    private Global global;
//
//    protected OnTrigger(Object method, TriggerType type, ILoader loader) {
//        this.method = method;
//        this.priority = Priority.NORMAL;
//        this.type = type;
//        this.loader = loader;
//
//        this.register();
//    }
//
//    /**
//     * Sets a triggers priority using {@link Priority}.
//     * Highest runs first.
//     * @param priority the priority of the trigger
//     * @return the trigger for method chaining
//     */
//    public OnTrigger setPriority(Priority priority) {
//        this.priority = priority;
//        return this;
//    }
//
//    /**
//     * Registers a trigger based on its type.
//     * This is done automatically with TriggerRegister.
//     * @return the trigger for method chaining
//     */
//    public OnTrigger register() {
//        this.loader.addTrigger(this);
//        return this;
//    }
//
//    /**
//     * Unregisters a trigger.
//     * @return the trigger for method chaining
//     */
//    public OnTrigger unregister() {
//        this.loader.removeTrigger(this);
//        return this;
//    }
//
//    /**
//     * @return boolean of if trigger is registered
//     */
//    public boolean isRegistered() {
//        return true;
//    }
//
//    protected void callMethod(Object... args) {
//        this.loader.trigger(this, this.method, args);
//    }
//
//    public abstract void trigger(Object... args);
//
//    public enum TriggerResult {
//        CANCEL
//    }
//
//    public enum Priority {
//        //LOWEST IS RAN LAST
//        LOWEST, LOW, NORMAL, HIGH, HIGHEST
//    }
//}
