package com.chattriggers.ctjs.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;

public class GuiHandler {
    private HashMap<GuiScreen, Integer> GUIs;

    public GuiHandler() {
        this.GUIs = new HashMap<>();
    }

    public void openGui(GuiScreen gui) {
        this.GUIs.put(gui, 1);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        for (Map.Entry<GuiScreen, Integer> gui : this.GUIs.entrySet()) {
            if (gui.getValue() == 0) {
                Minecraft.getMinecraft().displayGuiScreen(gui.getKey());
                this.GUIs.remove(gui.getKey());
            } else {
                this.GUIs.put(gui.getKey(), 0);
            }
        }
    }
}
