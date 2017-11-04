package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Module;
import com.chattriggers.ctjs.libs.RenderLib;
import net.minecraft.client.gui.GuiScreen;

public class ModuleGui extends GuiScreen {
    private Module module;

    public ModuleGui(Module module) {
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawBackground(0);

        int i = 0;
        for (String line : module.getLines()) {
            RenderLib.drawStringWithShadow(
                    line.replace("\u0009", "     "),
                    0,
                    i*9,
                    0xffffffff
            );
            i++;
        }
    }
}
