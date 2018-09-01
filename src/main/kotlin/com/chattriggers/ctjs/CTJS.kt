package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.CTCommand
import com.chattriggers.ctjs.engine.ModuleManager
import com.chattriggers.ctjs.loader.UriScheme
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.objects.Sound
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.capes.LayerCape
import com.chattriggers.ctjs.utils.config.Config
import com.chattriggers.ctjs.utils.config.GuiConfig
import com.chattriggers.ctjs.utils.console.Console
import com.chattriggers.ctjs.utils.kotlin.AnnotationHandler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.sentry.Sentry
import io.sentry.event.UserBuilder
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException

@Mod(modid = Reference.MODID,
    name = Reference.MODNAME,
    version = Reference.MODVERSION,
    clientSideOnly = true,
    modLanguage = "Kotlin",
    modLanguageAdapter = "com.chattriggers.ctjs.utils.kotlin.KotlinAdapter"
)
object CTJS {
    lateinit var assetsDir: File
    lateinit var configLocation: File
    val sounds = mutableListOf<Sound>()

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        this.configLocation = event.modConfigurationDirectory
        val pictures = File(event.modConfigurationDirectory, "ChatTriggers/images/")
        pictures.mkdirs()
        assetsDir = pictures

        setupConfig()

        Console()

        Loader.instance().modList.filter { it.modId == Reference.MODID }.forEach {
            AnnotationHandler.subscribeAutomatic(it, event.asmData)
        }

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()

        Sentry.init(Reference.SENTRYDSN)

        Sentry.getContext().user = UserBuilder()
                .setUsername(Player.getName())
                .setId(Player.getUUID())
                .build()

        val sha256uuid = DigestUtils.sha256Hex(Player.getUUID())
        FileLib.getUrlContent("http://167.99.3.229/tracker/?uuid=$sha256uuid")
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        ModuleManager.load(true)
        registerHooks()
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {

        Client.getMinecraft().renderManager.skinMap.values.forEach {
            it.addLayer(LayerCape(it))
        }
    }

    fun setupConfig() {
        GuiConfig()
        Config()
        if (loadConfig()) Config.getInstance().init()
    }

    fun saveConfig() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val path = File(this.configLocation, "ChatTriggers.json").absolutePath
        FileLib.write(path, gson.toJson(Config.getInstance()))
    }

    private fun loadConfig(): Boolean {
        try {
            Config.setInstance(
                    Gson().fromJson(
                            FileReader(
                                    File(this.configLocation, "ChatTriggers.json")
                            ),
                            Config.getInstance().javaClass
                    )
            )
            return true
        } catch (exception: FileNotFoundException) {
            try {
                File(this.configLocation, "ChatTriggers.json").createNewFile()
                Config.getInstance().init()
                saveConfig()
            } catch (ioexception: IOException) {
                ioexception.printStackTrace()
            }
        }

        return false
    }

    private fun registerHooks() {
        ClientCommandHandler.instance.registerCommand(CTCommand)

        Runtime.getRuntime().addShutdownHook(
                Thread { TriggerType.GAME_UNLOAD::triggerAll }
        )
    }
}