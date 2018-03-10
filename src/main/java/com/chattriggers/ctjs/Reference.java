package com.chattriggers.ctjs;

import net.minecraft.launchwrapper.Launch;

public class Reference {
    public static final String MODID = "ct.js";
    public static final String MODNAME = "ChatTriggers";
    public static final String MODVERSION = "0.11-SNAPSHOT";
    public static final String MAPPINGURL = "http://export.mcpbot.bspk.rs/mcp_stable_nodoc/22-1.8.9/mcp_stable_nodoc-22-1.8.9.zip";
    public static final String SENTRYDSN = "https://a69c5c01577c457b88434de9b995ceec:317ddf76172b4020b80f79befe536f98@sentry.io/259416"
                    + "?release=" + MODVERSION
                    + "&environment=" + ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") ? "development" : "production")
                    + "&stacktrace.app.packages=com.chattriggers,jdk.nashorn"
                    + "&uncaught.handler.enabled=false";
}
