package com.chattriggers.ctjs.utils.config;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class Config {
    public static Config getInstance() {
        return instance;
    }

    public static void setInstance(Config instance) {
        Config.instance = instance;
    }

    private static transient Config instance;

    @Getter
    private ConfigString modulesFolder;

    public ConfigBoolean getPrintChatToConsole() {
        return printChatToConsole;
    }

    private ConfigBoolean printChatToConsole;
    @Getter
    private ConfigBoolean showUpdatesInChat;
    public ConfigBoolean getShowUpdatesInChat() {return showUpdatesInChat;}
    private ConfigBoolean updateModulesOnBoot;

    public ConfigBoolean getUpdateModulesOnBoot() {
        return updateModulesOnBoot;
    }

    public ConfigBoolean getClearConsoleOnLoad() {
        return clearConsoleOnLoad;
    }

    private ConfigBoolean clearConsoleOnLoad;
    @Getter
    private ConfigBoolean openConsoleOnError;
    @Getter
    private ConfigStringSelector consoleTheme;
    @Getter
    private ConfigBoolean customTheme;
    @Getter
    private ConfigColor consoleForegroundColor;
    @Getter
    private ConfigColor consoleBackgroundColor;

    public Config() {
        instance = this;
    }

    public void init() {
        this.modulesFolder = new ConfigString(this.modulesFolder,"Directory", "./config/ChatTriggers/modules/", -110, 10);
        this.modulesFolder.setDirectory(true);
        this.printChatToConsole = new ConfigBoolean(this.printChatToConsole,"Print Chat To Console", true, -110, 65);
        this.showUpdatesInChat = new ConfigBoolean(this.showUpdatesInChat, "Show Update Messages In Chat", true, -110, 120);
        this.updateModulesOnBoot = new ConfigBoolean(this.updateModulesOnBoot, "Update Modules On Boot", true, -110, 175);


        this.clearConsoleOnLoad = new ConfigBoolean(this.clearConsoleOnLoad, "Clear Console On Load", true, 110, 10);
        this.openConsoleOnError = new ConfigBoolean(this.openConsoleOnError, "Auto Open Console On Error", false, 110, 65);
        this.customTheme = new ConfigBoolean(this.customTheme, "Custom Console Theme", false, 110, 120);

        String[] themes = new String[]{"default.dark", "ashes.dark", "atelierforest.dark", "isotope.dark", "codeschool.dark", "gotham", "hybrid", "3024.light", "chalk.light", "blue", "slate", "red", "green", "aids"};
        this.consoleTheme = new ConfigStringSelector(this.consoleTheme, "Console Theme", 0, themes, 110, 175);
        this.consoleForegroundColor = new ConfigColor(this.consoleForegroundColor,"Console Foreground Color", new Color(208, 208, 208), 110, 175);
        this.consoleBackgroundColor = new ConfigColor(this.consoleBackgroundColor,"Console Background Color", new Color(21, 21, 21), 110, 250);
    }

    public void updateHidden() {
        if (this.customTheme.value) {
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
