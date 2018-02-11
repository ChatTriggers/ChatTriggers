package com.chattriggers.ctjs.minecraft.listeners;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.EventLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.World;
import com.chattriggers.ctjs.modules.gui.ModulesGui;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.HashMap;

public class ClientListener {
    private int ticksPassed;
    private KeyBinding guiKeyBind;
    private HashMap<Integer, Boolean> mouseState;

    public ClientListener() {
        this.guiKeyBind = new KeyBinding("Key to open import gui", Keyboard.KEY_L, "CT Controls");
        ClientRegistry.registerKeyBinding(this.guiKeyBind);

        this.ticksPassed = 0;

        this.mouseState = new HashMap<>();
        for (int i = 0; i < 5; i++)
            this.mouseState.put(i, false);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (World.getWorld() == null) return;

        TriggerType.TICK.triggerAll(this.ticksPassed);
        this.ticksPassed++;
    }


    private void handleMouseInput() {
        if (!Mouse.isCreated()) return;

        for (int i = 0; i < 5; i++) {
            if (Mouse.isButtonDown(i) == this.mouseState.get(i)) continue;
            TriggerType.CLICKED.triggerAll(Client.getMouseX(), Client.getMouseY(), i, Mouse.isButtonDown(i));
            this.mouseState.put(i, Mouse.isButtonDown(i));
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

        // mouse clicked trigger
        handleMouseInput();

        CTJS.getInstance().getCps().clickCalc();
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (EventLib.getButton(event) == -1) return;

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

    @SubscribeEvent
    public void onGuiOpened(GuiOpenEvent event) {
        TriggerType.GUI_OPENED.triggerAll(event);
    }
}
