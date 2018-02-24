package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiTextField;

public class ConfigString extends ConfigOption {
    @Getter
    private String defaultValue;

    @Setter
    private String value = null;

    private transient GuiTextField textField;
    private transient Long systemTime;

    ConfigString(String name, String defaultValue, int x, int y) {
        super(ConfigOption.Type.STRING);

        this.name = name;
        this.defaultValue = defaultValue;

        this.x = x;
        this.y = y;
        this.systemTime = Client.getSystemTime();
    }

    public String getValue() {
        if (value == null)
            return defaultValue;
        return value;
    }

    @Override
    public void init() {
        this.textField = new GuiTextField(
                0,
                RenderLib.getFontRenderer(),
                RenderLib.getRenderWidth() / 2 - 100 + this.x,
                this.y + 15,
                200,
                20
        );
        this.textField.setMaxStringLength(100);
        this.textField.setText(getValue());
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        update();

        RenderLib.drawRectangle(
                0x80000000,
                RenderLib.getRenderWidth() / 2 - 105 + this.x,
                this.y - 5,
                210,
                45
        );

        RenderLib.drawString(
                this.name,
                RenderLib.getRenderWidth() / 2 - 100 + this.x,
                this.y
        );

        this.textField.drawTextBox();
    }

    private void update() {
        while (this.systemTime < Client.getSystemTime() + 50) {
            this.systemTime += 50;
            this.textField.updateCursorCounter();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.textField.isFocused())
            this.textField.textboxKeyTyped(typedChar, keyCode);
    }
}
