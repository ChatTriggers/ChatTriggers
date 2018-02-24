package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiButton;

public class ConfigStringSelector extends ConfigOption {
    @Getter
    private transient String[] values;
    @Getter
    private Integer defaultValue;
    @Setter
    private Integer value = null;

    private transient GuiButton leftArrowButton;
    private transient GuiButton rightArrowButton;

    ConfigStringSelector(String name, Integer defaultValue, String[] values, int x, int y) {
        super(ConfigOption.Type.STRING_SELECTOR);

        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.values = values;

        this.x = x;
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
        this.leftArrowButton = new GuiButton(
                0,
                RenderLib.getRenderWidth() / 2 - 100 + this.x,
                this.y +5,
                30,
                20,
                "<"
        );

        this.rightArrowButton = new GuiButton(
                0,
                RenderLib.getRenderWidth() / 2 + 70 + this.x,
                this.y + 5,
                30,
                20,
                ">"
        );
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        RenderLib.drawRectangle(
                0x80000000,
                RenderLib.getRenderWidth() / 2 - 105 + this.x,
                this.y - 5,
                210,
                45
        );

        RenderLib.drawString(
                getValue(),
                RenderLib.getRenderWidth() / 2 - 50 + this.x,
                this.y + 10
        );

        this.leftArrowButton.drawButton(Client.getMinecraft(), mouseX, mouseY);
        this.rightArrowButton.drawButton(Client.getMinecraft(), mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.leftArrowButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value--;

            if (this.value < 0) this.value = this.values.length - 1;

            this.leftArrowButton.playPressSound(Client.getMinecraft().getSoundHandler());
        } else if (this.rightArrowButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value++;

            if (this.value >= this.values.length) this.value = 0;

            this.rightArrowButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
    }
}
