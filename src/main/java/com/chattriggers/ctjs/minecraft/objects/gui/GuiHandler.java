package com.chattriggers.ctjs.minecraft.objects.gui;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GuiHandler {
    public static GuiHandler getInstance() {
        return instance;
    }

    private static GuiHandler instance;

    private HashMap<GuiScreen, Integer> GUIs;

    public GuiHandler() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);

        this.GUIs = new HashMap<>();
    }

    public void openGui(GuiScreen gui) {
        this.GUIs.put(gui, 1);
    }

    public void clearGuis() {
        this.GUIs.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Iterator<Map.Entry<GuiScreen, Integer>> it = this.GUIs.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<GuiScreen, Integer> gui = it.next();

            if (gui.getValue() == 0) {
                Minecraft.getMinecraft().displayGuiScreen(gui.getKey());
                it.remove();
            } else {
                this.GUIs.put(gui.getKey(), 0);
            }
        }
    }
}
