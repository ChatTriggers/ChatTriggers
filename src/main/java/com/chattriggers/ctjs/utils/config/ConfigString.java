package com.chattriggers.ctjs.utils.config;

import lombok.Getter;
import lombok.Setter;

public class ConfigString {
    @Getter
    private String name;
    @Getter
    private String defaultValue;

    @Setter
    private String value = null;

    ConfigString(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        if (value == null)
            return defaultValue;
        return value;
    }
}
