package com.chattriggers.ctjs.utils.config;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class ConfigColor extends ConfigOption {
    @Getter
    private Color defaultValue;

    @Setter
    private Color value = null;

    ConfigColor(String name, Color defaultValue) {
        super(ConfigOption.Type.COLOR);

        this.name = name;
        this.defaultValue = defaultValue;
    }

    public Color getValue() {
        if (value == null)
            return defaultValue;
        return value;
    }
}
