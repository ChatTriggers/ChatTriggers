package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.utils.console.Console;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Config {
    @Setter
    private transient File configFile;
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

        this.modulesFolder = new ConfigString("Directory", "./config/ChatTriggers/modules/");
        this.printChatToConsole = new ConfigBoolean("Print Chat To Console", true);

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
                });

        this.customTheme = new ConfigBoolean("Custom Console Theme", false);
        this.consoleForegroundColor = new ConfigColor("Console Foreground Color", new Color(208, 208, 208));
        this.consoleBackgroundColor = new ConfigColor("Console Background Color", new Color(21, 21, 21));
    }

    void addConfigOption(ConfigOption configOption) {
        this.configOptions.add(configOption);
    }

    public void save() {
        save(false);
    }

    private void save(Boolean secondChance) {
        try {
            Path path = Paths.get(configFile.getPath());
            ArrayList<String> lines = new ArrayList<>();
            lines.add(new Gson().toJson(this));
            Files.write(path, lines, Charset.forName("UTF-8"));
        } catch (FileNotFoundException exception) {
            createConfig();
            if (secondChance)
                Console.getConsole().printStackTrace(exception);
            else
                save(true);
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
        }
    }

    public void load() {
        try {
            Config json = new Gson().fromJson(new FileReader(configFile), Config.class);
            postLoad();
        } catch (FileNotFoundException exception) {
            createConfig();
        }
    }

    private void postLoad() {

    }

    private void createConfig() {
        try {
            configFile.createNewFile();
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
        }
    }
}
