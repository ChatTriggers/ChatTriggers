package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.engine.module.ModuleManager
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.triggers.EventType
import com.chattriggers.ctjs.triggers.TriggerType

object WorldListener : Initializer {
    private var playerList = mutableListOf<String>()

    override fun init() {
        CTJS.addEventListener(EventType.WorldLoad) {
            playerList.clear()
            TriggerType.WorldLoad.triggerAll()
            ModuleManager.pendingOldModules.forEach(ModuleManager::reportOldVersion)
            ModuleManager.pendingOldModules.clear()

            CTJS.sounds
                .filter { it.isListening }
                .forEach { it.onWorldLoad() }

            CTJS.sounds.clear()
        }

        CTJS.addEventListener(EventType.Tick) {
            World.getAllPlayers().filter {
                !playerList.contains(it.getName())
            }.forEach {
                playerList.add(it.getName())
                TriggerType.PlayerJoin.triggerAll(it)
            }

            playerList.removeIf {
                if (World.getPlayerByName(it) == null) {
                    TriggerType.PlayerLeave.triggerAll(it)
                    true
                } else false
            }
        }
    }
}
