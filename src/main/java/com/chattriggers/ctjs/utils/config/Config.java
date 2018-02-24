package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.FileLib;
import com.chattriggers.ctjs.utils.console.Console;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Config {
    @Setter
    private transient File configFile;
    @Getter
    private transient ArrayList<ConfigOption> configOptions;


    @Getter
    private ConfigString modulesFolder;
    @Getter
    private ConfigBoolean printChatToConsole;

    @Getter
    private ConfigStringSelector consoleTheme;

    @Getter
    private ConfigBoolean customTheme;
    @Getter
    private ConfigColor consoleForegroundColor;
    @Getter
    private ConfigColor consoleBackgroundColor;


    public Config() {
        this.configOptions = new ArrayList<>();
    }

    public void init() {
        this.configOptions.clear();

        this.modulesFolder = new ConfigString("Directory", "./config/ChatTriggers/modules/", -110, 10);
        this.printChatToConsole = new ConfigBoolean("Print Chat To Console", true, -110, 65);

        this.consoleTheme = new ConfigStringSelector("Console Theme", 0,
                new String[]{
                        "default.dark",
                        "ashes.dark",
                        "atelierforest.dark",
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
                }, 110, 65);

        this.customTheme = new ConfigBoolean("Custom Console Theme", false, 110, 10);
        this.consoleForegroundColor = new ConfigColor("Console Foreground Color", new Color(208, 208, 208), 110, 65);
        this.consoleBackgroundColor = new ConfigColor("Console Background Color", new Color(21, 21, 21), 110, 140);
    }

    public void addConfigOption(ConfigOption configOption) {
        this.configOptions.add(configOption);
    }

    public void save() {
        FileLib.write(configFile.getAbsolutePath(), new Gson().toJson(this));
    }

    public void load() {
        try {
            // TODO temporary bypass of loading because it doesn't work yet
            Config json = new Gson().fromJson(new FileReader(configFile), Config.class);
        } catch (FileNotFoundException exception) {
            createConfig();
        }
    }

    private void createConfig() {
        try {
            configFile.createNewFile();
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
        }
    }

    public void updateHidden() {
        if (this.customTheme.getValue()) {
            this.consoleForegroundColor.hidden = false;
            this.consoleBackgroundColor.hidden = false;
            this.consoleTheme.hidden = true;
        } else {
            this.consoleForegroundColor.hidden = true;
            this.consoleBackgroundColor.hidden = true;
            this.consoleTheme.hidden = false;
        }
    }
}
