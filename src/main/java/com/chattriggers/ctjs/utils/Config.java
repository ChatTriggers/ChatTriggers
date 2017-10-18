package com.chattriggers.ctjs.utils;
import com.chattriggers.ctjs.Reference;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class Config {
    @Getter @Setter
    private Configuration config;
    @Getter @Setter
    private File configFile;

    // configuration settings
    @Getter
    private Boolean printChatToConsole;

    public void saveConfig() {
        this.printChatToConsole = this.config.getBoolean("printChatToConsole", "console", true, "Chat printing to console");

        this.config.save();
    }

    public void loadConfig() {
        this.config = new Configuration(configFile);
        saveConfig();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(Reference.MODID)) {
            saveConfig();
        }
    }
}
