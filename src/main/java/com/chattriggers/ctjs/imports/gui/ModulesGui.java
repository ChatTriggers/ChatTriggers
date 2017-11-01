package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Module;
import com.chattriggers.ctjs.imports.ModuleMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

public class ModulesGui extends GuiScreen {
    private FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
    private ArrayList<Module> modules;
    private int scrolled;

    private ScaledResolution res;

    public ModulesGui(ArrayList<Module> modules) {
        this.modules = modules;
        this.scrolled = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.res = new ScaledResolution(Minecraft.getMinecraft());

        drawBackground(0);

        // draw scroll bar
        rectangle(
                this.res.getScaledWidth() - 5,
                10 + scrolled / 5,
                5,
                50,
                0xa0000000
        );

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            if (module.getMetadata() == null) {
                drawModule(module, i);
            } else {
                drawModule(module, module.getMetadata(), i);
            }

        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);

            if (isHovered(module, mouseX, mouseY, i)) {

            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int i = Mouse.getEventDWheel();
        if (i == 0) return;

        if (i > 1) i = 1;
        if (i < -1) i = -1;

        if (isShiftKeyDown()) i *= 10;
        else i *= 20;

        this.scrolled -= i;
        if (this.scrolled > modules.size() * 110 + 10 - res.getScaledHeight())
            this.scrolled = modules.size() * 110 + 10 - res.getScaledHeight();
        if (this.scrolled < 0)
            this.scrolled = 0;

    }

    private void drawModule(Module module, int i) {
        int x = 20;
        int y = getModuleY(i);
        int width = this.res.getScaledWidth() - 40;
        int height = 100;

        // background
        rectangle(x, y, width, height, 0x80000000);

        // name
        ren.drawStringWithShadow(
                module.getName(),
                x + 2,
                y + 2,
                0xffffffff
        );
        rectangle(
                x + 2,
                y+12,
                width - 4,
                2,
                0xa0000000
        );

        // no metadata
        ren.drawStringWithShadow(
                "No metadata provided",
                x + 2,
                y + 20,
                0xffffffff
        );
    }

    private void drawModule(Module module, ModuleMetadata moduleMetadata, int i) {
        int x = 100;
        int y = getModuleY(i);
        int width = 200;
        int height = 50;

        // background
        rectangle(x, y, width, height, 0x50000000);

        // name
        ren.drawStringWithShadow(
                moduleMetadata.getName(),
                x + 2,
                y + 2,
                0xffffffff
        );

        // description
        ren.drawStringWithShadow(
                moduleMetadata.getDescription(),
                x + 2,
                y + 10,
                0xffffffff
        );

        // directory
        ren.drawStringWithShadow(
                "/mods/ChatTriggers/modules/" + module.getName() + "/",
                x + 2,
                y + height - 12,
                0xffffffff
        );
    }

    private int getModuleY(int i) {
        return i * 110 + 10 - scrolled;
    }

    private Boolean isHovered(Module module, int mouseX, int mouseY, int i) {
        return mouseX > 10 && mouseX < ren.getStringWidth(module.getName()) + 20
                && mouseY > getModuleY(i) && mouseY < getModuleY(i) + 10;
    }

    private void rectangle(int x, int y, int width, int height, int color) {
        drawRect(x, y, x+width, y+height, color);
    }

    private float easeOut(float from, float to ) {
        return from + (to - from) / 5f;
    }

    private void openModule(Module theModule) {
        Minecraft.getMinecraft().displayGuiScreen(new ModuleGui(theModule));
    }
}
