package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraft.client.gui.GuiButton;

import javax.annotation.Nullable;

public class ConfigStringSelector extends ConfigOption {
    public Integer value = null;
    private Integer defaultValue;
    private String[] values;

    private transient GuiButton leftArrowButton;
    private transient GuiButton rightArrowButton;

    ConfigStringSelector(@Nullable ConfigStringSelector previous, String name, Integer defaultValue, String[] values, int x, int y) {
        super(ConfigOption.Type.STRING_SELECTOR);

        this.name = name;
        this.defaultValue = defaultValue;
        this.values = values;

        if (previous == null)
            this.value = this.defaultValue;
        else
            this.value = previous.value;

        this.x = x;
        this.y = y;
    }

    public String getValue() {
        try {
            return this.values[this.value];
        } catch (IndexOutOfBoundsException exception) {
            if (this.values.length > 0)
                return this.values[0];
            else
                Console.getInstance().printStackTrace(exception);
        }

        return "";
    }

    @Override
    public void init() {
        super.init();

        this.leftArrowButton = new GuiButton(
                0,
                Renderer.screen.getWidth() / 2 - 100 + this.x,
                this.y + 15,
                30,
                20,
                "<"
        );

        this.rightArrowButton = new GuiButton(
                0,
                Renderer.screen.getWidth() / 2 + 70 + this.x,
                this.y + 15,
                30,
                20,
                ">"
        );
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.hidden) return;

        int middle = Renderer.screen.getWidth() / 2;

        Renderer.rectangle(0x80000000, middle - 105 + this.x, this.y - 5, 210, 45)
                .setShadow(0xd0000000, 3, 3)
                .draw();
        Renderer.text(this.name, middle - 100 + this.x, this.y).draw();

        Renderer.text(
                getValue(),
                middle + this.x - Renderer.getStringWidth(getValue()) / 2,
                this.y + 20
        ).draw();

        this.leftArrowButton.xPosition = middle - 100 + this.x;
        this.rightArrowButton.xPosition = middle + 70 + this.x;
        this.leftArrowButton.drawButton(Client.getMinecraft(), mouseX, mouseY);
        this.rightArrowButton.drawButton(Client.getMinecraft(), mouseX, mouseY);

        super.draw(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (this.hidden) return;

        if (this.leftArrowButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value--;

            if (this.value < 0) this.value = this.values.length - 1;

            this.leftArrowButton.playPressSound(Client.getMinecraft().getSoundHandler());
        } else if (this.rightArrowButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value++;

            if (this.value >= this.values.length) this.value = 0;

            this.rightArrowButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }

        if (this.resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = this.defaultValue;
            this.resetButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
    }
}
