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
import com.chattriggers.ctjs.triggers.ForgeTrigger
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.UpdateChecker
import com.chattriggers.ctjs.utils.console.LogType
import com.google.gson.Gson
import gg.essential.vigilance.Vigilance
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.File
import java.net.URL
import java.net.URLConnection
import java.security.KeyStore
import javax.net.ssl.HttpsURLConnection
import java.security.MessageDigest
import java.util.*
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import kotlin.concurrent.thread
import kotlin.math.log

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
    val images = mutableSetOf<Image>()

    @JvmStatic
    val sslContext by lazy {
        try {
            val myKeyStore = KeyStore.getInstance("JKS")
            myKeyStore.load(CTJS::class.java.getResourceAsStream("/ctjskeystore.jks"), "changeit".toCharArray())
            val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            kmf.init(myKeyStore, null)
            tmf.init(myKeyStore)
            var ctx = SSLContext.getInstance("TLS")
            ctx?.init(kmf.keyManagers, tmf.trustManagers, null)
            ctx
        } catch (e: Exception) {
            "Failed to load keystore. Web requests may fail on older Java versions".printToConsole(logType = LogType.WARN)
            e.printTraceToConsole()
            null
        }
    }
    
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        listOf(
            WorldListener,
            CPS,
            GuiHandler,
            ClientListener,
            UpdateChecker,
            MouseListener,
            ModuleUpdater,
            ForgeTrigger
        ).forEach(MinecraftForge.EVENT_BUS::register)

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        Config.loadData()

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

    fun makeWebRequest(url: String): URLConnection {
        val connection = URL(url).openConnection()
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (ChatTriggers)")
        if (connection is HttpsURLConnection && sslContext != null) {
            connection.sslSocketFactory = sslContext!!.socketFactory
        }
        connection.connectTimeout = 3000
        connection.readTimeout = 3000
        return connection
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
        val connection = makeWebRequest(url)
        connection.getInputStream()
    }
}
