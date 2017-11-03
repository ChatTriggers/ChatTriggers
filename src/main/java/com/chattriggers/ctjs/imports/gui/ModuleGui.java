package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Module;
import net.minecraft.client.gui.GuiScreen;

public class ModuleGui extends GuiScreen {
    private Module module;

    public ModuleGui(Module module) {
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        module.getLines();
    }
}
