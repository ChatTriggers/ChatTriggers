package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.loader.ModuleManager;
import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.utils.console.Console;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.Global;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.script.ScriptException;

public abstract class OnTrigger {
    @Getter
    protected Object method;
    @Getter
    protected Priority priority;
    @Getter
    protected TriggerType type;
    @Getter @Setter
    protected Module owningModule;

    private Global global;

    protected OnTrigger(Object method, TriggerType type) {
        this.method = method;
        this.priority = Priority.NORMAL;
        this.type = type;

        if (TriggerRegister.currentModule != null) {
            setOwningModule(TriggerRegister.currentModule);
        }

        this.register();
    }

    /**
     * Sets a triggers priority using {@link Priority}.
     * Highest runs first.
     * @param priority the priority of the trigger
     * @return the trigger for method chaining
     */
    public OnTrigger setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Registers a trigger based on its type.
     * This is done automatically with TriggerRegister.
     * @return the trigger for method chaining
     */
    public OnTrigger register() {
        this.type.addTrigger(this);
        return this;
    }

    /**
     * Unregisters a trigger.
     * @return the trigger for method chaining
     */
    public OnTrigger unregister() {
        this.type.removeTrigger(this);
        return this;
    }

    /**
     * @return boolean of if trigger is registered
     */
    public boolean isRegistered() {
        return this.type.containsTrigger(this);
    }

    protected void callMethod(Object... args) {
        try {
            if (this.method instanceof String) {
                callNamedMethod(args);
                return;
            }

            callActualMethod(args);
        } catch (Exception e) {
            Console.getInstance().printStackTrace(e, this);
            this.type.removeTrigger(this);
        }
    }

    protected void callActualMethod(Object... args) {

        ScriptObjectMirror som;

        if (this.method instanceof ScriptObjectMirror) {
            som = ((ScriptObjectMirror) this.method);
        } else {

            if (global == null) {
                global = ReflectionHelper.getPrivateValue(
                        NashornScriptEngine.class,
                        ((NashornScriptEngine) ModuleManager.getInstance().getScriptLoaders().get(0).getScriptEngine()),
                        "global"
                );
            }

            Object obj = ScriptObjectMirror.wrap(this.method, global);

            som = ((ScriptObjectMirror) obj);
        }

        som.call(som, args);
    }

    protected void callNamedMethod(Object... args) throws ScriptException, NoSuchMethodException {
        ModuleManager.getInstance().invokeFunction(
                ((String) method),
                args
        );
    }

    public abstract void trigger(Object... args);

    public enum TriggerResult {
        CANCEL
    }

    public enum Priority {
        //LOWEST IS RAN LAST
        LOWEST, LOW, NORMAL, HIGH, HIGHEST
    }
}
