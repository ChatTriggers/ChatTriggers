package com.chattriggers.ctjs.utils.config;

import lombok.Getter;
import lombok.Setter;

public class ConfigString extends ConfigOption {
    @Getter
    private String defaultValue;

    @Setter
    private String value = null;

    ConfigString(String name, String defaultValue) {
        super(ConfigOption.Type.STRING);

        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        if (value == null)
            return defaultValue;
        return value;
    }
}
