package com.chattriggers.ctjs.utils.config

import java.awt.Color

object Config {
    var modulesFolder = ConfigString()
    var printChatToConsole = ConfigBoolean()
    var showUpdatesInChat = ConfigBoolean()
    var updateModulesOnBoot = ConfigBoolean()
    var clearConsoleOnLoad = ConfigBoolean()
    var openConsoleOnError = ConfigBoolean()
    var consoleTheme = ConfigStringSelector()
    var customTheme = ConfigBoolean()
    var consoleForegroundColor = ConfigColor()
    var consoleBackgroundColor = ConfigColor()

    fun init() {
        this.modulesFolder = ConfigString(this.modulesFolder, "Directory", "./config/ChatTriggers/modules/", -110, 10)
        this.modulesFolder.isDirectory = true
        this.printChatToConsole = ConfigBoolean(this.printChatToConsole, "Print Chat To Console", true, -110, 65)
        this.showUpdatesInChat = ConfigBoolean(this.showUpdatesInChat, "Show Update Messages In Chat", true, -110, 120)
        this.updateModulesOnBoot = ConfigBoolean(this.updateModulesOnBoot, "Update Modules On Boot", true, -110, 175)


        this.clearConsoleOnLoad = ConfigBoolean(this.clearConsoleOnLoad, "Clear Console On Load", true, 110, 10)
        this.openConsoleOnError = ConfigBoolean(this.openConsoleOnError, "Auto Open Console On Error", false, 110, 65)
        this.customTheme = ConfigBoolean(this.customTheme, "Custom Console Theme", false, 110, 120)

        val themes = arrayOf("default.dark", "ashes.dark", "atelierforest.dark", "isotope.dark", "codeschool.dark", "gotham", "hybrid", "3024.light", "chalk.light", "blue", "slate", "red", "green", "aids")
        this.consoleTheme = ConfigStringSelector(this.consoleTheme, "Console Theme", 0, themes, 110, 175)
        this.consoleForegroundColor = ConfigColor(this.consoleForegroundColor, "Console Foreground Color", Color(208, 208, 208), 110, 175)
        this.consoleBackgroundColor = ConfigColor(this.consoleBackgroundColor, "Console Background Color", Color(21, 21, 21), 110, 250)
    }

    fun updateHidden() {
        if (this.customTheme!!.value) {
            this.consoleForegroundColor?.hidden = false
            this.consoleBackgroundColor?.hidden = false
            this.consoleTheme?.hidden = true
        } else {
            this.consoleForegroundColor?.hidden = true
            this.consoleBackgroundColor?.hidden = true
            this.consoleTheme?.hidden = false
        }
    }
}

