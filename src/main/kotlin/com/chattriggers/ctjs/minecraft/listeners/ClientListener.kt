package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockFace
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.network.Packet
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import org.lwjgl.input.Mouse
import org.lwjgl.util.vector.Vector3f
import org.mozilla.javascript.Context

object ClientListener {
    private var ticksPassed: Int = 0
    private val mouseState: MutableMap<Int, Boolean>
    private val draggedState: MutableMap<Int, State>

    class State(val x: Float, val y: Float)

    init {
        ticksPassed = 0

        mouseState = mutableMapOf()
        draggedState = mutableMapOf()

        for (i in 0..4)
            mouseState[i] = false
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (World.getWorld() == null || event.phase == TickEvent.Phase.END)
            return

        TriggerType.Tick.triggerAll(ticksPassed)
        ticksPassed++

        Scoreboard.resetCache()
    }

    private fun handleMouseInput() {
        if (!Mouse.isCreated()) return

        val scroll = Mouse.getEventDWheel()

        when {
            scroll > 0 -> TriggerType.Scrolled.triggerAll(Client.getMouseX(), Client.getMouseY(), 1)
            scroll < 0 -> TriggerType.Scrolled.triggerAll(Client.getMouseX(), Client.getMouseY(), -1)
        }

        for (button in 0..4) {
            handleDragged(button)

            // normal clicked
            if (Mouse.isButtonDown(button) == mouseState[button]) continue

            TriggerType.Clicked.triggerAll(
                Client.getMouseX(),
                Client.getMouseY(),
                button,
                Mouse.isButtonDown(button)
            )

            mouseState[button] = Mouse.isButtonDown(button)

            // add new dragged
            if (Mouse.isButtonDown(button))
                draggedState[button] = State(Client.getMouseX(), Client.getMouseY())
            else if (draggedState.containsKey(button))
                draggedState.remove(button)
        }
    }

    private fun handleDragged(button: Int) {
        if (button !in draggedState)
            return

        TriggerType.Dragged.triggerAll(
            Client.getMouseX() - (draggedState[button]?.x ?: 0f),
            Client.getMouseY() - (draggedState[button]?.y ?: 0f),
            Client.getMouseX(),
            Client.getMouseY(),
            button
        )

        // update dragged
        draggedState[button] = State(Client.getMouseX(), Client.getMouseY())
    }

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
                        Context.enter()
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
                        Context.enter()
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

    @SubscribeEvent
    fun onDrawScreenEvent(event: GuiScreenEvent.DrawScreenEvent.Post) {
        TriggerType.PostGuiRender.triggerAll(event.mouseX, event.mouseY, event.gui)
    }

    @SubscribeEvent
    fun onRenderTick(event: TickEvent.RenderTickEvent) {
        TriggerType.Step.triggerAll()
    }

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent.Pre) {
        GlStateManager.pushMatrix()
        handleOverlayTriggers(event)
        GlStateManager.popMatrix()

        if (event.type != RenderGameOverlayEvent.ElementType.TEXT)
            return

        handleMouseInput()
    }

    private fun handleOverlayTriggers(event: RenderGameOverlayEvent.Pre) {
        when (event.type) {
            RenderGameOverlayEvent.ElementType.PLAYER_LIST -> TriggerType.RenderPlayerList.triggerAll(event)
            RenderGameOverlayEvent.ElementType.CROSSHAIRS -> TriggerType.RenderCrosshair.triggerAll(event)
            RenderGameOverlayEvent.ElementType.DEBUG -> TriggerType.RenderDebug.triggerAll(event)
            RenderGameOverlayEvent.ElementType.BOSSHEALTH -> TriggerType.RenderBossHealth.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HEALTH -> TriggerType.RenderHealth.triggerAll(event)
            RenderGameOverlayEvent.ElementType.ARMOR -> TriggerType.RenderArmor.triggerAll(event)
            RenderGameOverlayEvent.ElementType.FOOD -> TriggerType.RenderFood.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HEALTHMOUNT -> TriggerType.RenderMountHealth.triggerAll(event)
            RenderGameOverlayEvent.ElementType.EXPERIENCE -> TriggerType.RenderExperience.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HOTBAR -> TriggerType.RenderHotbar.triggerAll(event)
            RenderGameOverlayEvent.ElementType.AIR -> TriggerType.RenderAir.triggerAll(event)
            RenderGameOverlayEvent.ElementType.TEXT -> TriggerType.RenderOverlay.triggerAll(event)
        }
    }

    @SubscribeEvent
    fun onGuiOpened(event: GuiOpenEvent) {
        if (event.gui != null) TriggerType.GuiOpened.triggerAll(event)
    }

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

    @SubscribeEvent
    fun onPickupItem(event: EntityItemPickupEvent) {
        if (event.entityPlayer !is EntityPlayerMP) return

        val player = event.entityPlayer as EntityPlayerMP

        val item = event.item

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

    fun onHitBlock(pos: MCBlockPos, facing: EnumFacing): Boolean {
        val event = CancellableEvent()

        TriggerType.HitBlock.triggerAll(
            World.getBlockAt(pos.x, pos.y, pos.z).withFace(BlockFace.fromMCEnumFacing(facing)),
            event
        )

        return event.isCancelled()
    }

    @SubscribeEvent
    fun onDropItem(event: ItemTossEvent) {
        val item = event.entityItem

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
        GlStateManager.pushMatrix()

        TriggerType.GuiRender.triggerAll(
            e.mouseX,
            e.mouseY,
            e.gui
        )

        GlStateManager.popMatrix()
    }

    //#if MC<=10809
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
    //$$        is PlayerInteractEvent.EntityInteract, is PlayerInteractEvent.EntityInteractSpecific ->
    //$$            PlayerInteractAction.RIGHT_CLICK_ENTITY
    //$$        is PlayerInteractEvent.RightClickBlock -> PlayerInteractAction.RIGHT_CLICK_BLOCK
    //$$        is PlayerInteractEvent.RightClickItem -> PlayerInteractAction.RIGHT_CLICK_ITEM
    //$$        is PlayerInteractEvent.RightClickEmpty -> PlayerInteractAction.RIGHT_CLICK_EMPTY
    //$$        is PlayerInteractEvent.LeftClickBlock -> PlayerInteractAction.LEFT_CLICK_BLOCK
    //$$        is PlayerInteractEvent.LeftClickEmpty -> PlayerInteractAction.LEFT_CLICK_EMPTY
    //$$        else -> PlayerInteractAction.UNKNOWN
    //$$    }
    //$$
    //$$    TriggerType.PLAYER_INTERACT.triggerAll(
    //$$            action,
    //$$            World.getBlockAt(e.pos.x, e.pos.y, e.pos.z),
    //$$            e
    //$$    )
    //$$}
    //#endif

    /**
     * Used as a pass through argument in [com.chattriggers.ctjs.engine.IRegister.registerPlayerInteract].\n
     * Exposed in providedLibs as InteractAction.
     */
    enum class PlayerInteractAction {
        RIGHT_CLICK_BLOCK,
        RIGHT_CLICK_EMPTY,

        //#if MC>10809
        //$$RIGHT_CLICK_ENTITY,
        //$$RIGHT_CLICK_ITEM,
        //$$LEFT_CLICK_EMPTY,
        //#endif
        UNKNOWN
    }
}
