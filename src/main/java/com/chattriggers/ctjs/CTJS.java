package com.chattriggers.ctjs;

import com.chattriggers.ctjs.commands.CTCommand;
import com.chattriggers.ctjs.minecraft.handlers.CommandHandler;
import com.chattriggers.ctjs.minecraft.handlers.GuiHandler;
import com.chattriggers.ctjs.minecraft.listeners.ChatListener;
import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import com.chattriggers.ctjs.minecraft.listeners.WorldListener;
import com.chattriggers.ctjs.loader.ModuleManager;
import com.chattriggers.ctjs.minecraft.handlers.DisplayHandler;
import com.chattriggers.ctjs.minecraft.objects.CPS;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.ImagesPack;
import com.chattriggers.ctjs.utils.config.Config;
import com.chattriggers.ctjs.utils.console.Console;
import io.sentry.Sentry;
import io.sentry.event.UserBuilder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@Mod(modid = Reference.MODID,
        name = Reference.MODNAME,
        version = Reference.MODVERSION,
        clientSideOnly = true)
public class CTJS {
    @Getter
    private static CTJS instance;

    @Getter @Setter
    private DisplayHandler displayHandler;
    @Getter
    private GuiHandler guiHandler;
    @Getter
    private CommandHandler commandHandler;
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
    @Getter
    private ModuleManager moduleManager;
    @Getter
    private CPS cps;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        this.displayHandler = new DisplayHandler();
        this.guiHandler = new GuiHandler();
        this.commandHandler = new CommandHandler();
        this.chatListener = new ChatListener();
        this.moduleManager = new ModuleManager();
        this.cps = new CPS();

        registerListeners();

        registerHooks();

        moduleManager.load(true);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        this.console = new Console();

        Sentry.init(Reference.SENTRYDSN);

        Sentry.getContext().setUser(
            new UserBuilder()
                .setUsername(Player.getName())
                .setId(Player.getUUID())
                .build()
        );

        this.injectResourcePack(event.getModConfigurationDirectory().toString());

        this.config = new Config();
        this.config.init();
        this.config.setConfigFile(new File(event.getModConfigurationDirectory().toString(), "ChatTriggers.json"));
        this.config.save();
        this.config.load();
    }

    private void injectResourcePack(String path) {
        try {
            File pictures = new File(path, "ChatTriggers/images/");
            Field field = FMLClientHandler.class.getDeclaredField("resourcePackList");
            field.setAccessible(true);

            List<IResourcePack> packs = (List<IResourcePack>) field.get(FMLClientHandler.instance());
            imagesPack = new ImagesPack(pictures);
            packs.add(imagesPack);
            pictures.mkdirs();
            assetsDir = pictures;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void registerListeners() {
        MinecraftForge.EVENT_BUS.register(new WorldListener());
        MinecraftForge.EVENT_BUS.register(new ClientListener());

        MinecraftForge.EVENT_BUS.register(this.displayHandler);
        MinecraftForge.EVENT_BUS.register(this.guiHandler);
        MinecraftForge.EVENT_BUS.register(this.chatListener);
        MinecraftForge.EVENT_BUS.register(this.config);
        MinecraftForge.EVENT_BUS.register(this.cps);
    }

    private void registerHooks() {
        ClientCommandHandler.instance.registerCommand(new CTCommand());

        Runtime.getRuntime().addShutdownHook(new Thread(TriggerType.GAME_UNLOAD::triggerAll));
    }
}

