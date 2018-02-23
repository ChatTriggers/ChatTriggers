package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class ConfigColor extends ConfigOption {
    @Getter
    private Color defaultValue;

    @Setter
    private Color value = null;

    private transient int y;

    ConfigColor(String name, Color defaultValue, int y) {
        super(ConfigOption.Type.COLOR);

        this.name = name;
        this.defaultValue = defaultValue;

        this.y = y;
    }

    public Color getValue() {
        if (value == null)
            return defaultValue;
        return value;
    }

    @Override
    public void init() {

    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RenderLib.drawRectangle(
                0x80000000,
                RenderLib.getRenderWidth() / 2 - 105,
                this.y - 5,
                210,
                45
        );

        RenderLib.drawStringWithShadow(
                this.name,
                RenderLib.getRenderWidth() / 2 - 100,
                this.y
        );
    }

    @Override
    public void update() {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }
}
