package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.MathLib;
import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class ConfigColor extends ConfigOption {
    public Color value = null;
    private Color defaultValue;

    private transient GuiButton redButton, greenButton, blueButton;
    private transient boolean redHeld, blueHeld, greenHeld;

    ConfigColor(ConfigColor previous, String name, Color defaultValue, int x, int y) {
        super(ConfigOption.Type.COLOR);

        this.name = name;
        this.defaultValue = defaultValue;

        if (previous == null)
            this.value = this.defaultValue;
        else
            this.value = previous.value;

        this.x = x;
        this.y = y;
    }

    @Override
    public void init() {
        super.init();

        this.redHeld = false;
        this.blueHeld = false;
        this.greenHeld = false;

        int middle = RenderLib.getRenderWidth() / 2;

        this.redButton = new GuiButton(
                0,
                (int) MathLib.map(this.value.getRed(), 0, 255, middle - 100 + this.x, middle + 52 + this.x),
                this.y + 15,
                5,
                10,
                ""
        );

        this.greenButton = new GuiButton(
                0,
                (int) MathLib.map(this.value.getGreen(), 0, 255, middle - 100 + this.x, middle + 52 + this.x),
                this.y + 30,
                5,
                10,
                ""
        );

        this.blueButton = new GuiButton(
                0,
                (int) MathLib.map(this.value.getBlue(), 0, 255, middle - 100 + this.x, middle + 52 + this.x),
                this.y + 45,
                5,
                10,
                ""
        );
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.hidden) return;

        int middle = RenderLib.getRenderWidth() / 2;

        RenderLib.drawRectangle(0x80000000, middle - 105 + this.x, this.y - 5, 210, 65);
        RenderLib.drawString(this.name, middle - 100 + this.x, this.y);

        // red slider
        RenderLib.drawRectangle(0xff000000, middle - 101 + this.x, this.y + 18, 157, 5);
        RenderLib.drawRectangle(0xffaa0000, middle - 100 + this.x, this.y + 19, 155, 3);
        this.redButton.drawButton(Client.getMinecraft(), mouseX, mouseY);

        // green slider
        RenderLib.drawRectangle(0xff000000, middle - 101 + this.x, this.y + 33, 157, 5);
        RenderLib.drawRectangle(0xff008800, middle - 100 + this.x, this.y + 34, 155, 3);
        this.greenButton.drawButton(Client.getMinecraft(), mouseX, mouseY);

        // blue slider
        RenderLib.drawRectangle(0xff000000, middle - 101 + this.x, this.y + 48, 157, 5);
        RenderLib.drawRectangle(0xff0000cc, middle - 100 + this.x, this.y + 49, 155, 3);
        this.blueButton.drawButton(Client.getMinecraft(), mouseX, mouseY);

        // color preview
        RenderLib.drawRectangle(0xff000000, middle + this.x + 59, this.y + 14, 42, 42);
        RenderLib.drawRectangle(this.value.getRGB(), middle + this.x + 60, this.y + 15, 40, 40);

        handleHeldButtons(mouseX, middle);

        super.draw(mouseX, mouseY);
    }

    private void handleHeldButtons(int mouseX, int middle) {
        if (this.redHeld)
            this.redButton.xPosition = mouseX - 2;
        if (this.greenHeld)
            this.greenButton.xPosition = mouseX - 2;
        if (this.blueHeld)
            this.blueButton.xPosition = mouseX - 2;

        limitHeldButtons(this.redButton, this.blueButton, this.greenButton);

        this.value = new Color(
                (int) MathLib.map(this.redButton.xPosition, middle - 100 + this.x, middle + 52 + this.x, 0, 255),
                (int) MathLib.map(this.greenButton.xPosition, middle - 100 + this.x, middle + 52 + this.x, 0, 255),
                (int) MathLib.map(this.blueButton.xPosition, middle - 100 + this.x, middle + 52 + this.x, 0, 255)
        );
    }

    private void limitHeldButtons(GuiButton... buttons) {
        for (GuiButton button : buttons) {
            if (button.xPosition < RenderLib.getRenderWidth() / 2 - 100 + this.x)
                button.xPosition = RenderLib.getRenderWidth() / 2 - 100 + this.x;
            if (button.xPosition > RenderLib.getRenderWidth() / 2 + 52 + this.x)
                button.xPosition = RenderLib.getRenderWidth() / 2 + 52 + this.x;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.hidden) return;

        if (this.redButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.redHeld = true;
            this.redButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
        if (this.greenButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.greenHeld = true;
            this.greenButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
        if (this.blueButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.blueHeld = true;
            this.blueButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }

        if (this.resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = this.defaultValue;
            int middle = RenderLib.getRenderWidth() / 2;
            this.redButton.xPosition = (int) MathLib.map(this.value.getRed(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
            this.greenButton.xPosition = (int) MathLib.map(this.value.getGreen(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
            this.blueButton.xPosition = (int) MathLib.map(this.value.getBlue(), 0, 255, middle - 100 + this.x, middle + 52 + this.x);
            this.resetButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
    }

    @Override
    public void mouseReleased() {
        this.redHeld = false;
        this.blueHeld = false;
        this.greenHeld = false;
    }
}
