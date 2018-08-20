package com.chattriggers.ctjs.loader;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.objects.KeyBind;
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler;
import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.triggers.TriggerType;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;

import javax.script.ScriptException;
import java.io.File;
import java.util.ArrayList;

public class ModuleManager {
    public static ModuleManager getInstance() {
        return instance;
    }

    private static ModuleManager instance;

    @Getter
    private ArrayList<ScriptLoader> scriptLoaders;
    @Getter
    private boolean isLoading;

    public ModuleManager() {
        instance = this;

        this.scriptLoaders = new ArrayList<>();
    }

    public void load(Boolean updateCheck) {
        this.isLoading = true;
        scriptLoaders.add(new JSScriptLoader());

        for (ScriptLoader sl : scriptLoaders) {
            sl.preLoad();
        }

        for (ScriptLoader sl : scriptLoaders) {
            sl.loadModules(updateCheck, true);
        }

        for (ScriptLoader sl : scriptLoaders) {
            sl.postLoad();
        }


        System.gc();
        this.isLoading = false;

        TriggerType.GAME_LOAD.triggerAll();
    }

    public void unload() {
        for (ScriptLoader sl : scriptLoaders) {
            MinecraftForge.EVENT_BUS.unregister(sl);
        }

        scriptLoaders.clear();

        KeyBind.clearKeyBinds();
        TriggerType.clearAllTriggers();
        DisplayHandler.INSTANCE.clearDisplays();
    }

    public ArrayList<Module> getModules() {
        ArrayList<Module> modules = new ArrayList<>();

        for (ScriptLoader sl : scriptLoaders) {
            modules.addAll(sl.loadModules(false, false));
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

    public void importModule(String name, boolean required) {
        ChatLib.chat("&7Importing " + name);

        if (!required) {
            new Thread(() -> doImport(name)).start();
        } else {
            doImport(name);
        }
    }

    private void doImport(String name) {
        JSScriptLoader scriptLoader = (JSScriptLoader) scriptLoaders.get(0);
        if (scriptLoader.downloadModule(name,true)) {
            scriptLoader.loadModule(new File(scriptLoader.modulesDir, name), false);
            ChatLib.chat("&6Successfully imported " + name + "!");
        }
    }
}
