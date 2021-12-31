package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.minecraft.wrappers.Scoreboard
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockFace
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Initializer
import com.chattriggers.ctjs.utils.kotlin.MCBlockPos
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.util.ActionResult
import net.minecraft.util.TypedActionResult

object ClientListener : Initializer {
    private var ticksPassed: Int = 0
    val chatHistory = mutableListOf<String>()
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
