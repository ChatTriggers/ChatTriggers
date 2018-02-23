package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;

public class ConfigStringSelector extends ConfigOption {
    @Getter
    private transient String[] values;
    @Getter
    private Integer defaultValue;

    @Setter
    private Integer value = null;

    private transient int y;

    ConfigStringSelector(String name, Integer defaultValue, String[] values, int y) {
        super(ConfigOption.Type.STRING_SELECTOR);

        this.name = name;
        this.defaultValue = defaultValue;
        this.values = values;

        this.y = y;
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
