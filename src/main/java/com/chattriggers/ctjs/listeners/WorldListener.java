package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.imports.gui.ModulesGui;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class WorldListener {
    private boolean shouldTriggerWorldLoad;
    private int ticksPassed;
    private KeyBinding guiKeyBind;

    public WorldListener() {
        ticksPassed = 0;
        this.guiKeyBind = new KeyBinding("Key to open import gui", Keyboard.KEY_L, "CT Controls");
        ClientRegistry.registerKeyBinding(this.guiKeyBind);
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
        if (Minecraft.getMinecraft().theWorld == null) return;

        TriggerType.TICK.triggerAll(ticksPassed);
        ticksPassed++;
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent e) {
        if (guiKeyBind.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ModulesGui(CTJS.getInstance().getScriptLoader().getLoadedImports()));
        }
    }
}
