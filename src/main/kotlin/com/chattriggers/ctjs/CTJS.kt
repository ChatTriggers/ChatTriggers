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
import com.chattriggers.ctjs.utils.kotlin.AnnotationHandler
import com.google.gson.JsonParser
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
import java.io.FileReader

@Mod(modid = Reference.MODID,
    name = Reference.MODNAME,
    version = Reference.MODVERSION,
    clientSideOnly = true,
    modLanguage = "Kotlin",
    modLanguageAdapter = "com.chattriggers.ctjs.utils.kotlin.KotlinAdapter"
)
object CTJS {
    lateinit var assetsDir: File
    private lateinit var configLocation: File
    val sounds = mutableListOf<Sound>()

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        this.configLocation = event.modConfigurationDirectory
        val pictures = File(event.modConfigurationDirectory, "ChatTriggers/images/")
        pictures.mkdirs()
        assetsDir = pictures

        loadConfig()

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
        FileLib.getUrlContent("https://www.chattriggers.com/tracker/?uuid=$sha256uuid")
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

    fun saveConfig() = Config.save(File(this.configLocation, "ChatTriggers.json"))

    fun loadConfig(): Boolean {
        try {
            val parser = JsonParser()
            val obj = parser.parse(
                    FileReader(
                            File(this.configLocation, "ChatTriggers.json")
                    )
            ).asJsonObject

            Config.load(obj)

            return true
        } catch (exception: Exception) {
            val place = File(this.configLocation, "ChatTriggers.json")
            place.delete()
            place.createNewFile()
            saveConfig()
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