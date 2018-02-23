package com.chattriggers.ctjs.utils.config;

import lombok.Getter;

public abstract class ConfigOption {
    @Getter
    protected String name;
    @Getter
    protected Type type;

    ConfigOption(Type type) {
        this.type = type;

        //CTJS.getInstance().getConfig().addConfigOption(this);
    }

    public enum Type {
        STRING, STRING_SELECTOR, COLOR, BOOLEAN
    }
}
