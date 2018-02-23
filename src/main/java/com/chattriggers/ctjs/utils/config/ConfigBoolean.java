package com.chattriggers.ctjs.utils.config;

import lombok.Getter;
import lombok.Setter;

public class ConfigBoolean extends ConfigOption {
    @Getter
    private Boolean defaultValue;

    @Setter
    private Boolean value = null;

    ConfigBoolean(String name, Boolean defaultValue) {
        super(ConfigOption.Type.BOOLEAN);

        this.name = name;
        this.defaultValue = defaultValue;
    }

    public Boolean getValue() {
        if (this.value == null)
            return this.defaultValue;
        return this.value;
    }

    @Override
    void draw(int mouseX, int mouseY) {

    }

    @Override
    void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    void keyTyped(char typedChar, int keyCode) {

    }
}
