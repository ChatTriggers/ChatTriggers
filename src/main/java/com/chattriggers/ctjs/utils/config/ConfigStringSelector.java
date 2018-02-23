package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;

public class ConfigStringSelector extends ConfigOption {
    @Getter
    private String[] values;
    @Getter
    private Integer defaultValue;

    @Setter
    private Integer value = null;

    ConfigStringSelector(String name, Integer defaultValue, String[] values) {
        super(ConfigOption.Type.STRING_SELECTOR);

        this.name = name;
        this.defaultValue = defaultValue;
        this.values = values;
    }

    public String getValue() {
        try {
            if (this.value == null)
                return values[defaultValue];
            return values[value];
        } catch (IndexOutOfBoundsException exception) {
            if (values.length > 0)
                return values[0];
            else
                Console.getConsole().printStackTrace(exception);
        }

        return "";
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
