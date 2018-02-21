package com.chattriggers.ctjs.utils.config;

import lombok.Getter;
import lombok.Setter;

public class ConfigBoolean {
    @Getter
    private String name;
    @Getter
    private Boolean defaultValue;

    @Setter
    private Boolean value = null;

    ConfigBoolean(String name, Boolean defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public Boolean getValue() {
        if (this.value == null)
            return this.defaultValue;
        return this.value;
    }
}
