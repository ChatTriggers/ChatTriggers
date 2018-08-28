package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.CommandHandler
import com.chattriggers.ctjs.loader.ModuleManager
import com.chattriggers.ctjs.minecraft.imixins.IClientCommandHandler
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.objects.display.DisplayHandler
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.console.Console
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.client.ClientCommandHandler

object Reference {
    const val MODID = "ct.js"
    const val MODNAME = "ChatTriggers"
    const val MODVERSION = "@MOD_VERSION@"
    val SENTRYDSN = ("https://a69c5c01577c457b88434de9b995ceec:317ddf76172b4020b80f79befe536f98@sentry.io/259416"
            + "?release=" + MODVERSION
            + "&environment=" + (if (Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean) "development" else "production")
            + "&stacktrace.app.packages=com.chattriggers,jdk.nashorn"
            + "&uncaught.handler.enabled=false")

    private var isLoaded = true

    fun reload() {
        load(true)
    }

    @JvmOverloads
    fun load(updateCheck: Boolean = false) {
        if (!this.isLoaded) return
        this.isLoaded = false

        TriggerType.GAME_UNLOAD.triggerAll()
        TriggerType.WORLD_UNLOAD.triggerAll()

        ChatLib.chat("&cReloading ct.js scripts...")
        Thread {
            DisplayHandler.clearDisplays()
            GuiHandler.clearGuis()

            for (type in TriggerType.values())
                type.clearTriggers()

            CommandHandler.getCommandList().clear()
            (ClientCommandHandler.instance as IClientCommandHandler).removeCTCommands()
            ModuleManager.getInstance().unload()

            if (Config.getInstance().clearConsoleOnLoad.value)
                Console.getInstance().clearConsole()

            CTJS.setupConfig()

            ModuleManager.getInstance().load(updateCheck)

            ChatLib.chat("&aDone reloading scripts!")

            TriggerType.WORLD_LOAD.triggerAll()
            this.isLoaded = true
        }.start()
    }
}