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
import gg.essential.elementa.utils.elementaDev
import gg.essential.vigilance.Vigilance
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.File
import java.net.URL
import java.security.MessageDigest
import java.util.*
import kotlin.concurrent.thread

@Mod(
    modid = Reference.MODID,
    name = Reference.MODNAME,
    version = Reference.MODVERSION,
    clientSideOnly = true,
    modLanguage = "Kotlin",
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object CTJS {
    const val WEBSITE_ROOT = "https://www.chattriggers.com"
    val gson = Gson()
    val configLocation = File("./config")
    val assetsDir = File(configLocation, "ChatTriggers/images/").apply { mkdirs() }
    val sounds = mutableListOf<Sound>()
    val images = mutableListOf<Image>()

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        elementaDev = true

        listOf(
            WorldListener,
            CPS,
            GuiHandler,
            ClientListener,
            UpdateChecker,
            MouseListener,
            ModuleUpdater
        ).forEach(MinecraftForge.EVENT_BUS::register)

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        Vigilance.initialize()
        Config.preload()

        // Ensure that reportHashedUUID always runs on a separate thread
        if (Config.threadedLoading) {
            thread {
                try {
                    ModuleManager.entryPass()
                    reportHashedUUID()
                } catch (e: Exception) {
                    e.printTraceToConsole()
                    e.printStackTrace()
                }
            }
        } else {
            ModuleManager.entryPass()
            thread {
                reportHashedUUID()
            }
        }

        registerHooks()
    }

    private fun registerHooks() {
        ClientCommandHandler.instance.registerCommand(CTCommand)
        Runtime.getRuntime().addShutdownHook(Thread(TriggerType.GameUnload::triggerAll))
    }

    private fun reportHashedUUID() {
        val uuid = Player.getUUID().encodeToByteArray()
        val salt = (System.getProperty("user.name") ?: "").encodeToByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        val hashedUUID = md.digest(uuid)
        val hash = Base64.getUrlEncoder().encodeToString(hashedUUID)

        val url = "${WEBSITE_ROOT}/api/statistics/track?hash=$hash&version=${Reference.MODVERSION}"
        val connection = URL(url).openConnection().apply {
            setRequestProperty("User-Agent", "Mozilla/5.0")
            connectTimeout = 5000
            readTimeout = 5000
        }
        connection.getInputStream()
    }
}
