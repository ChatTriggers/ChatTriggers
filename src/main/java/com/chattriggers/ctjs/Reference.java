//package com.chattriggers.ctjs;
//
//import com.chattriggers.ctjs.commands.CommandHandler;
//import com.chattriggers.ctjs.loader.ModuleManager;
//import com.chattriggers.ctjs.minecraft.libs.ChatLib;
//import com.chattriggers.ctjs.minecraft.imixins.IClientCommandHandler;
//import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler;
//import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler;
//import com.chattriggers.ctjs.triggers.TriggerType;
//import com.chattriggers.ctjs.utils.config.Config;
//import com.chattriggers.ctjs.utils.console.Console;
//import lombok.Getter;
//import net.minecraft.launchwrapper.Launch;
//import net.minecraftforge.client.ClientCommandHandler;
//
//public class Reference {
//    @Getter
//    private static Reference instance;
//
//    public static final String MODID = "ct.js";
//    public static final String MODNAME = "ChatTriggers";
//    public static final String MODVERSION = "0.16.5-SNAPSHOT";
//    public static final String SENTRYDSN = "https://a69c5c01577c457b88434de9b995ceec:317ddf76172b4020b80f79befe536f98@sentry.io/259416"
//                    + "?release=" + MODVERSION
//                    + "&environment=" + ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment") ? "development" : "production")
//                    + "&stacktrace.app.packages=com.chattriggers,jdk.nashorn"
//                    + "&uncaught.handler.enabled=false";
//
//    private boolean isLoaded = true;
//
//    Reference() {
//        instance = this;
//    }
//
//    public String getModID() {
//        return MODID;
//    }
//
//    public String getModName() {
//        return MODNAME;
//    }
//
//    public String getVersion() {
//        return MODVERSION;
//    }
//
//    public void reload() {
//        load(true);
//    }
//
//    public void load() {
//        load(false);
//    }
//
//    public void load(boolean updateCheck) {
//        if (!this.isLoaded) return;
//        this.isLoaded = false;
//
//        TriggerType.GAME_UNLOAD.triggerAll();
//        TriggerType.WORLD_UNLOAD.triggerAll();
//
//        ChatLib.chat("&cReloading ct.js scripts...");
//        new Thread(() -> {
//            DisplayHandler.getInstance().clearDisplays();
//            GuiHandler.getInstance().clearGuis();
//
//            for (TriggerType type : TriggerType.values())
//                type.clearTriggers();
//            CommandHandler.getInstance().getCommandList().clear();
//            ((IClientCommandHandler) ClientCommandHandler.instance).removeCTCommands();
//            ModuleManager.getInstance().unload();
//
//            if (Config.getInstance().getClearConsoleOnLoad().value)
//                Console.getInstance().clearConsole();
//
//            CTJS.INSTANCE.setupConfig();
//
//            ModuleManager.getInstance().load(updateCheck);
//
//            ChatLib.chat("&aDone reloading scripts!");
//
//            TriggerType.WORLD_LOAD.triggerAll();
//            this.isLoaded = true;
//        }).start();
//    }
//}
