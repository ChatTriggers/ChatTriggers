package com.chattriggers.ctjs.loader;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.imports.Module;
import com.chattriggers.ctjs.objects.KeyBind;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.capes.DLCape;
import net.minecraftforge.common.MinecraftForge;

import javax.script.ScriptException;
import java.util.ArrayList;

public class ModuleManager {
    private ArrayList<ScriptLoader> scriptLoaders;

    public ModuleManager() {
        this.scriptLoaders = new ArrayList<>();

        DLCape.getCapes();
    }

    public void load() {
        scriptLoaders.add(new JSScriptLoader());

        for (ScriptLoader sl : scriptLoaders) {
            sl.preLoad();
        }

        for (ScriptLoader sl : scriptLoaders) {
            sl.loadModules();
        }

        for (ScriptLoader sl : scriptLoaders) {
            sl.postLoad();
        }


        System.gc();
    }

    public void unload() {
        for (ScriptLoader sl : scriptLoaders) {
            MinecraftForge.EVENT_BUS.unregister(sl);
        }

        scriptLoaders.clear();

        KeyBind.clearKeyBinds();
        TriggerType.clearAllTriggers();
        CTJS.getInstance().getDisplayHandler().clearDisplays();
        CTJS.getInstance().getConsole().clearConsole();
    }

    public ArrayList<Module> getModules() {
        ArrayList<Module> modules = new ArrayList<>();

        for (ScriptLoader sl : scriptLoaders) {
            modules.addAll(sl.loadModules());
        }

        return modules;
    }

    public void invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        for (ScriptLoader sl : scriptLoaders) {
            sl.getInvocableEngine().invokeFunction(name, args);
        }
    }

    public Object eval(String script) throws ScriptException {
        //TODO: Make this elegant, I'm not sure yet how to.
        return scriptLoaders.get(0).getScriptEngine().eval(script);
    }
}
