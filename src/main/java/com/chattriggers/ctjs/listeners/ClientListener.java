package com.chattriggers.ctjs.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.libs.EventLib;
import com.chattriggers.ctjs.libs.MinecraftVars;
import com.chattriggers.ctjs.modules.gui.ModulesGui;
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
import org.lwjgl.input.Mouse;

import java.io.IOException;

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
        if (MinecraftVars.getWorld() == null) return;

        TriggerType.TICK.triggerAll(ticksPassed);
        ticksPassed++;

        // mouse clicked trigger
        handleMouseInput();
    }

    private void handleMouseInput() {
        if (!Mouse.isCreated()) return;

        while (Mouse.next()) {
            if (Mouse.getEventButton() == -1) continue;
            TriggerType.CLICKED.triggerAll(MinecraftVars.getMouseX(), MinecraftVars.getMouseY(), Mouse.getEventButton(), Mouse.getEventButtonState());
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (EventLib.getType(event) != RenderGameOverlayEvent.ElementType.TEXT)
            return;


        // render overlay trigger
        TriggerType.RENDER_OVERLAY.triggerAll();

        // step trigger
        TriggerType.STEP.triggerAll();

        CTJS.getInstance().getCps().clickCalc();
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (EventLib.getButton(event) == -1) return;

        // clicked trigger
        // TriggerType.CLICKED.triggerAll(EventLib.getButton(event), EventLib.getButtonState(event), event);

        // add to cps
        if (EventLib.getButton(event) == 0 && EventLib.getButtonState(event)) CTJS.getInstance().getCps().addLeftClicks();
        if (EventLib.getButton(event) == 1 && EventLib.getButtonState(event)) CTJS.getInstance().getCps().addRightClicks();
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent e) {
        if (guiKeyBind.isPressed()) {
            CTJS.getInstance().getGuiHandler().openGui(new ModulesGui(CTJS.getInstance().getModuleManager().getModules()));
        }
    }
}
