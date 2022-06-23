package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.engine.langs.js.JSContextFactory
import com.chattriggers.ctjs.launch.mixins.transformers.NetworkManagerAccessor
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent
import com.chattriggers.ctjs.minecraft.listeners.events.ChatEvent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.triggers.EventType
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import com.chattriggers.ctjs.utils.kotlin.asMixin
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemStack
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.util.BlockPos
import org.lwjgl.util.vector.Vector3f
import org.mozilla.javascript.Context
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.concurrent.CopyOnWriteArrayList

//#if FORGE
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//#endif

object ClientListener : Initializer {
    private var ticksPassed: Int = 0
    val chatHistory = mutableListOf<String>()
    val actionBarHistory = mutableListOf<String>()
    private val tasks = CopyOnWriteArrayList<Task>()

    class Task(var delay: Int, val callback: () -> Unit)

    fun addTask(delay: Int, callback: () -> Unit) {
        tasks.add(Task(delay, callback))
    }

    override fun init() {
        ticksPassed = 0

        CTJS.addEventListener(EventType.Tick) {
            tasks.removeAll {
                if (it.delay-- <= 0) {
                    //#if MC<=11202
                    Client.getMinecraft().addScheduledTask(it.callback)
                    //#else
                    //$$ Client.getMinecraft().tell(it.callback)
                    //#endif
                    true
                } else false
            }

            if (!World.isLoaded())
                return@addEventListener

            TriggerType.Tick.triggerAll(ticksPassed)
            ticksPassed++

            Scoreboard.resetCache()
        }

        CTJS.addEventListener(EventType.Chat) { args ->
            val event = args!![0] as ChatEvent

            when (event.type.toInt()) {
                in 0..1 -> {
                    // save to chatHistory
                    chatHistory += ChatLib.getChatMessage(event, true)
                    if (chatHistory.size > 1000) chatHistory.removeAt(0)

                    // normal Chat Message
                    TriggerType.Chat.triggerAll(ChatLib.getChatMessage(event, false), event)

                    // print to console
                    if (Config.printChatToConsole) {
                        "[CHAT] ${ChatLib.replaceFormatting(ChatLib.getChatMessage(event, true))}".printToConsole()
                    }
                }
                2 -> {
                    // save to actionbar history
                    actionBarHistory += ChatLib.getChatMessage(event, true)
                    if (actionBarHistory.size > 1000) actionBarHistory.removeAt(0)

                    // action bar
                    TriggerType.ActionBar.triggerAll(ChatLib.getChatMessage(event, false), event)
                }
            }
        }

        CTJS.addEventListener(EventType.ServerConnect) { args ->
            val manager = args!![0] as NetworkManager

            TriggerType.ServerConnect.triggerAll()

            manager.asMixin<NetworkManagerAccessor>().channel.pipeline()
                .addBefore("packet_handler", "CT_packet_handler", object : ChannelDuplexHandler() {
                    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
                        val packetReceivedEvent = CancellableEvent()

                        if (msg is Packet<*>) {
                            JSContextFactory.enterContext()
                            try {
                                TriggerType.PacketReceived.triggerAll(msg, packetReceivedEvent)
                            } finally {
                                Context.exit()
                            }
                        }

                        if (!packetReceivedEvent.isCanceled())
                            ctx?.fireChannelRead(msg)
                    }

                    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
                        val packetSentEvent = CancellableEvent()

                        if (msg is Packet<*>) {
                            JSContextFactory.enterContext()
                            try {
                                TriggerType.PacketSent.triggerAll(msg, packetSentEvent)
                            } finally {
                                Context.exit()
                            }
                        }

                        if (!packetSentEvent.isCanceled())
                            ctx?.write(msg, promise)
                    }
                })
        }
    }

    //#if FORGE
    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent.Pre) {
        Renderer.pushMatrix()
        handleOverlayTriggers(event)
        Renderer.popMatrix()
    }

    private fun handleOverlayTriggers(event: RenderGameOverlayEvent.Pre) {
        when (event.type) {
            //#if MC<=11202
            RenderGameOverlayEvent.ElementType.BOSSHEALTH -> TriggerType.RenderBossHealth.triggerAll(event)
            //elseif MC>=11701
            //$$ RenderGameOverlayEvent.ElementType.BOSSINFO -> TriggerType.RenderBossHealth.triggerAll(event)
            //#endif
            RenderGameOverlayEvent.ElementType.CHAT -> TriggerType.RenderChat.triggerAll(event)
            RenderGameOverlayEvent.ElementType.DEBUG -> TriggerType.RenderDebug.triggerAll(event)
            RenderGameOverlayEvent.ElementType.PLAYER_LIST -> TriggerType.RenderPlayerList.triggerAll(event)
            RenderGameOverlayEvent.ElementType.TEXT -> {
                TriggerType.RenderOverlay.triggerAll(event)
                CTJS.getEventListeners(EventType.RenderOverlay).forEach {
                    it.invoke(arrayOf(event))
                }
            }
            //#if MC<=11202
            RenderGameOverlayEvent.ElementType.CROSSHAIRS -> TriggerType.RenderCrosshair.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HEALTH -> TriggerType.RenderHealth.triggerAll(event)
            RenderGameOverlayEvent.ElementType.ARMOR -> TriggerType.RenderArmor.triggerAll(event)
            RenderGameOverlayEvent.ElementType.FOOD -> TriggerType.RenderFood.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HEALTHMOUNT -> TriggerType.RenderMountHealth.triggerAll(event)
            RenderGameOverlayEvent.ElementType.EXPERIENCE -> TriggerType.RenderExperience.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HOTBAR -> TriggerType.RenderHotbar.triggerAll(event)
            RenderGameOverlayEvent.ElementType.AIR -> TriggerType.RenderAir.triggerAll(event)
            RenderGameOverlayEvent.ElementType.PORTAL -> TriggerType.RenderPortal.triggerAll(event)
            RenderGameOverlayEvent.ElementType.JUMPBAR -> TriggerType.RenderJumpBar.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HELMET -> TriggerType.RenderHelmet.triggerAll(event)
            //#endif
            else -> {}
        }
    }
    //#endif

    internal fun onDropItem(item: ItemStack?, cir: CallbackInfoReturnable<EntityItem>) {
        if (item == null)
            return

        val event = CancellableEvent()
        TriggerType.DropItem.triggerAll(Item(item), event)

        if (event.isCanceled())
            cir.returnValue = null
    }

    internal fun onPlayerInteract(action: PlayerInteractAction, pos: BlockPos?): Boolean {
        val event = CancellableEvent()

        TriggerType.PlayerInteract.triggerAll(
            action,
            Vector3f((pos?.x ?: 0).toFloat(), (pos?.y ?: 0).toFloat(), (pos?.z ?: 0).toFloat()),
            event
        )

        return event.isCanceled()
    }

    /**
     * Used as a pass through argument in [com.chattriggers.ctjs.engine.IRegister.registerPlayerInteract].\n
     * Exposed in providedLibs as InteractAction.
     */
    enum class PlayerInteractAction {
        RIGHT_CLICK_BLOCK,
        RIGHT_CLICK_EMPTY
    }
}
