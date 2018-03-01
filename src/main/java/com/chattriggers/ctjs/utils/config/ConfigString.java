package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Setter;
import net.minecraft.client.gui.GuiTextField;

import javax.annotation.Nullable;
import java.io.File;

public class ConfigString extends ConfigOption {
    public String value = null;
    private String defaultValue;

    private transient GuiTextField textField;
    private transient long systemTime;
    @Setter
    private boolean isValid;
    @Setter
    private boolean isDirectory;

    ConfigString(@Nullable ConfigString previous, String name, String defaultValue, int x, int y) {
        super(ConfigOption.Type.STRING);

        this.name = name;
        this.defaultValue = defaultValue;

        if (previous == null)
            this.value = this.defaultValue;
        else
            this.value = previous.value;

        this.x = x;
        this.y = y;
        this.systemTime = Client.getSystemTime();
        this.isValid = true;
        this.isDirectory = false;
    }

    private void updateValidDirectory(String directory) {
        this.isValid = !this.isDirectory || new File(directory).isDirectory();
    }

    private String getIsValidColor() {
        if (this.isValid)
            return ChatLib.addColor("&a");
        return ChatLib.addColor("&c");
    }

    @Override
    public void init() {
        super.init();

        updateValidDirectory(this.value);
        this.textField = new GuiTextField(
                0,
                Renderer.getFontRenderer(),
                Renderer.getRenderWidth() / 2 - 100 + this.x,
                this.y + 15,
                200,
                20
        );
        this.textField.setMaxStringLength(100);
        this.textField.setText(getIsValidColor() + this.value);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.hidden) return;

        update();

        int middle = Renderer.getRenderWidth() / 2;

        Renderer.rectangle(0x80000000, middle - 105 + this.x, this.y - 5, 210, 45)
                .setShadow(0xd0000000, 3, 3)
                .draw();
        Renderer.text(this.name, middle - 100 + this.x, this.y).draw();

        this.textField.xPosition = middle - 100 + this.x;
        this.textField.drawTextBox();

        super.draw(mouseX, mouseY);
    }

    private void update() {
        while (this.systemTime < Client.getSystemTime() + 50) {
            this.systemTime += 50;
            this.textField.updateCursorCounter();
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.hidden) return;

        this.textField.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = this.defaultValue;
            this.textField.setText(getIsValidColor() + this.value);
            this.resetButton.playPressSound(Client.getMinecraft().getSoundHandler());
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.hidden) return;

        if (this.textField.isFocused()) {
            this.textField.textboxKeyTyped(typedChar, keyCode);

            String text = ChatLib.removeFormatting(this.textField.getText());
            updateValidDirectory(text);
            this.textField.setText(getIsValidColor() + text);

            if (this.isValid)
                this.value = text;
        }
    }
}
