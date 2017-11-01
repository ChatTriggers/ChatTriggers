package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class ModulesGui extends GuiScreen {
    private FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
    private ArrayList<Module> modules;

    public ModulesGui(ArrayList<Module> modules) {
        this.modules = modules;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);

            if (isHovered(module, mouseX, mouseY, i)) {
                module.setX(easeOut(module.getX(), 20));
            } else {
                module.setX(easeOut(module.getX(), 10));
            }

            ren.drawStringWithShadow(
                    module.getName(),
                    module.getX(),
                    getModuleY(i),
                    0xffffffff
            );
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);

            if (isHovered(module, mouseX, mouseY, i)) {

            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private int getModuleY(int i) {
        return i*10+100;
    }

    private Boolean isHovered(Module module, int mouseX, int mouseY, int i) {
        return mouseX > 10 && mouseX < ren.getStringWidth(module.getName()) + 20
                && mouseY > getModuleY(i) && mouseY < getModuleY(i) + 10;
    }

    private float easeOut(float from, float to ) {
        return from + (to - from) / 5f;
    }

    private void openModule(Module theModule) {
        Minecraft.getMinecraft().displayGuiScreen(new ModuleGui(theModule));
    }
}
