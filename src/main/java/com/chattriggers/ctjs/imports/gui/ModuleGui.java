package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Module;
import net.minecraft.client.gui.GuiScreen;

public class ModuleGui extends GuiScreen {
    private Module theModule;

    public ModuleGui(Module theModule) {
        this.theModule = theModule;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        theModule.getLines();
    }
}
