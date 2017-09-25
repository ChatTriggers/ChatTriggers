package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.triggers.TriggerRegister;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldListener {
    private boolean shouldTriggerWorldLoad;
    private int ticksPassed;

    public WorldListener() {
        ticksPassed = 0;
    }

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
        TriggerRegister.TriggerTypes.triggerAllOfType(TriggerRegister.TriggerTypes.TICK, ticksPassed);
        ticksPassed++;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            TriggerRegister.TriggerTypes.triggerAllOfType(TriggerRegister.TriggerTypes.RENDER_OVERLAY, event);
        }
    }
}
