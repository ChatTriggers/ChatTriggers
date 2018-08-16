package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.CTJS;
import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;
import java.util.ArrayList;

public class GuiConfig extends GuiScreen {
    @Getter
    private static GuiConfig instance;

    @Getter
    private ArrayList<ConfigOption> configOptions;
    private Boolean isOpen = false;
    private IconHandler iconHandler;

    public GuiConfig() {
        instance = this;

        this.configOptions = new ArrayList<>();
        this.iconHandler = new IconHandler();
    }

    public void addConfigOption(ConfigOption configOption) {
        this.configOptions.add(configOption);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();

        if (!this.isOpen) {
            this.isOpen = true;
            for (ConfigOption configOption : this.configOptions)
                configOption.init();
        }

        Config.getInstance().updateHidden();

        drawBackground(0);

        for (ConfigOption configOption : this.configOptions)
            configOption.draw(mouseX, mouseY, partialTicks);

        iconHandler.drawIcons();

        GlStateManager.popMatrix();
    }

    @Override
    public void onGuiClosed() {
        this.isOpen = false;
        CTJS.getInstance().saveConfig();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton != 0) return;

        for (ConfigOption configOption : this.configOptions)
            configOption.mouseClicked(mouseX, mouseY);

        iconHandler.clickIcons(mouseX, mouseY);
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
