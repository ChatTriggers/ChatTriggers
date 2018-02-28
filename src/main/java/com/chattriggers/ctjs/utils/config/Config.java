package com.chattriggers.ctjs.utils.config;

import lombok.Getter;

import java.awt.*;

public class Config {
    @Getter
    private ConfigString modulesFolder;
    @Getter
    private ConfigBoolean printChatToConsole;
    @Getter
    private ConfigBoolean clearConsoleOnLoad;

    @Getter
    private ConfigStringSelector consoleTheme;

    @Getter
    private ConfigBoolean customTheme;
    @Getter
    private ConfigColor consoleForegroundColor;
    @Getter
    private ConfigColor consoleBackgroundColor;

    public void init() {
        this.modulesFolder = new ConfigString(this.modulesFolder,"Directory", "./config/ChatTriggers/modules/", -110, 10);
        this.modulesFolder.setDirectory(true);

        this.printChatToConsole = new ConfigBoolean(this.printChatToConsole,"Print Chat To Console", true, -110, 65);
        this.clearConsoleOnLoad = new ConfigBoolean(this.clearConsoleOnLoad, "Clear Console On Load", true, -110, 120);

        String[] themes = new String[]{"default.dark", "ashes.dark", "atelierforest.dark", "isotope.dark", "codeschool.dark", "gotham", "hybrid", "3024.light", "chalk.light", "blue", "slate", "red", "green", "aids"};
        this.consoleTheme = new ConfigStringSelector(this.consoleTheme, "Console Theme", 0, themes, 110, 65);

        this.customTheme = new ConfigBoolean(this.customTheme, "Custom Console Theme", false, 110, 10);

        this.consoleForegroundColor = new ConfigColor(this.consoleForegroundColor,"Console Foreground Color", new Color(208, 208, 208), 110, 65);
        this.consoleBackgroundColor = new ConfigColor(this.consoleBackgroundColor,"Console Background Color", new Color(21, 21, 21), 110, 140);
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
