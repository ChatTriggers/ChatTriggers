package com.chattriggers.jsct.listeners;

import com.chattriggers.jsct.triggers.TriggerRegister;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldListener {
    private boolean shouldTriggerWorldLoad;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        shouldTriggerWorldLoad = true;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (shouldTriggerWorldLoad) {
            TriggerRegister.TriggerTypes.triggerAllOfType(TriggerRegister.TriggerTypes.WORLD_LOAD);
            shouldTriggerWorldLoad = false;
        }
    }

    @SubscribeEvent
    public void onSoundPlay(PlaySoundEvent event) {
        TriggerRegister.TriggerTypes.triggerAllOfType(TriggerRegister.TriggerTypes.SOUND_PLAY, event);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        TriggerRegister.TriggerTypes.triggerAllOfType(TriggerRegister.TriggerTypes.TICK);
    }
}
