package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.CTCommand
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.launch.mixins.transformers.MouseMixin
import com.chattriggers.ctjs.loader.UriScheme
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.listeners.WorldListener
import com.chattriggers.ctjs.minecraft.objects.Sound
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.wrappers.CPS
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.Initializer
import com.chattriggers.ctjs.utils.UpdateChecker
import com.google.gson.Gson
import com.mojang.brigadier.CommandDispatcher
import gg.essential.vigilance.Vigilance
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import java.io.File

object CTJS : ClientModInitializer {
    const val WEBSITE_ROOT = "https://www.chattriggers.com"
    val gson = Gson()
    val configLocation = File("./config")
    val assetsDir = File(configLocation, "ChatTriggers/images/").apply { mkdirs() }
    val sounds = mutableListOf<Sound>()

    internal lateinit var commandDispatcher: CommandDispatcher<ServerCommandSource>

    override fun onInitializeClient() {
        listOf(
            WorldListener,
            CPS,
            GuiHandler,
            ClientListener,
            UpdateChecker,
        ).forEach(Initializer::onInitialize)

        MouseMixin.registerTriggerListeners()

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()

        Vigilance.initialize()
        Config.preload()

        Reference.conditionalThread {
            ModuleManager.entryPass()
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            commandDispatcher = dispatcher
            CTCommand.register(dispatcher)
        }

        Runtime.getRuntime().addShutdownHook(Thread(TriggerType.GameUnload::triggerAll))
    }
}
