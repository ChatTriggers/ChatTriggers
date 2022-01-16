package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockFace
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Initializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.util.TypedActionResult

object ClientListener : Initializer {
    private var ticksPassed: Int = 0
    private val scrollListeners = mutableListOf<(x: Double, y: Double, delta: Double) -> Unit>()
    private val clickListeners = mutableListOf<(x: Double, y: Double, button: Int, pressed: Boolean) -> Unit>()
    private val draggedListeners = mutableListOf<(deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) -> Unit>()

    @JvmStatic
    val chatHistory = mutableListOf<String>()

    @JvmStatic
    val actionBarHistory = mutableListOf<String>()

    override fun onInitialize() {
        ClientTickEvents.START_CLIENT_TICK.register {
            if (World.getWorld() != null) {
                TriggerType.Tick.triggerAll(ticksPassed)
                ticksPassed++
                Scoreboard.resetCache()
            }
        }

        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register { _, hitResult ->
            val event = CancellableEvent()
            TriggerType.BlockHighlight.triggerAll(hitResult!!.pos, event)
            event.isCanceled()
        }

        // TODO("fabric"): Include hand argument?
        AttackBlockCallback.EVENT.register { _, _, _, pos, direction ->
            val event = CancellableEvent()
            TriggerType.HitBlock.triggerAll(
                World.getBlockAt(BlockPos(pos)).withFace(BlockFace.fromDirection(direction)),
                event
            )

            event.actionResult()
        }

        // TODO("fabric"): Wrapper for hit result
        UseBlockCallback.EVENT.register { _, _, _, hitResult ->
            val event = CancellableEvent()
            TriggerType.PlayerInteract.triggerAll(
                PlayerInteractAction.RIGHT_CLICK_BLOCK,
                hitResult,
            )
            event.actionResult()
        }

        // TODO("fabric"): Wrapper for hit result
        UseItemCallback.EVENT.register { player, _, hand ->
            val item = player.getStackInHand(hand)
            val event = CancellableEvent()
            TriggerType.PlayerInteract.triggerAll(
                PlayerInteractAction.RIGHT_CLICK_ITEM,
                item,
            )
            TypedActionResult(event.actionResult(), item)
        }

        // TODO("fabric"): Wrapper for hit result
        UseEntityCallback.EVENT.register { _, _, _, _, hitResult ->
            val event = CancellableEvent()
            TriggerType.PlayerInteract.triggerAll(
                PlayerInteractAction.RIGHT_CLICK_ENTITY,
                hitResult,
            )
            event.actionResult()
        }
    }

    fun registerScrollListener(listener: (x: Double, y: Double, delta: Double) -> Unit) {
        scrollListeners.add(listener)
    }

    fun registerClickListener(listener: (x: Double, y: Double, button: Int, pressed: Boolean) -> Unit) {
        clickListeners.add(listener)
    }

    fun registerDraggedListener(listener: (deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) -> Unit) {
        draggedListeners.add(listener)
    }

    @JvmStatic
    fun onScroll(x: Double, y: Double, delta: Double) {
        scrollListeners.forEach { it(x, y, delta) }
    }

    @JvmStatic
    fun onClick(mouseX: Double, mouseY: Double, button: Int, pressed: Boolean) {
        clickListeners.forEach { it(mouseX, mouseY, button, pressed) }
    }

    @JvmStatic
    fun onDragged(deltaX: Double, deltaY: Double, mouseX: Double, mouseY: Double, button: Int) {
        draggedListeners.forEach { it(deltaX, deltaY, mouseX, mouseY, button) }
    }

    fun registerTriggerListeners() {
        registerScrollListener(TriggerType.Scrolled::triggerAll)
        registerClickListener(TriggerType.Clicked::triggerAll)
        registerDraggedListener(TriggerType.Dragged::triggerAll)
    }

    fun clearListeners() {
        scrollListeners.clear()
        clickListeners.clear()
        draggedListeners.clear()
    }

    // TODO(BREAKING): Remove left click and unknown (interact should be right-click only)
    /**
     * Used as a pass through argument in [com.chattriggers.ctjs.engine.IRegister.registerPlayerInteract].\n
     * Exposed in providedLibs as InteractAction.
     */
    enum class PlayerInteractAction {
        RIGHT_CLICK_BLOCK,
        RIGHT_CLICK_ENTITY,
        RIGHT_CLICK_ITEM,
    }
}
