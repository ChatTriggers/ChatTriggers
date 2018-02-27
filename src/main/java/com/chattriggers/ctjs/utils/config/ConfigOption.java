package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.Renderer;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import net.minecraft.client.gui.GuiButton;

public abstract class ConfigOption {
    @Getter
    protected String name;
    @Getter
    protected transient Type type;

    protected int x;
    protected int y;
    protected boolean hidden;

    protected GuiButton resetButton;

    ConfigOption(Type type) {
        this.type = type;

        CTJS.getInstance().getGuiConfig().addConfigOption(this);
    }

    public void init() {
        this.resetButton = new GuiButton(
                0,
                Renderer.getRenderWidth() / 2 - 100 + this.x + 185,
                this.y - 2,
                14,
                12,
                ""
        );
    }
    public void draw(int mouseX, int mouseY) {
        this.resetButton.xPosition = Renderer.getRenderWidth() / 2 - 100 + this.x + 185;
        this.resetButton.drawButton(Client.getMinecraft(), mouseX, mouseY);

        Renderer.drawString(
                "\u21BA",
                Renderer.getRenderWidth() / 2 - 100 + this.x + 189,
                this.y - 4,
                2,
                0xffffffff,
                true
        );
    }
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
    public void mouseReleased() {}
    public void keyTyped(char typedChar, int keyCode) {}

    public enum Type {
        STRING, STRING_SELECTOR, COLOR, BOOLEAN
    }
}
