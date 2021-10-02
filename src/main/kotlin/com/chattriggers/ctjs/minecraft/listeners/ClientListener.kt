package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockFace
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.network.Packet
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.util.vector.Vector3f

//#if MC==11602
//$$ import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
//$$ import net.minecraftforge.client.event.DrawHighlightEvent
//#else
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.GL11
//#endif

object ClientListener {
    private var ticksPassed: Int = 0
    private val mouseState: MutableMap<Int, Boolean>
    private val draggedState: MutableMap<Int, State>

    class State(val x: Float, val y: Float)

    init {
        this.ticksPassed = 0

        this.mouseState = mutableMapOf()
        draggedState = mutableMapOf()

        for (i in 0..4)
            this.mouseState[i] = false
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (World.getWorld() == null || event.phase == TickEvent.Phase.END)
            return

        TriggerType.TICK.triggerAll(this.ticksPassed)
        this.ticksPassed++

        Scoreboard.resetCache()
    }

    //#if MC==11602
    //$$ @SubscribeEvent
    //$$ fun onMouseScroll(event: GuiScreenEvent.MouseScrollEvent) {
    //$$     if (event is GuiScreenEvent.MouseScrollEvent.Post)
    //$$         return
    //$$
    //$$     val value = if (event.scrollDelta < 0) -1 else 1
    //$$     TriggerType.SCROLLED.triggerAll(event.mouseX, event.mouseY, value)
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ fun onMouseClicked(event: GuiScreenEvent.MouseClickedEvent) {
    //$$     if (event is GuiScreenEvent.MouseClickedEvent.Post)
    //$$         return
    //$$
    //$$     TriggerType.CLICKED.triggerAll(
    //$$         event.mouseX,
    //$$         event.mouseY,
    //$$         event.button,
    //$$         true,
    //$$     )
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ fun onMouseRelease(event: GuiScreenEvent.MouseReleasedEvent) {
    //$$     if (event is GuiScreenEvent.MouseReleasedEvent.Post)
    //$$         return
    //$$
    //$$     TriggerType.CLICKED.triggerAll(
    //$$         event.mouseX,
    //$$         event.mouseY,
    //$$         event.button,
    //$$         false,
    //$$     )
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ fun onMouseDragged(event: GuiScreenEvent.MouseDragEvent) {
    //$$     if (event is GuiScreenEvent.MouseDragEvent.Post)
    //$$         return
    //$$
    //$$     TriggerType.DRAGGED.triggerAll(
    //$$         event.dragX,
    //$$         event.dragY,
    //$$         event.mouseX,
    //$$         event.mouseY,
    //$$         event.mouseButton,
    //$$     )
    //$$ }
    //#else
    private fun handleMouseInput() {
        if (!Mouse.isCreated()) return

        val scroll = Mouse.getEventDWheel()

        when {
            scroll > 0 -> TriggerType.SCROLLED.triggerAll(Client.getMouseX(), Client.getMouseY(), 1)
            scroll < 0 -> TriggerType.SCROLLED.triggerAll(Client.getMouseX(), Client.getMouseY(), -1)
        }

        for (button in 0..4) {
            handleDragged(button)

            // normal clicked
            if (Mouse.isButtonDown(button) == this.mouseState[button]) continue

            TriggerType.CLICKED.triggerAll(
                Client.getMouseX(),
                Client.getMouseY(),
                button,
                Mouse.isButtonDown(button)
            )

            this.mouseState[button] = Mouse.isButtonDown(button)

            // add new dragged
            if (Mouse.isButtonDown(button))
                this.draggedState[button] = State(Client.getMouseX(), Client.getMouseY())
            else if (this.draggedState.containsKey(button))
                this.draggedState.remove(button)
        }
    }

    private fun handleDragged(button: Int) {
        if (button !in draggedState)
            return

        TriggerType.DRAGGED.triggerAll(
            Client.getMouseX() - (this.draggedState[button]?.x ?: 0f),
            Client.getMouseY() - (this.draggedState[button]?.y ?: 0f),
            Client.getMouseX(),
            Client.getMouseY(),
            button
        )

        // update dragged
        this.draggedState[button] = State(Client.getMouseX(), Client.getMouseY())
    }
    //#endif

    // TODO(1.16.2)
    //#if MC==10809
    @SubscribeEvent
    fun onNetworkEvent(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        event.manager.channel().pipeline().addAfter("fml:packet_handler", "CT_packet_handler", object : ChannelDuplexHandler() {
            override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
                val packetReceivedEvent = CancellableEvent()

                if (msg is Packet<*>)
                    TriggerType.PACKET_RECEIVED.triggerAll(msg, packetReceivedEvent)

                if (!packetReceivedEvent.isCanceled())
                    ctx?.fireChannelRead(msg)
            }

            override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
                val packetSentEvent = CancellableEvent()

                if (msg is Packet<*>)
                    TriggerType.PACKET_SENT.triggerAll(msg, packetSentEvent)

                if (!packetSentEvent.isCanceled())
                    ctx?.write(msg, promise)
            }
        })
    }
    //#endif

    @SubscribeEvent
    fun onDrawScreenEvent(event: GuiScreenEvent.DrawScreenEvent.Post) {
        TriggerType.POST_GUI_RENDER.triggerAll(event.gui, event.mouseX, event.mouseY)
    }

    @SubscribeEvent
    fun onRenderTick(event: TickEvent.RenderTickEvent) {
        TriggerType.STEP.triggerAll()
    }

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent.Pre) {
        //#if MC==11602
        //$$ event.matrixStack.push()
        //#else
        GL11.glPushMatrix()
        //#endif

        handleOverlayTriggers(event)

        //#if MC==11602
        //$$ event.matrixStack.pop()
        //#else
        GL11.glPopMatrix()
        //#endif

        if (event.type != RenderGameOverlayEvent.ElementType.TEXT)
            return

        //#if MC==10809
        handleMouseInput()
        //#endif
    }

