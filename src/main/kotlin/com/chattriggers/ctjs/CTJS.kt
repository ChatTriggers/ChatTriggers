package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.CTCommand
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.loader.UriScheme
import com.chattriggers.ctjs.minecraft.libs.renderer.Image
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.listeners.WorldListener
import com.chattriggers.ctjs.minecraft.objects.Sound
import com.chattriggers.ctjs.minecraft.wrappers.CPS
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.Initializer
import com.google.gson.Gson
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.CommandNode
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.ServerCommandSource
import java.io.File

object CTJS : ClientModInitializer {
    const val WEBSITE_ROOT = "https://www.chattriggers.com"
    val gson = Gson()
    val configLocation = File("./config")
    val assetsDir = File(configLocation, "ChatTriggers/images/").apply { mkdirs() }
    val sounds = mutableListOf<Sound>()
    val images = mutableListOf<Image>()

    private val commandsPendingRegistration = mutableListOf<CommandNode<ServerCommandSource>>()

    internal var commandDispatcher: CommandDispatcher<ServerCommandSource>? = null

    override fun onInitializeClient() {
        listOf(
            WorldListener,
            CPS,
            ClientListener,
        ).forEach(Initializer::onInitialize)

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()

        Config.initialize()

        // TODO: Ideally we would do this earlier, right before ASM injection
        ModuleManager.setup()

        Reference.conditionalThread {
            ModuleManager.entryPass()
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            commandDispatcher = dispatcher
            CTCommand.register(dispatcher)

            commandsPendingRegistration.forEach(dispatcher.root::addChild)
            commandsPendingRegistration.clear()
        }

        Runtime.getRuntime().addShutdownHook(Thread(TriggerType.GameUnload::triggerAll))
    }

    fun registerCommand(command: CommandNode<ServerCommandSource>) {
        commandDispatcher?.root?.addChild(command) ?: commandsPendingRegistration.add(command)
    }

    fun unregisterCommand(command: CommandNode<ServerCommandSource>) {
        val dispatcher = commandDispatcher
        if (dispatcher == null) {
            commandsPendingRegistration.remove(command)
        } else if (!dispatcher.root.children.remove(command)) {
            throw IllegalArgumentException("Encountered an error while removing command ${command.name}")
        }
    }
}
