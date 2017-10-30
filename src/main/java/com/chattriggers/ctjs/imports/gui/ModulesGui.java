package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

public class ModulesGui extends GuiScreen {
    private ArrayList<Module> modules;

    public ModulesGui(ArrayList<Module> modules) {
        this.modules = modules;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void openModule(Module theModule) {
        Minecraft.getMinecraft().displayGuiScreen(new ModuleGui(theModule));
    }
}