    private fun handleOverlayTriggers(event: RenderGameOverlayEvent.Pre) {
        //#if MC==11602
        //$$ Renderer.pushMatrixStack(event.matrixStack)
        //#endif

        when (event.type) {
            RenderGameOverlayEvent.ElementType.PLAYER_LIST -> TriggerType.RENDER_PLAYER_LIST.triggerAll(event)
            RenderGameOverlayEvent.ElementType.CROSSHAIRS -> TriggerType.RENDER_CROSSHAIR.triggerAll(event)
            RenderGameOverlayEvent.ElementType.DEBUG -> TriggerType.RENDER_DEBUG.triggerAll(event)
            RenderGameOverlayEvent.ElementType.BOSSHEALTH -> TriggerType.RENDER_BOSS_HEALTH.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HEALTH -> TriggerType.RENDER_HEALTH.triggerAll(event)
            RenderGameOverlayEvent.ElementType.ARMOR -> TriggerType.RENDER_ARMOR.triggerAll(event)
            RenderGameOverlayEvent.ElementType.FOOD -> TriggerType.RENDER_FOOD.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HEALTHMOUNT -> TriggerType.RENDER_MOUNT_HEALTH.triggerAll(event)
            RenderGameOverlayEvent.ElementType.EXPERIENCE -> TriggerType.RENDER_EXPERIENCE.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HOTBAR -> TriggerType.RENDER_HOTBAR.triggerAll(event)
            RenderGameOverlayEvent.ElementType.AIR -> TriggerType.RENDER_AIR.triggerAll(event)
            RenderGameOverlayEvent.ElementType.TEXT -> TriggerType.RENDER_OVERLAY.triggerAll(event)
        }

        //#if MC==11602
        //$$ Renderer.popMatrixStack()
        //#endif
    }

    @SubscribeEvent
    fun onGuiOpened(event: GuiOpenEvent) {
        TriggerType.GUI_OPENED.triggerAll(event)
    }

    @SubscribeEvent
    //#if MC==11602
    //$$ fun onBlockHighlight(event: DrawHighlightEvent.HighlightBlock) {
    //$$     val position = event.target?.hitVec ?: return
    //#else
    fun onBlockHighlight(event: DrawBlockHighlightEvent) {
        if (event.target == null)
            return

        val position = Vector3f(
            event.target.blockPos.x.toFloat(),
            event.target.blockPos.y.toFloat(),
            event.target.blockPos.z.toFloat()
        )

    //#endif
        TriggerType.BLOCK_HIGHLIGHT.triggerAll(position, event)
    }

    @SubscribeEvent
    fun onPickupItem(event: EntityItemPickupEvent) {
        //#if MC==11602
        //$$ val player = event.entity
        //#else
        val player = event.entityPlayer
        //#endif

        if (player !is EntityPlayerMP)
            return

        val item = event.item

        val position = Vector3f(
            item.posX.toFloat(),
            item.posY.toFloat(),
            item.posZ.toFloat(),
        )
        val motion = Vector3f(
            //#if MC==11602
            //$$ item.motion.x.toFloat(),
            //$$ item.motion.y.toFloat(),
            //$$ item.motion.z.toFloat(),
            //#else
            item.motionX.toFloat(),
            item.motionY.toFloat(),
            item.motionZ.toFloat(),
        //#endif
        )

        TriggerType.PICKUP_ITEM.triggerAll(
            //#if MC<=10809
            Item(item.entityItem),
            //#else
            //$$ Item(item.item),
            //#endif
            PlayerMP(player),
            position,
            motion,
            event,
        )
    }

    fun onHitBlock(pos: MCBlockPos, facing: EnumFacing): Boolean {
        val event = CancellableEvent()

        TriggerType.HIT_BLOCK.triggerAll(
            World.getBlockAt(pos.x, pos.y, pos.z).withFace(BlockFace.fromMCEnumFacing(facing)),
            event
        )

        return event.isCancelled()
    }

    fun onDropItem(player: EntityPlayer, item: ItemStack): Boolean {
        if (player !is EntityPlayerMP) return false

        val event = CancellableEvent()

        TriggerType.DROP_ITEM.triggerAll(
            Item(item),
            PlayerMP(player),
            event
        )

        return event.isCancelled()
    }

    @SubscribeEvent
    fun onGuiRender(e: GuiScreenEvent.BackgroundDrawnEvent) {
        //#if MC==11602
        //$$ Renderer.pushMatrixStack(e.matrixStack)
        //#else
        GlStateManager.pushMatrix()
        //#endif

        TriggerType.GUI_RENDER.triggerAll(
            //#if MC==11602
            //$$ Client.getMouseX(),
            //$$ Client.getMouseY(),
            //#else
            e.mouseX,
            e.mouseY,
            //#endif
            e.gui
        )

        //#if MC==11602
        //$$ Renderer.popMatrixStack()
        //#else
        GlStateManager.popMatrix()
        //#endif
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

        TriggerType.PLAYER_INTERACT.triggerAll(
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
    //$$        is PlayerInteractEvent.LeftClickEmpty -> PlayerInteractAction.LEFT_CLICK_EMPTY
    //$$        is PlayerInteractEvent.LeftClickBlock -> return
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

        //#if MC==11602
        //$$ RIGHT_CLICK_ENTITY,
        //$$ RIGHT_CLICK_ITEM,
        //$$ LEFT_CLICK_EMPTY,
        //#endif
        UNKNOWN
    }
}
