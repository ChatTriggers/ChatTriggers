package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.Tessellator
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP
import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.client.event.sound.PlaySoundEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.event.world.NoteBlockEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import org.lwjgl.util.vector.Vector3f

object WorldListener {
    private var shouldTriggerWorldLoad: Boolean = false
    private var playerList = mutableListOf<String>()

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        if (!shouldTriggerWorldLoad)
            TriggerType.GameLoad.triggerAll()
        playerList.clear()
        shouldTriggerWorldLoad = true
    }

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent.Pre) {
        if (!shouldTriggerWorldLoad) return

        TriggerType.WorldLoad.triggerAll()
        ModuleManager.pendingOldModules.forEach(ModuleManager::reportOldVersion)
        ModuleManager.pendingOldModules.clear()
        shouldTriggerWorldLoad = false

        CTJS.sounds
            .filter { it.isListening }
            .forEach { it.onWorldLoad() }

        CTJS.sounds.clear()
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        Tessellator.partialTicks = event.partialTicks
        TriggerType.RenderWorld.triggerAll(event.partialTicks)
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        TriggerType.WorldUnload.triggerAll()
    }

    @SubscribeEvent
    fun onSoundPlay(event: PlaySoundEvent) {
        val position = Vector3f(
            event.sound.xPosF,
            event.sound.yPosF,
            event.sound.zPosF
        )

        val vol = try {
            event.sound.volume
        } catch (ignored: Exception) {
            0
        }
        val pitch = try {
            event.sound.pitch
        } catch (ignored: Exception) {
            1
        }

        TriggerType.SoundPlay.triggerAll(
            position,
            event.name,
            vol,
            pitch,
            //#if MC<=10809
            event.category ?: event.category?.categoryName,
            //#else
            //$$ event.sound.category ?: event.sound.category.name,
            //#endif
            event
        )
    }

    @SubscribeEvent
    fun noteBlockEventPlay(event: NoteBlockEvent.Play) {
        val position = Vector3f(
            event.pos.x.toFloat(),
            event.pos.y.toFloat(),
            event.pos.z.toFloat()
        )

        TriggerType.NoteBlockPlay.triggerAll(
            position,
            event.note.name,
            event.octave,
            event
        )
    }

    @SubscribeEvent
    fun noteBlockEventChange(event: NoteBlockEvent.Change) {
        val position = Vector3f(
            event.pos.x.toFloat(),
            event.pos.y.toFloat(),
            event.pos.z.toFloat()
        )

        TriggerType.NoteBlockChange.triggerAll(
            position,
            event.note.name,
            event.octave,
            event
        )
    }

    @SubscribeEvent
    fun updatePlayerList(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END)
            return

        World.getAllPlayers().filter {
            !playerList.contains(it.getName())
        }.forEach {
            playerList.add(it.getName())
            TriggerType.PlayerJoin.triggerAll(it)
            return@forEach
        }

        val ite = playerList.listIterator()

        while (ite.hasNext()) {
            val it = ite.next()

            if (World.getPlayerByName(it) == null) {
                playerList.remove(it)
                TriggerType.PlayerLeave.triggerAll(it)
                break
            }
        }
    }

    @SubscribeEvent
    fun livingDeathEvent(event: LivingDeathEvent) {
        TriggerType.EntityDeath.triggerAll(
            Entity(event.entity)
        )
    }

    @SubscribeEvent
    fun attackEntityEvent(event: AttackEntityEvent) {
        TriggerType.EntityDamage.triggerAll(
            Entity(event.target),
            PlayerMP(event.entityPlayer)
        )
    }
}
