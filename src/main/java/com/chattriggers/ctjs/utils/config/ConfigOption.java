package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.CTJS;
import lombok.Getter;

public abstract class ConfigOption {
    @Getter
    protected String name;
    @Getter
    protected Type type;

    ConfigOption(Type type) {
        this.type = type;

        CTJS.getInstance().getConfig().addConfigOption(this);
    }

    abstract void draw(int mouseX, int mouseY);
    abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);
    abstract void keyTyped(char typedChar, int keyCode);

    public enum Type {
        STRING, STRING_SELECTOR, COLOR, BOOLEAN
    }
}
