package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.CTJS;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class GuiConfig extends GuiScreen {
    private ArrayList<ConfigOption> configOptions;

    public GuiConfig() {
        this.configOptions = CTJS.getInstance().getConfig().getConfigOptions();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);

        for (ConfigOption configOption : configOptions)
            configOption.draw(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (ConfigOption configOption : configOptions)
            configOption.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (ConfigOption configOption : configOptions)
            configOption.keyTyped(typedChar, keyCode);
    }
}
