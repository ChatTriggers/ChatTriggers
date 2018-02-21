package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;

public class ConfigStringSelector {
    @Getter
    private String name;
    @Getter
    private String[] values;
    @Getter
    private Integer defaultValue;

    @Setter
    private Integer value = null;

    ConfigStringSelector(String name, Integer defaultValue, String[] values) {
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
}
