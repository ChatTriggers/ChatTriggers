package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Import;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;

public class ImportsGui extends GuiScreen {
    private ArrayList<Import> imports;

    public ImportsGui(ArrayList<Import> imports) {
        this.imports = imports;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void openImport(Import theImport) {
        Minecraft.getMinecraft().displayGuiScreen(new ImportGui(theImport));
    }
}
