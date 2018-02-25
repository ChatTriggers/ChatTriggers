package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Setter;
import net.minecraft.client.gui.GuiButton;

public class ConfigBoolean extends ConfigOption {
    @Setter
    private Boolean value = null;
    private transient Boolean defaultValue;

    private transient GuiButton button;

    ConfigBoolean(String name, Boolean defaultValue, int x, int y) {
        super(ConfigOption.Type.BOOLEAN);

        this.name = name;
        this.defaultValue = defaultValue;

        this.x = x;
        this.y = y;
    }

    public Boolean getValue() {
        if (this.value == null)
            return this.defaultValue;
        return this.value;
    }

    private String getStringValue() {
        if (getValue())
            return ChatLib.addColor("&aTrue");
        return ChatLib.addColor("&cFalse");
    }

    @Override
    public void init() {
        this.button = new GuiButton(
                0,
                RenderLib.getRenderWidth() / 2 - 100 + this.x,
                this.y + 15,
                getStringValue()
        );
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        int middle = RenderLib.getRenderWidth() / 2;

        RenderLib.drawRectangle(0x80000000, middle - 105 + this.x, this.y - 5, 210, 45);
        RenderLib.drawString(this.name, middle - 100 + this.x, this.y);

        this.button.drawButton(Client.getMinecraft(), mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.button.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = !getValue();
            this.button.displayString = getStringValue();
            this.button.playPressSound(Client.getMinecraft().getSoundHandler());
        }
    }
}
