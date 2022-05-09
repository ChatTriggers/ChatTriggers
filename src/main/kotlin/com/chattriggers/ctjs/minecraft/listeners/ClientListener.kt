package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.engine.langs.js.JSContextFactory
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.EventLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockFace
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UMouse
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.client.event.*
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.util.vector.Vector3f
import org.mozilla.javascript.Context
import java.util.concurrent.CopyOnWriteArrayList

//#if MC>=11701
//$$ import com.chattriggers.ctjs.Reference
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.event.*
//#else
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.network.Packet
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
//#endif

//#if MC>=11701
//$$ @Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//#endif
object ClientListener {
    private var ticksPassed: Int = 0
    val chatHistory = mutableListOf<String>()
    val actionBarHistory = mutableListOf<String>()
    private val tasks = CopyOnWriteArrayList<Task>()

    class Task(var delay: Int, val callback: () -> Unit)

    init {
        ticksPassed = 0
    }

    @SubscribeEvent
    fun onReceiveChat(event: ClientChatReceivedEvent) {
        when (EventLib.getType(event)) {
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

    fun addTask(delay: Int, callback: () -> Unit) {
        tasks.add(Task(delay, callback))
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END)
            return

        tasks.removeAll {
            if (it.delay-- <= 0) {
                Client.getMinecraft().addScheduledTask { it.callback() }
                true
            } else false
        }

        if (!World.isLoaded())
            return

        TriggerType.Tick.triggerAll(ticksPassed)
        ticksPassed++

        Scoreboard.resetCache()
    }

    // TODO(CONVERT)
    //#if MC<=11202
    @SubscribeEvent
    fun onClientDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        TriggerType.ServerDisconnect.triggerAll(event)
    }

