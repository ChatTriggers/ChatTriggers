package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.CTCommand
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.engine.module.ModuleUpdater
import com.chattriggers.ctjs.loader.UriScheme
import com.chattriggers.ctjs.minecraft.libs.renderer.Image
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.listeners.MouseListener
import com.chattriggers.ctjs.minecraft.listeners.WorldListener
import com.chattriggers.ctjs.minecraft.objects.Sound
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.wrappers.CPS
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.UpdateChecker
import com.google.gson.Gson
import gg.essential.vigilance.Vigilance
import net.minecraftforge.common.MinecraftForge
import java.io.File
import java.net.URL
import java.net.URLConnection
import java.security.MessageDigest
import java.util.*
import kotlin.concurrent.thread

import net.minecraftforge.fml.common.Mod
//#if MC<=11202
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#else
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//$$ import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
//$$ import net.minecraftforge.event.RegisterCommandsEvent
//$$ import net.minecraft.commands.CommandSourceStack
//$$ import com.mojang.brigadier.CommandDispatcher
//$$ import com.mojang.brigadier.tree.CommandNode
//#endif

//#if MC<=11202
@Mod(
    modid = Reference.MODID,
    name = Reference.MODNAME,
    version = Reference.MODVERSION,
    clientSideOnly = true,
)
//#else
//$$ @Mod(Reference.MODID)
//$$ @Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//#endif
class CTJS {
    //#if MC>=11701 && FABRIC==0
    //$$ init {
    //$$     FMLJavaModLoadingContext.get().modEventBus.addListener(::init)
    //$$ }
    //#endif

    //#if MC<=11202
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
    //#elseif MC>=11701 && FABRIC==0
    //$$ fun init(event: FMLCommonSetupEvent) {
    //#else
    //$$ fun init() {
    //#endif
        listOf(
            WorldListener,
            CPS,
            ClientListener,
            UpdateChecker,
            MouseListener,
            ModuleUpdater
        ).forEach(MinecraftForge.EVENT_BUS::register)

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()

        Vigilance.initialize()
        Config.preload()

        // Ensure that reportHashedUUID always runs on a separate thread
        if (Config.threadedLoading) {
            thread {
                try {
                    ModuleManager.setup()
                    ModuleManager.entryPass()
                    reportHashedUUID()
                } catch (e: Exception) {
                    e.printTraceToConsole()
                    e.printStackTrace()
                }
            }
        } else {
            ModuleManager.setup()
            ModuleManager.entryPass()
            thread {
                reportHashedUUID()
            }
        }

        Runtime.getRuntime().addShutdownHook(Thread(TriggerType.GameUnload::triggerAll))

        //#if MC<=11202
        ClientCommandHandler.instance.registerCommand(CTCommand)
        //#endif
    }

    //#if MC>=11701
    //$$ @SubscribeEvent
    //$$ fun registerCommands(event: RegisterCommandsEvent) {
    //$$     commandDispatcher = event.dispatcher
    //$$     CTCommand.register(event.dispatcher)
    //$$     commandsPendingRegistration.forEach(event.dispatcher.root::addChild)
    //$$     commandsPendingRegistration.clear()
    //$$ }
    //#endif

    private fun reportHashedUUID() {
        val uuid = Player.getUUID().encodeToByteArray()
        val salt = (System.getProperty("user.name") ?: "").encodeToByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        val hashedUUID = md.digest(uuid)
        val hash = Base64.getUrlEncoder().encodeToString(hashedUUID)

        val url = "${WEBSITE_ROOT}/api/statistics/track?hash=$hash&version=${Reference.MODVERSION}"
        val connection = makeWebRequest(url)
        connection.getInputStream()
    }

    companion object {
        const val WEBSITE_ROOT = "https://www.chattriggers.com"
        const val modulesFolder = "./config/ChatTriggers/modules"
        val gson = Gson()
        val configLocation = File("./config")
        val assetsDir = File(configLocation, "ChatTriggers/images/").apply { mkdirs() }
        val sounds = mutableListOf<Sound>()
        val images = mutableListOf<Image>()

        fun makeWebRequest(url: String): URLConnection = URL(url).openConnection().apply {
            setRequestProperty("User-Agent", "Mozilla/5.0 (ChatTriggers)")
            connectTimeout = 3000
            readTimeout = 3000
        }

        //#if MC>=11701
        //$$ private val commandsPendingRegistration = mutableListOf<CommandNode<CommandSourceStack>>()
        //$$ internal var commandDispatcher: CommandDispatcher<CommandSourceStack>? = null
        //$$
        //$$ fun registerCommand(command: CommandNode<CommandSourceStack>) {
        //$$     if (commandDispatcher!!.root.children.any { it.name == command.name })
        //$$         throw IllegalArgumentException("Command with name ${command.name} already exists!")
        //$$     commandDispatcher?.root?.addChild(command) ?: commandsPendingRegistration.add(command)
        //$$ }
        //$$
        //$$ fun unregisterCommand(command: CommandNode<CommandSourceStack>) {
        //$$     val dispatcher = commandDispatcher
        //$$     if (dispatcher == null) {
        //$$         commandsPendingRegistration.remove(command)
        //$$     } else if (!dispatcher.root.children.remove(command)) {
        //$$         throw IllegalArgumentException("Encountered an error while removing command ${command.name}")
        //$$     }
        //$$ }
        //#endif
    }
}
