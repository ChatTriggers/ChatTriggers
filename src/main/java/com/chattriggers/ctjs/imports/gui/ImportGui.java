package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Import;
import net.minecraft.client.gui.GuiScreen;

public class ImportGui extends GuiScreen {
    private Import theImport;

    public ImportGui(Import theImport) {
        this.theImport = theImport;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        theImport.getLines();
    }
}
