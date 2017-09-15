package com.chattriggers.jsct.listeners;

import com.chattriggers.jsct.triggers.TriggerRegister;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldListener {
    private boolean shouldTriggerWorldLoad;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e) {
        shouldTriggerWorldLoad = true;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent e) {
        if (shouldTriggerWorldLoad) {
            TriggerRegister.TriggerTypes.triggerAllOfType(TriggerRegister.TriggerTypes.WORLD_LOAD);
            shouldTriggerWorldLoad = false;
        }
    }
}
