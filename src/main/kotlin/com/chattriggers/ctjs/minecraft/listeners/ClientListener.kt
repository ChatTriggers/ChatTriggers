package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.minecraft.libs.EventLib
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.item.ItemTossEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.input.Mouse
import javax.vecmath.Vector3d

@KotlinListener
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
        if (World.getWorld() == null) return

        TriggerType.TICK.triggerAll(this.ticksPassed)
        this.ticksPassed++

        Scoreboard.resetCache()
    }

    private fun handleMouseInput() {
        if (!Mouse.isCreated()) return

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

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        TriggerType.RENDER_WORLD.triggerAll(event.partialTicks)
    }

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent) {
        handleOverlayTriggers(event)

        if (EventLib.getType(event) != RenderGameOverlayEvent.ElementType.TEXT)
            return

        TriggerType.STEP.triggerAll()

        handleMouseInput()
    }

    private fun handleOverlayTriggers(event: RenderGameOverlayEvent) {
        val element = EventLib.getType(event)

        when (element) {
            RenderGameOverlayEvent.ElementType.PLAYER_LIST -> TriggerType.RENDER_PLAYER_LIST.triggerAll(event)
            RenderGameOverlayEvent.ElementType.CROSSHAIRS -> TriggerType.RENDER_CROSSHAIR.triggerAll(event)
            RenderGameOverlayEvent.ElementType.DEBUG -> TriggerType.RENDER_DEBUG.triggerAll(event)
            RenderGameOverlayEvent.ElementType.BOSSHEALTH -> TriggerType.RENDER_BOSS_HEALTH.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HEALTH -> TriggerType.RENDER_HEALTH.triggerAll(event)
            RenderGameOverlayEvent.ElementType.FOOD -> TriggerType.RENDER_FOOD.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HEALTHMOUNT -> TriggerType.RENDER_MOUNT_HEALTH.triggerAll(event)
            RenderGameOverlayEvent.ElementType.EXPERIENCE -> TriggerType.RENDER_EXPERIENCE.triggerAll(event)
            RenderGameOverlayEvent.ElementType.HOTBAR -> TriggerType.RENDER_HOTBAR.triggerAll(event)
            RenderGameOverlayEvent.ElementType.AIR -> TriggerType.RENDER_AIR.triggerAll(event)
            RenderGameOverlayEvent.ElementType.TEXT -> TriggerType.RENDER_OVERLAY.triggerAll(event)
        }
    }

    @SubscribeEvent
    fun onGuiOpened(event: GuiOpenEvent) {
        TriggerType.GUI_OPENED.triggerAll(event)
    }

    @SubscribeEvent
    fun onBlockHighlight(event: DrawBlockHighlightEvent) {
        if (event.target == null || event.target.blockPos == null) return

        val position = Vector3d(
                event.target.blockPos.x.toDouble(),
                event.target.blockPos.y.toDouble(),
                event.target.blockPos.z.toDouble()
        )

        TriggerType.BLOCK_HIGHLIGHT.triggerAll(event, position)
    }

    @SubscribeEvent
    fun onPickupItem(event: EntityItemPickupEvent) {
        if (event.entityPlayer !is EntityPlayerMP) return

        val player = event.entityPlayer as EntityPlayerMP

        val item = event.item

        val position = Vector3d(
                item.posX,
                item.posY,
                item.posZ
        )
        val motion = Vector3d(
                item.motionX,
                item.motionY,
                item.motionZ
        )

        TriggerType.PICKUP_ITEM.triggerAll(
                //#if MC<=10809
                Item(item.entityItem),
                //#else
                //$$ Item(item.item),
                //#endif
                PlayerMP(player),
                position,
                motion
        )
    }

    @SubscribeEvent
    fun onDropItem(event: ItemTossEvent) {
        if (event.player !is EntityPlayerMP) return

        val player = event.player as EntityPlayerMP
        val entityItem = event.entityItem

        val position = Vector3d(
                entityItem.posX,
                entityItem.posY,
                entityItem.posZ
        )
        val motion = Vector3d(
                entityItem.motionX,
                entityItem.motionY,
                entityItem.motionZ
        )

        TriggerType.DROP_ITEM.triggerAll(
                //#if MC<=10809
                Item(entityItem.entityItem),
                //#else
                //$$ Item(entityItem.item),
                //#endif
                PlayerMP(player),
                position,
                motion
        )
    }

    @SubscribeEvent
    fun onItemTooltip(e: ItemTooltipEvent) {
        TriggerType.TOOLTIP.triggerAll(
                e.toolTip,
                Item(e.itemStack)
        )
    }
}