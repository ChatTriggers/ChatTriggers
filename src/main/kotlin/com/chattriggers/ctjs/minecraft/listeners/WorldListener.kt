package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.Server
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Block
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.kotlin.KotlinListener
import io.sentry.Sentry
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.event.world.NoteBlockEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import javax.vecmath.Vector3d
import javax.vecmath.Vector3f

@KotlinListener
object WorldListener {
    private var shouldTriggerWorldLoad: Boolean = false
    private var playerList: MutableList<String> = mutableListOf()

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        this.playerList.clear()
        this.shouldTriggerWorldLoad = true

        Sentry.getStoredClient().serverName = Server.getName()
    }

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent) {
        // world loadExtra trigger
        if (!shouldTriggerWorldLoad) return

        TriggerType.WORLD_LOAD.triggerAll()
        shouldTriggerWorldLoad = false

        CTJS.sounds
                .stream()
                .filter { it.isListening }
                .forEach { it.onWorldLoad() }

        CTJS.sounds.clear()
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        TriggerType.WORLD_UNLOAD.triggerAll()
        Sentry.getStoredClient().serverName = "Not connected"
    }

    @SubscribeEvent
    fun onSoundPlay(event: PlaySoundEvent) {
        val position = Vector3f(
                event.sound.xPosF,
                event.sound.yPosF,
                event.sound.zPosF
        )

        val vol = try { event.sound.volume } catch (ignored: Exception) { 0 }
        val pitch = try { event.sound.volume } catch (ignored: Exception) { 1 }

        TriggerType.SOUND_PLAY.triggerAll(
                event,
                position,
                event.name,
                vol,
                pitch,
                //#if MC<=10809
                event.category ?: event.category?.categoryName
                //#else
                //$$ event.sound.category ?: event.sound.category.name
                //#endif
        )
    }

    @SubscribeEvent
    fun noteBlockEventPlay(event: NoteBlockEvent.Play) {
        val position = Vector3d(
                event.pos.x.toDouble(),
                event.pos.y.toDouble(),
                event.pos.z.toDouble()
        )

        TriggerType.NOTE_BLOCK_PLAY.triggerAll(
                event,
                position,
                event.note.name,
                event.octave
        )
    }

    @SubscribeEvent
    fun noteBlockEventChange(event: NoteBlockEvent.Change) {
        val position = Vector3d(
                event.pos.x.toDouble(),
                event.pos.y.toDouble(),
                event.pos.z.toDouble()
        )

        TriggerType.NOTE_BLOCK_CHANGE.triggerAll(
                event,
                position,
                event.note.name,
                event.octave
        )
    }

    @SubscribeEvent
    fun updatePlayerList(event: TickEvent.ClientTickEvent) {
        World.getAllPlayers().filter {
            !playerList.contains(it.getName())
        }.forEach {
            playerList.add(it.getName())
            TriggerType.PLAYER_JOIN.triggerAll(this)
            return@forEach
        }

        val ite = playerList.listIterator()

        while (ite.hasNext()) {
            val it = ite.next()

            try {
                World.getPlayerByName(it)
            } catch (exception: Exception) {
                this.playerList.remove(it)
                TriggerType.PLAYER_LEAVE.triggerAll(it)
                break
            }
        }
    }

    @SubscribeEvent
    fun blockBreak(event: BlockEvent.BreakEvent) {
        TriggerType.BLOCK_BREAK.triggerAll(
            World.getBlockAt(event.pos.x, event.pos.y, event.pos.z),
            PlayerMP(event.player),
            event
        )
    }
}