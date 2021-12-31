package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.minecraft.libs.Tessellator
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.Entity
import com.chattriggers.ctjs.minecraft.wrappers.objects.entity.PlayerMP
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Initializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents

object WorldListener : Initializer {
    @JvmStatic
    var shouldTriggerWorldLoad: Boolean = false

    @JvmStatic
    var playerList = mutableListOf<String>()

    override fun onInitialize() {
        WorldRenderEvents.END.register {
            // TODO("fabric"): Verify tickDelta is same as partialTicks
            Tessellator.partialTicks = it.tickDelta()
            Tessellator.boundMatrixStack = it.matrixStack()
            TriggerType.RenderWorld.triggerAll(it.tickDelta())
        }

        // TODO("fabric"): Is this actually a server join/leave? Or just a render distance thing
        ClientEntityEvents.ENTITY_LOAD.register { entity, _ ->
            TriggerType.PlayerJoin.triggerAll(entity)
        }

        ClientEntityEvents.ENTITY_UNLOAD.register { entity, _ ->
            TriggerType.PlayerLeave.triggerAll(entity)
        }
    }

    @JvmStatic
    fun onPreOverlayRender() {
        if (!shouldTriggerWorldLoad)
            return

        TriggerType.WorldLoad.triggerAll()
        ModuleManager.pendingOldModules.forEach(ModuleManager::reportOldVersion)
        ModuleManager.pendingOldModules.clear()
        shouldTriggerWorldLoad = false

        CTJS.sounds
            .filter { it.isListening }
            .forEach { it.onWorldLoad() }

        CTJS.sounds.clear()
    }
}
