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
import com.chattriggers.ctjs.minecraft.wrappers.CPS
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.EventType
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.UpdateChecker
import com.google.gson.Gson
import net.minecraft.client.Minecraft
import java.io.File
import java.net.URL
import java.net.URLConnection
import java.security.MessageDigest
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.concurrent.thread

//#if MC<=11202
import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif

//#if MC>=11701
//$$ import com.mojang.brigadier.CommandDispatcher
//$$ import com.mojang.brigadier.tree.CommandNode
//$$ import net.minecraft.commands.CommandSourceStack
//#endif

//#if MC>=11701 && FORGE
//$$ import net.minecraftforge.api.distmarker.Dist
//$$ import net.minecraftforge.event.RegisterCommandsEvent
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent
//$$ import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//#endif

//#if FORGE
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
//#else
//$$ import net.fabricmc.api.ClientModInitializer
//#endif

//#if FORGE
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
//#else
//$$ class CTJS : ClientModInitializer {
//#endif

    //#if MC<=11202
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        //#elseif MC>=11701 && FABRIC==0
        //$$ fun init(event: FMLCommonSetupEvent) {
        //#else
        //$$ override fun onInitializeClient() {
        //#endif

        //#if MC<=11202
        Minecraft.getMinecraft().addScheduledTask {
        //#elseif MC>=11701
        //$$ Minecraft.getInstance().tell {
        //#endif
            Config.preload()
            CTCommand.register()

            ModuleUpdater
            UpdateChecker
            MouseListener
            CPS
            WorldListener

            //#if FORGE
            MinecraftForge.EVENT_BUS.register(ClientListener)
            //#else
            //$$ ClientListener
            //#endif

            UriScheme.installUriScheme()
            UriScheme.createSocketListener()

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
        }
    }

    //#if MC>=11701 && FABRIC==0
    //$$ @SubscribeEvent
    //$$ fun registerCommands(event: RegisterCommandsEvent) {
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
        private val eventListeners = mutableMapOf<EventType, CopyOnWriteArrayList<(Array<Any>?) -> Unit>>()

        fun addEventListener(category: EventType, listener: (Array<Any>?) -> Unit) : () -> Unit {
            eventListeners.getOrPut(category) { CopyOnWriteArrayList() }.add(listener)
            return { eventListeners[category]?.remove(listener) }
        }

        fun getEventListeners(type: EventType): List<(Array<Any>?) -> Unit> = eventListeners[type] ?: listOf()

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
