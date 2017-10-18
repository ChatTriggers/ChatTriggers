package com.chattriggers.ctjs;

import com.chattriggers.ctjs.commands.CTCommand;
import com.chattriggers.ctjs.libs.MinecraftVars;
import com.chattriggers.ctjs.listeners.ChatListener;
import com.chattriggers.ctjs.listeners.WorldListener;
import com.chattriggers.ctjs.loader.ScriptLoader;
import com.chattriggers.ctjs.objects.DisplayHandler;
import com.chattriggers.ctjs.objects.KeyBind;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.config.Config;
import com.chattriggers.ctjs.utils.ImagesPack;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@Mod(modid = Reference.MODID,
        name = Reference.MODNAME,
        version = Reference.MODVERSION,
        guiFactory = "com.chattriggers.ctjs.utils.config.ConfigGuiFactory",
        clientSideOnly = true)
public class CTJS {
    @Getter
    private static CTJS instance;

    @Getter @Setter
    private ScriptEngine scriptEngine;
    @Getter @Setter
    private Invocable invocableEngine;
    @Getter @Setter
    private ScriptLoader scriptLoader;
    @Getter @Setter
    private DisplayHandler displayHandler;
    @Getter
    private ChatListener chatListener;
    @Getter
    private ImagesPack imagesPack;
    @Getter
    private File assetsDir;
    @Getter
    private Console console;

    @Getter
    private Config config;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;

        this.displayHandler = new DisplayHandler();
        this.chatListener = new ChatListener();
        this.console = new Console();

        initMain(true);

        registerListeners();

        registerHooks();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.injectResourcePack(event.getModConfigurationDirectory().toString());

        this.config = new Config();
        this.config.setConfigFile(new File(event.getModConfigurationDirectory().toString(), "ChatTriggers.cfg"));
        this.config.loadConfig();
    }

    public void initMain(boolean firstTime) {
        this.scriptEngine = new ScriptEngineManager(null).getEngineByName("nashorn");
        this.invocableEngine = ((Invocable) scriptEngine);

        if (!firstTime) {
            MinecraftForge.EVENT_BUS.unregister(scriptLoader);
            KeyBind.clearKeyBinds();
            TriggerType.clearAllTriggers();
        }

        this.displayHandler.clearDisplays();

        this.console.clearConsole();
        this.scriptLoader = new ScriptLoader();
        MinecraftForge.EVENT_BUS.register(scriptLoader);

        System.gc();
    }

    private void injectResourcePack(String path) {
        try {
            File pictures = new File(path, "ctjs/images/");
            Field field = FMLClientHandler.class.getDeclaredField("resourcePackList");
            field.setAccessible(true);

            List<IResourcePack> packs = (List<IResourcePack>) field.get(FMLClientHandler.instance());
            packs.add(imagesPack = new ImagesPack(pictures));
            pictures.mkdirs();
            assetsDir = pictures;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void registerListeners() {
        MinecraftForge.EVENT_BUS.register(this.displayHandler);
        MinecraftForge.EVENT_BUS.register(new WorldListener());
        MinecraftForge.EVENT_BUS.register(new ChatListener());
        MinecraftForge.EVENT_BUS.register(this.config);
    }

    private void registerHooks() {
        ClientRegistry.registerKeyBinding(MinecraftVars.keyLeftArrow);
        ClientRegistry.registerKeyBinding(MinecraftVars.keyRightArrow);
        ClientRegistry.registerKeyBinding(MinecraftVars.keyUpArrow);
        ClientRegistry.registerKeyBinding(MinecraftVars.keyDownArrow);

        ClientCommandHandler.instance.registerCommand(new CTCommand());
    }
}

