package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.imports.gui.ModulesGui;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class ClientListener {
    private int ticksPassed;
    private KeyBinding guiKeyBind;

    public ClientListener() {
        this.guiKeyBind = new KeyBinding("Key to open import gui", Keyboard.KEY_L, "CT Controls");
        ClientRegistry.registerKeyBinding(this.guiKeyBind);

        ticksPassed = 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld == null) return;

        TriggerType.TICK.triggerAll(ticksPassed);
        ticksPassed++;
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            // render overlay trigger
            TriggerType.RENDER_OVERLAY.triggerAll();

            // step trigger
            TriggerType.STEP.triggerAll();
        }

        CTJS.getInstance().getCps().clickCalc();
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (event.button == 0 && event.buttonstate) CTJS.getInstance().getCps().addLeftClicks();
        if (event.button == 1 && event.buttonstate) CTJS.getInstance().getCps().addRightClicks();
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent e) {
        if (guiKeyBind.isPressed()) {
            CTJS.getInstance().getGuiHandler().openGui(new ModulesGui(CTJS.getInstance().getModuleManager().getModules()));
        }
    }
}
