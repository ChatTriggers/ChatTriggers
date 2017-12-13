package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.Reference;
import com.chattriggers.ctjs.libs.EventLib;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class Config {
    @Getter
    @Setter
    private Configuration config;
    @Getter
    @Setter
    private File configFile;

    // configuration settings
    @Getter
    private Boolean printChatToConsole;
    @Getter
    private Boolean showCapes;
    @Getter
    private String consoleTheme;
    @Getter
    private Boolean customTheme;
    @Getter
    private String fg;
    @Getter
    private String bg;


    private void saveConfig() {
        this.printChatToConsole = this.config.getBoolean("print chat to console", "ct", true, "Chat printing to console");
        this.showCapes = this.config.getBoolean("show capes", "ct", true, "Show developer and creator capes");

        this.consoleTheme = this.config.getString("console theme", "ct",
            "default.dark", "Console theme", new String[]{
                "ashes.dark",
                "atelierforest.dark",
                "default.dark",
                "isotope.dark",
                "codeschool.dark",
                "gotham",
                "hybrid",
                "3024.light",
                "chalk.light",
                "blue",
                "slate",
                "red",
                "green",
                "aids"
            });

        this.customTheme = this.config.getBoolean("custom theme", "ct",
                false, "Use custom theme");
        this.bg = this.config.getString("background", "ct",
                "[21,21,21]", "Custom background color ([r,g,b])");
        this.fg = this.config.getString("foreground", "ct",
                "[208,208,208]", "Custom foreground color ([r,g,b])");

        this.config.save();
    }

    public void loadConfig() {
        this.config = new Configuration(configFile);
        saveConfig();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (EventLib.getModId(event).equals(Reference.MODID)) {
            saveConfig();
        }
    }
}
