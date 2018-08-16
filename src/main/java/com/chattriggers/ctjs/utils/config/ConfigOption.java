package com.chattriggers.ctjs.utils.config;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import net.minecraft.client.gui.GuiButton;

public abstract class ConfigOption {
    @Getter
    protected String name;
    @Getter
    protected transient Type type;

    protected int x;
    protected int y;
    protected boolean hidden;

    transient GuiButton resetButton;

    ConfigOption(Type type) {
        this.type = type;

        GuiConfig.getInstance().addConfigOption(this);
    }

    public void init() {
        this.resetButton = new GuiButton(
                0,
                Renderer.screen.getWidth() / 2 - 100 + this.x + 185,
                this.y - 2,
                14,
                12,
                ""
        );
    }
    public void draw(int mouseX, int mouseY, float partialTicks) {
        //#if MC<=10809
        this.resetButton.xPosition = Renderer.screen.getWidth() / 2 - 100 + this.x + 185;
        //#else
        //$$ this.resetButton.x = Renderer.screen.getWidth() / 2 - 100 + this.x + 185;
        //#endif

        //#if MC<=10809
        this.resetButton.drawButton(Client.getMinecraft(), mouseX, mouseY);
        //#else
        //$$ this.resetButton.drawButton(Client.getMinecraft(), mouseX, mouseY, partialTicks);
        //#endif

        Renderer.text("\u21BA", Renderer.screen.getWidth() / 2 - 100 + this.x + 189, this.y - 4)
                .setScale(2)
                .setColor(0xffffffff)
                .setShadow(true)
                .draw();
    }
    public abstract void mouseClicked(int mouseX, int mouseY);
    public void mouseReleased() {}
    public void keyTyped(char typedChar, int keyCode) {}

    public enum Type {
        STRING, STRING_SELECTOR, COLOR, BOOLEAN
    }
}
