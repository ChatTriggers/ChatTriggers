package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiTextField;

public class ConfigString extends ConfigOption {
    @Getter
    private String defaultValue;

    @Setter
    private String value = null;

    private transient GuiTextField textField;
    private transient int y;

    ConfigString(String name, String defaultValue, int y) {
        super(ConfigOption.Type.STRING);

        this.name = name;
        this.defaultValue = defaultValue;

        this.y = y;
        this.textField = new GuiTextField(
                0,
                RenderLib.getFontRenderer(),
                RenderLib.getRenderWidth() / 2 - 68,
                this.y + 15,
                137,
                20
        );
        this.textField.setMaxStringLength(100);
        this.textField.setText("test");
        this.textField.setFocused(true);
        this.textField.setCursorPosition(0);
    }

    public String getValue() {
        if (value == null)
            return defaultValue;
        return value;
    }

    @Override
    void draw(int mouseX, int mouseY) {
        this.textField.updateCursorCounter();

        RenderLib.drawRectangle(
                0x50000000,
                RenderLib.getRenderWidth() / 2 - 105,
                y - 5,
                210,
                60
        );

        RenderLib.drawStringWithShadow(
                this.name,
                RenderLib.getRenderWidth() / 2 - 100,
                y
        );

        try {
            this.textField.drawTextBox();
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.textField.mouseClicked(mouseX, mouseX, mouseButton);
    }

    @Override
    void keyTyped(char typedChar, int keyCode) {
        if (this.textField.isFocused())
            this.textField.textboxKeyTyped(typedChar, keyCode);
    }
}
