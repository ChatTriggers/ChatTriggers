package com.chattriggers.ctjs;

import com.chattriggers.ctjs.commands.CTCommand;
import com.chattriggers.ctjs.commands.CommandHandler;
import com.chattriggers.ctjs.loader.ModuleManager;
import com.chattriggers.ctjs.minecraft.libs.FileLib;
import com.chattriggers.ctjs.minecraft.libs.Tessellator;
import com.chattriggers.ctjs.minecraft.listeners.ChatListener;
import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import com.chattriggers.ctjs.minecraft.listeners.WorldListener;
import com.chattriggers.ctjs.minecraft.objects.CPS;
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler;
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.ImagesPack;
import com.chattriggers.ctjs.utils.UpdateChecker;
import com.chattriggers.ctjs.utils.config.Config;
import com.chattriggers.ctjs.utils.config.GuiConfig;
import com.chattriggers.ctjs.utils.console.Console;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sentry.Sentry;
import io.sentry.event.UserBuilder;
import lombok.Getter;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Mod(modid = Reference.MODID,
        name = Reference.MODNAME,
        version = Reference.MODVERSION,
        clientSideOnly = true)
public class CTJS {
    @Getter
    @Mod.Instance
    private static CTJS instance;

    @Getter
    private ChatListener chatListener;
    @Getter
    private ImagesPack imagesPack;
    @Getter
    private File assetsDir;
    @Getter
    private Tessellator tessellator;

    private File configLocation;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        new DisplayHandler();
        new GuiHandler();
        new CommandHandler();
        this.chatListener = new ChatListener();
        new ModuleManager();
        new CPS();
        this.tessellator = new Tessellator();

        new UpdateChecker();

        registerListeners();

        registerHooks();

        ModuleManager.getInstance().load(true);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        new Console();

        Sentry.init(Reference.SENTRYDSN);

        Sentry.getContext().setUser(
                new UserBuilder()
                        .setUsername(Player.getName())
                        .setId(Player.getUUID())
                        .build()
        );

        String sha256uuid = DigestUtils.sha256Hex(Player.getUUID());
        FileLib.getUrlContent("http://167.99.3.229/tracker/?uuid=" + sha256uuid);

        this.injectResourcePack(event.getModConfigurationDirectory().toString());

        this.configLocation = event.getModConfigurationDirectory();

        setupConfig();
    }

    public void setupConfig() {
        new GuiConfig();
        new Config();
        if (loadConfig()) Config.getInstance().init();
    }

    public void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String path = new File(this.configLocation, "ChatTriggers.json").getAbsolutePath();
        FileLib.write(path, gson.toJson(Config.getInstance()));
    }

    private boolean loadConfig() {
        try {
            Config.setInstance(new Gson().fromJson(new FileReader(new File(this.configLocation, "ChatTriggers.json")), Config.getInstance().getClass()));
            return true;
        } catch (FileNotFoundException exception) {
            try {
                new File(this.configLocation, "ChatTriggers.json").createNewFile();
                Config.getInstance().init();
                saveConfig();
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
        return false;
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

        MinecraftForge.EVENT_BUS.register(this.chatListener);
    }

    private void registerHooks() {
        ClientCommandHandler.instance.registerCommand(new CTCommand());

        Runtime.getRuntime().addShutdownHook(new Thread(TriggerType.GAME_UNLOAD::triggerAll));
    }
}
