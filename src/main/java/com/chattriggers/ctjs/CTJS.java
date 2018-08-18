//package com.chattriggers.ctjs;
//
//import com.chattriggers.ctjs.commands.CTCommand;
//import com.chattriggers.ctjs.commands.CommandHandler;
//import com.chattriggers.ctjs.loader.ModuleManager;
//import com.chattriggers.ctjs.loader.UriScheme;
//import com.chattriggers.ctjs.minecraft.libs.FileLib;
//import com.chattriggers.ctjs.minecraft.libs.Tessellator;
//import com.chattriggers.ctjs.minecraft.listeners.ChatListener;
//import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
//import com.chattriggers.ctjs.minecraft.listeners.WorldListener;
//import com.chattriggers.ctjs.minecraft.objects.CPS;
//import com.chattriggers.ctjs.minecraft.objects.Sound;
//import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler;
//import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler;
//import com.chattriggers.ctjs.minecraft.wrappers.Client;
//import com.chattriggers.ctjs.minecraft.wrappers.Player;
//import com.chattriggers.ctjs.triggers.TriggerType;
//import com.chattriggers.ctjs.utils.UpdateChecker;
//import com.chattriggers.ctjs.utils.capes.CapeHandler;
//import com.chattriggers.ctjs.utils.capes.LayerCape;
//import com.chattriggers.ctjs.utils.config.Config;
//import com.chattriggers.ctjs.utils.config.GuiConfig;
//import com.chattriggers.ctjs.utils.console.Console;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import io.sentry.Sentry;
//import io.sentry.event.UserBuilder;
//import lombok.Getter;
//import net.minecraft.client.renderer.entity.RenderPlayer;
//import net.minecraftforge.client.ClientCommandHandler;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.Mod.EventHandler;
//import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
//import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
//import org.apache.commons.codec.digest.DigestUtils;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Mod(modid = Reference.MODID,
//        name = Reference.MODNAME,
//        version = Reference.MODVERSION,
//        clientSideOnly = true)
//public class CTJS {
//    @Getter
//    @Mod.Instance
//    private static CTJS instance;
//
//    @Getter
//    private File assetsDir;
//    private File configLocation;
//    @Getter
//    private List<Sound> sounds = new ArrayList<>();
//
//    @EventHandler
//    public void preInit(FMLPreInitializationEvent event) {
//        this.configLocation = event.getModConfigurationDirectory();
//        setupConfig();
//
//        new Reference();
//        new Console();
//
//        File pictures = new File(event.getModConfigurationDirectory(), "ChatTriggers/images/");
//        pictures.mkdirs();
//        assetsDir = pictures;
//
//        UriScheme.installUriScheme();
//        UriScheme.createSocketListener();
//
//        Sentry.init(Reference.SENTRYDSN);
//
//        Sentry.getContext().setUser(
//                new UserBuilder()
//                        .setUsername(Player.getName())
//                        .setId(Player.getUUID())
//                        .build()
//        );
//
//        String sha256uuid = DigestUtils.sha256Hex(Player.getUUID());
//        FileLib.getUrlContent("http://167.99.3.229/tracker/?uuid=" + sha256uuid);
//    }
//
//    @EventHandler
//    public void init(FMLInitializationEvent event) {
//        new ChatListener();
//        new DisplayHandler();
//        new GuiHandler();
//        new CommandHandler();
//        new ModuleManager();
//        new CPS();
//        new Tessellator();
//
//        new UpdateChecker();
//
//        registerListeners();
//
//        registerHooks();
//
//        ModuleManager.getInstance().load(Config.getInstance().getUpdateModulesOnBoot().value);
//    }
//
//    @EventHandler
//    public void postInit(FMLPostInitializationEvent event) {
//        new CapeHandler();
//
//        for (RenderPlayer render : Client.getMinecraft().getRenderManager().getSkinMap().values()) {
//            render.addLayer(new LayerCape(render));
//        }
//    }
//
//    public void setupConfig() {
//        new GuiConfig();
//        new Config();
//        if (loadConfig()) Config.getInstance().init();
//    }
//
//    public void saveConfig() {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String path = new File(this.configLocation, "ChatTriggers.json").getAbsolutePath();
//        FileLib.write(path, gson.toJson(Config.getInstance()));
//    }
//
//    private boolean loadConfig() {
//        try {
//            Config.setInstance(new Gson().fromJson(new FileReader(new File(this.configLocation, "ChatTriggers.json")), Config.getInstance().getClass()));
//            return true;
//        } catch (FileNotFoundException exception) {
//            try {
//                new File(this.configLocation, "ChatTriggers.json").createNewFile();
//                Config.getInstance().init();
//                saveConfig();
//            } catch (IOException ioexception) {
//                ioexception.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    private void registerListeners() {
//        MinecraftForge.EVENT_BUS.register(new WorldListener());
//        MinecraftForge.EVENT_BUS.register(new ClientListener());
//    }
//
//    private void registerHooks() {
//        ClientCommandHandler.instance.registerCommand(new CTCommand());
//
//        Runtime.getRuntime().addShutdownHook(new Thread(TriggerType.GAME_UNLOAD::triggerAll));
//    }
//}
