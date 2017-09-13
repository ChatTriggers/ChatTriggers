package com.chattriggers.jsct;

import jdk.nashorn.api.scripting.NashornScriptEngine;
import lombok.Getter;
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
    public static JSCT instance;

    private NashornScriptEngine scriptEngine;

    @EventHandler
    public void init(FMLInitializationEvent e) {
        instance = this;

        this.scriptEngine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

    }



}

class Reference {
    public static final String MODID = "jsct";
    public static final String MODNAME = "jsChatTriggers";
    public static final String MODVERSION = "0.1-SNAPSHOT";
}