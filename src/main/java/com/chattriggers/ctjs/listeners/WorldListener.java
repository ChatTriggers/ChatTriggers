package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.triggers.TriggerType;
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
        // world load trigger
        if (shouldTriggerWorldLoad) {
            TriggerType.WORLD_LOAD.triggerAll();
            shouldTriggerWorldLoad = false;
        }

        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            // render overlay trigger
            TriggerType.RENDER_OVERLAY.triggerAll();

            // step trigger
            TriggerType.STEP.triggerAll();
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        TriggerType.WORLD_UNLOAD.triggerAll();
    }

    @SubscribeEvent
    public void onSoundPlay(PlaySoundEvent event) {
        TriggerType.SOUND_PLAY.triggerAll(event);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        TriggerType.TICK.triggerAll(ticksPassed);
        ticksPassed++;
    }
}
