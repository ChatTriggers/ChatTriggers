package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.CTCommand
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.loader.UriScheme
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.listeners.ChatListener
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.listeners.WorldListener
import com.chattriggers.ctjs.minecraft.objects.Sound
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.wrappers.CPS
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.UpdateChecker
import com.chattriggers.ctjs.utils.config.Config
import com.google.gson.JsonParser
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.commons.codec.digest.DigestUtils
import java.io.File
import java.io.FileReader
import kotlin.concurrent.thread

@Mod(
    modid = Reference.MODID,
    name = Reference.MODNAME,
    version = Reference.MODVERSION,
    clientSideOnly = true,
    modLanguage = "Kotlin",
    modLanguageAdapter = "com.chattriggers.ctjs.utils.kotlin.KotlinAdapter"
)
object CTJS {
    val configLocation = File("./config")
    val assetsDir = File(configLocation, "ChatTriggers/images/").apply { mkdirs() }
    val sounds = mutableListOf<Sound>()

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        thread(start = true) {
            loadConfig()
        }

        listOf(ChatListener, WorldListener, CPS, GuiHandler, ClientListener, UpdateChecker).forEach {
            MinecraftForge.EVENT_BUS.register(it)
        }

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()

        val sha256uuid = DigestUtils.sha256Hex(Player.getUUID())

        try {
            FileLib.getUrlContent("https://www.chattriggers.com/tracker/?uuid=$sha256uuid")
        } catch (e: Exception) {
            e.print()
        }
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        Reference.conditionalThread {
            ModuleManager.entryPass()
        }

        registerHooks()
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