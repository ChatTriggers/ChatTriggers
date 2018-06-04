package com.chattriggers.ctjs.minecraft.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.objects.Sound;
import com.chattriggers.ctjs.minecraft.wrappers.Server;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP;
import com.chattriggers.ctjs.triggers.TriggerType;
import io.sentry.Sentry;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class WorldListener {
    private boolean shouldTriggerWorldLoad;
    private List<String> playerList = new ArrayList<>();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.playerList = new ArrayList<>();
        this.shouldTriggerWorldLoad = true;

        Sentry.getStoredClient().setServerName(Server.getName());
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        // world load trigger
        if (shouldTriggerWorldLoad) {
            TriggerType.WORLD_LOAD.triggerAll();
            shouldTriggerWorldLoad = false;

            CTJS.getInstance()
                    .getSounds()
                    .stream()
                    .filter(Sound::isListening)
                    .forEach(Sound::onWorldLoad);

            CTJS.getInstance().getSounds().clear();
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        TriggerType.WORLD_UNLOAD.triggerAll();
        Sentry.getStoredClient().setServerName("Not connected");
    }

    @SubscribeEvent
    public void onSoundPlay(PlaySoundEvent event) {
        Vector3d position = new Vector3d(event.sound.getXPosF(), event.sound.getYPosF(), event.sound.getZPosF());
        TriggerType.SOUND_PLAY.triggerAll(
                event,
                position,
                event.name,
                event.sound.getVolume(),
                event.sound.getPitch(),
                event.category == null ? null : event.category.getCategoryName()
        );
    }

    @SubscribeEvent
    public void noteBlockEventPlay(NoteBlockEvent.Play event) {
        Vector3d position = new Vector3d(event.pos.getX(), event.pos.getY(), event.pos.getZ());
        TriggerType.NOTE_BLOCK_PLAY.triggerAll(event, position, event.getNote().name(), event.getOctave());
    }

    @SubscribeEvent
    public void noteBlockEventChange(NoteBlockEvent.Change event) {
        Vector3d position = new Vector3d(event.pos.getX(), event.pos.getY(), event.pos.getZ());
        TriggerType.NOTE_BLOCK_CHANGE.triggerAll(event, position, event.getNote().name(), event.getOctave());
    }

    @SubscribeEvent
    public void updatePlayerList(TickEvent.ClientTickEvent event) {
        for (PlayerMP player : World.getAllPlayers()) {
            if (!this.playerList.contains(player.getName())) {
                this.playerList.add(player.getName());
                TriggerType.PLAYER_JOIN.triggerAll(player);
                break;
            }
        }

        String currentPlayer;
        for (String player : this.playerList) {
            currentPlayer = player;
            try {
                World.getPlayerByName(player);
            } catch (Exception exception) {
                this.playerList.remove(currentPlayer);
                TriggerType.PLAYER_LEAVE.triggerAll(currentPlayer);
                break;
            }
        }
    }
}