    @SubscribeEvent
    fun onNetworkEvent(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        TriggerType.ServerConnect.triggerAll(event)

        event.manager.channel().pipeline()
            .addAfter("fml:packet_handler", "CT_packet_handler", object : ChannelDuplexHandler() {
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

                    if (!packetReceivedEvent.isCancelled())
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

                    if (!packetSentEvent.isCancelled())
                        ctx?.write(msg, promise)
                }
            })
    }
    //#endif

    @SubscribeEvent
    fun onDrawScreenEvent(event: GuiScreenEvent.DrawScreenEvent.Post) {
        TriggerType.PostGuiRender.triggerAll(event.mouseX, event.mouseY, event.gui)
    }

    @SubscribeEvent
    fun onRenderTick(event: TickEvent.RenderTickEvent) {
        TriggerType.Step.triggerAll()
        //#if MC<=11202
        if (World.isLoaded())
            MouseListener.handleDragged()
        //#endif
    }

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent.Pre) {
        //#if MC<=11202
        GlStateManager.pushMatrix()
        handleOverlayTriggers(event)
        GlStateManager.popMatrix()
        //#else
        //$$ event.matrixStack.pushPose()
        //$$ handleOverlayTriggers(event)
        //$$ event.matrixStack.popPose()
        //#endif
    }

    private fun handleOverlayTriggers(event: RenderGameOverlayEvent.Pre) {
        when (event.type) {
            RenderGameOverlayEvent.ElementType.PLAYER_LIST -> TriggerType.RenderPlayerList.triggerAll(event)
            RenderGameOverlayEvent.ElementType.DEBUG -> TriggerType.RenderDebug.triggerAll(event)
            RenderGameOverlayEvent.ElementType.TEXT -> TriggerType.RenderOverlay.triggerAll(event)
            RenderGameOverlayEvent.ElementType.CHAT -> TriggerType.RenderChat.triggerAll(event)
            // TODO(CONVERT): These don't exist, so I guess mixin them?
            //#if MC<=11202
            RenderGameOverlayEvent.ElementType.CROSSHAIRS -> TriggerType.RenderCrosshair.triggerAll(event)
            RenderGameOverlayEvent.ElementType.BOSSHEALTH -> TriggerType.RenderBossHealth.triggerAll(event)
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
        }
    }

    @SubscribeEvent
    fun onGuiOpened(event: GuiOpenEvent) {
        if (event.gui != null) TriggerType.GuiOpened.triggerAll(event)
    }

    // TODO(CONVERT)
    //#if MC<=11202
    @SubscribeEvent
    fun onBlockHighlight(event: DrawBlockHighlightEvent) {
        if (event.target == null || event.target.blockPos == null) return

        val position = Vector3f(
            event.target.blockPos.x.toFloat(),
            event.target.blockPos.y.toFloat(),
            event.target.blockPos.z.toFloat()
        )

        TriggerType.BlockHighlight.triggerAll(position, event)
    }
    //#endif

    @SubscribeEvent
    fun onPickupItem(event: EntityItemPickupEvent) {
        //#if MC<=11202
        val player = event.entityPlayer
        //#else
        //$$ val player = event.player
        //#endif

        if (player !is EntityPlayerMP)
            return

        val item = event.item

        // TODO(FEATURE): Vector3f wrapper class?
        //#if MC<=11202
        val position = Vector3f(
            item.posX.toFloat(),
            item.posY.toFloat(),
            item.posZ.toFloat()
        )
        val motion = Vector3f(
            item.motionX.toFloat(),
            item.motionY.toFloat(),
            item.motionZ.toFloat()
        )
        //#else
        //$$ val position = Vector3f(
        //$$     item.x.toFloat(),
        //$$     item.y.toFloat(),
        //$$     item.z.toFloat()
        //$$ )
        //$$ val motion = Vector3f(
        //$$     item.deltaMovement.x.toFloat(),
        //$$     item.deltaMovement.y.toFloat(),
        //$$     item.deltaMovement.z.toFloat()
        //$$ )
        //#endif

        TriggerType.PickupItem.triggerAll(
            //#if MC<=10809
            Item(item.entityItem),
            //#else
            //$$ Item(item.item),
            //#endif
            PlayerMP(player),
            position,
            motion,
            event
        )
    }

    @SubscribeEvent
    fun onDropItem(event: ItemTossEvent) {
        val item = event.entityItem

        //#if MC<=11202
        val position = Vector3f(
            item.posX.toFloat(),
            item.posY.toFloat(),
            item.posZ.toFloat()
        )
        val motion = Vector3f(
            item.motionX.toFloat(),
            item.motionY.toFloat(),
            item.motionZ.toFloat()
        )
        //#else
        //$$ val position = Vector3f(
        //$$     item.x.toFloat(),
        //$$     item.y.toFloat(),
        //$$     item.z.toFloat()
        //$$ )
        //$$ val motion = Vector3f(
        //$$     item.deltaMovement.x.toFloat(),
        //$$     item.deltaMovement.y.toFloat(),
        //$$     item.deltaMovement.z.toFloat()
        //$$ )
        //#endif

        TriggerType.DropItem.triggerAll(
            //#if MC<=10809
            Item(item),
            //#else
            //$$ Item(item.item),
            //#endif
            PlayerMP(event.player),
            position,
            motion,
            event
        )
    }

    @SubscribeEvent
    fun onGuiRender(e: GuiScreenEvent.BackgroundDrawnEvent) {
        //#if MC<=11202
        GlStateManager.pushMatrix()
        //#else
        //$$ e.matrixStack.pushPose()
        //#endif

        TriggerType.GuiRender.triggerAll(
            UMouse.Scaled.x,
            UMouse.Scaled.y,
            e.gui
        )

        //#if MC<=11202
        GlStateManager.popMatrix()
        //#else
        //$$ e.matrixStack.pushPose()
        //#endif
    }

    //#if MC<=11202
    @SubscribeEvent
    fun onInteract(e: PlayerInteractEvent) {
        val action = when (e.action) {
            PlayerInteractEvent.Action.LEFT_CLICK_BLOCK -> return
            PlayerInteractEvent.Action.RIGHT_CLICK_AIR -> PlayerInteractAction.RIGHT_CLICK_EMPTY
            PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK -> PlayerInteractAction.RIGHT_CLICK_BLOCK
            null -> PlayerInteractAction.UNKNOWN
        }

        TriggerType.PlayerInteract.triggerAll(
            action,
            Vector3f((e.pos?.x ?: 0).toFloat(), (e.pos?.y ?: 0).toFloat(), (e.pos?.z ?: 0).toFloat()),
            e
        )
    }
    //#else
    //$$@SubscribeEvent
    //$$fun onLeftClick(e: PlayerInteractEvent) {
    //$$    val action = when (e) {
    //$$        is PlayerInteractEvent.RightClickBlock -> PlayerInteractAction.RIGHT_CLICK_BLOCK
    //$$        is PlayerInteractEvent.RightClickEmpty -> PlayerInteractAction.RIGHT_CLICK_EMPTY
    //$$        else -> PlayerInteractAction.UNKNOWN
    //$$    }
    //$$
    //$$    TriggerType.PlayerInteract.triggerAll(
    //$$            action,
    //$$            World.getBlockAt(e.pos.x, e.pos.y, e.pos.z),
    //$$            e
    //$$    )
    //$$}
    //#endif

    @SubscribeEvent
    fun onHandRender(e: RenderHandEvent) {
        TriggerType.RenderHand.triggerAll(e)
    }

    /**
     * Used as a pass through argument in [com.chattriggers.ctjs.engine.IRegister.registerPlayerInteract].\n
     * Exposed in providedLibs as InteractAction.
     */
    enum class PlayerInteractAction {
        RIGHT_CLICK_BLOCK,
        RIGHT_CLICK_EMPTY,
        UNKNOWN
    }
}
