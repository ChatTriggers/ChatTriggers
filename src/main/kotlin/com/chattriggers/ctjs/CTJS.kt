package com.chattriggers.ctjs

import com.chattriggers.ctjs.commands.CTCommand
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.loader.UriScheme
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.listeners.WorldListener
import com.chattriggers.ctjs.minecraft.objects.Sound
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler
import com.chattriggers.ctjs.minecraft.wrappers.CPS
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.UpdateChecker
import com.chattriggers.ctjs.utils.Config
import com.google.gson.Gson
import gg.essential.vigilance.Vigilance
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import java.io.File

//#if MC==18090
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
//#else
//$$ import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent
//$$ import com.mojang.brigadier.CommandDispatcher
//$$ import net.minecraft.command.CommandSource
//$$ import net.minecraftforge.event.RegisterCommandsEvent
//#endif

@Mod(
    //#if MC==10809
    modid = Reference.MODID,
    name = Reference.MODNAME,
    version = Reference.MODVERSION,
    clientSideOnly = true
    //#else
    //$$ Reference.MODID
    //#endif
)
class CTJSMod {
    //#if MC!=10809
    //$$
    //$$ init {
    //$$     FMLJavaModLoadingContext.get().modEventBus.addListener { e: FMLCommonSetupEvent ->
    //$$         preInit()
    //$$         init()
    //$$     }
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ fun registerCommands(event: RegisterCommandsEvent) {
    //$$     CTJS.commandDispatcher = event.dispatcher
    //$$     CTCommand.register(event.dispatcher)
    //$$ }
    //#endif

    //#if MC==10809
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
    //#else
    //$$ fun preInit() {
    //#endif
        listOf(WorldListener, CPS, GuiHandler, ClientListener, UpdateChecker).forEach {
            MinecraftForge.EVENT_BUS.register(it)
        }

        UriScheme.installUriScheme()
        UriScheme.createSocketListener()
    }

    //#if MC==10809
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
    //#else
    //$$ fun init() {
    //#endif
        Vigilance.initialize()
        Config.preload()

        Reference.conditionalThread {
            ModuleManager.entryPass()
        }

        //#if MC==10809
        ClientCommandHandler.instance.registerCommand(CTCommand)
        //#endif

        Runtime.getRuntime().addShutdownHook(Thread(TriggerType.GAME_UNLOAD::triggerAll))
    }
}

object CTJS {
    const val WEBSITE_ROOT = "https://www.chattriggers.com"
    val gson = Gson()
    val configLocation = File("./config")
    val assetsDir = File(configLocation, "ChatTriggers/images/").apply { mkdirs() }
    val sounds = mutableListOf<Sound>()

    //#if MC!=10809
    //$$ lateinit var commandDispatcher: CommandDispatcher<CommandSource?>
    //#endif
}