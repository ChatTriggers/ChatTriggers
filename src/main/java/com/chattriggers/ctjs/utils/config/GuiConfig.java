package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.Renderer;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class GuiConfig extends GuiScreen {
    @Getter
    private ArrayList<ConfigOption> configOptions;
    private Boolean isOpen = false;

    public GuiConfig() {
        this.configOptions = new ArrayList<>();
    }

    public void addConfigOption(ConfigOption configOption) {
        this.configOptions.add(configOption);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (!this.isOpen) {
            this.isOpen = true;
            for (ConfigOption configOption : this.configOptions)
                configOption.init();
        }

        CTJS.getInstance().getConfig().updateHidden();

        drawBackground(0);

        for (ConfigOption configOption : this.configOptions)
            configOption.draw(mouseX, mouseY);

        CTJS.getInstance().getIcon().setY(Renderer.getRenderHeight() - 64);
        CTJS.getInstance().getIcon().draw();
    }

    @Override
    public void onGuiClosed() {
        this.isOpen = false;
        CTJS.getInstance().saveConfig();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for (ConfigOption configOption : this.configOptions)
            configOption.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (ConfigOption configOption : this.configOptions)
            configOption.mouseReleased();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (ConfigOption configOption : this.configOptions)
            configOption.keyTyped(typedChar, keyCode);
    }
}
