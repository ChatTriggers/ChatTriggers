package com.chattriggers.jsct;

import com.chattriggers.jsct.loader.ScriptLoader;
import com.chattriggers.jsct.objects.DisplayHandler;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import javax.script.ScriptEngineManager;

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.MODVERSION, clientSideOnly = true)
public class JSCT {
    @Getter
    private static JSCT instance;

    @Getter
    private NashornScriptEngine scriptEngine;
    @Getter
    private ScriptLoader scriptLoader;
    @Getter
    private DisplayHandler displayHandler;

    @EventHandler
    public void init(FMLInitializationEvent e) {
        instance = this;
        this.scriptEngine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

        this.scriptLoader = new ScriptLoader();
        this.displayHandler = new DisplayHandler();

        MinecraftForge.EVENT_BUS.register(scriptLoader);
    }
}

class Reference {
    public static final String MODID = "jsct";
    public static final String MODNAME = "jsChatTriggers";
    public static final String MODVERSION = "0.1-SNAPSHOT";
}