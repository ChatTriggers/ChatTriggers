package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.minecraft.libs.MinecraftVars;
import com.chattriggers.ctjs.triggers.TriggerType;
import io.sentry.Sentry;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldListener {
    private boolean shouldTriggerWorldLoad;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        shouldTriggerWorldLoad = true;
        Sentry.getStoredClient().setServerName(MinecraftVars.getServerName());
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        // world load trigger
        if (shouldTriggerWorldLoad) {
            TriggerType.WORLD_LOAD.triggerAll();
            shouldTriggerWorldLoad = false;
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        TriggerType.WORLD_UNLOAD.triggerAll();
        Sentry.getStoredClient().setServerName("Not connected");
    }

    @SubscribeEvent
    public void onSoundPlay(PlaySoundEvent event) {
        TriggerType.SOUND_PLAY.triggerAll(event);
    }
}
