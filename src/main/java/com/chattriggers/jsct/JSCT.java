package com.chattriggers.jsct;

import com.chattriggers.jsct.loader.ScriptLoader;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import javax.script.ScriptEngineManager;

/**
 * Copyright (c) FalseHonesty 2017
 */

@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.MODVERSION)
public class JSCT {
    @Getter
    private static JSCT instance;

    @Getter
    private NashornScriptEngine scriptEngine;
    @Getter
    private ScriptLoader scriptLoader;

    @EventHandler
    public void init(FMLInitializationEvent e) {
        instance = this;
        this.scriptEngine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

        this.scriptLoader = new ScriptLoader();

        MinecraftForge.EVENT_BUS.register(scriptLoader);
    }



}

class Reference {
    public static final String MODID = "jsct";
    public static final String MODNAME = "jsChatTriggers";
    public static final String MODVERSION = "0.1-SNAPSHOT";
}