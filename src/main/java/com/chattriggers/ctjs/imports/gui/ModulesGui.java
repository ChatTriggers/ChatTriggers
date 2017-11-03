package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Module;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.libs.RenderLib;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ModulesGui extends GuiScreen {
    private ArrayList<GuiModule> modules = new ArrayList<>();

    private int scrolled;
    private int maxScroll;

    private ScaledResolution res;
    private long sysTime;

    public ModulesGui(ArrayList<Module> modules) {
        int i = 0;
        for (Module module : modules) {
            this.modules.add(new GuiModule(module, i));
            i++;
        }

        this.res = new ScaledResolution(Minecraft.getMinecraft());
        this.sysTime = Minecraft.getSystemTime();

        this.scrolled = 0;
        this.maxScroll = this.modules.size() * 110 + 10 - this.res.getScaledHeight();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        while (Minecraft.getSystemTime() >= sysTime + 20L) {
            sysTime += 20L;
            for (GuiModule module : this.modules) {
                module.tick();
            }
        }

        this.res = new ScaledResolution(Minecraft.getMinecraft());
        this.maxScroll = this.modules.size() * 110 + 10 - this.res.getScaledHeight();

        drawBackground(0);

        // draw scroll bar
        int scrollHeight = res.getScaledHeight() - this.maxScroll;
        if (scrollHeight < 20) scrollHeight = 20;
        if (scrollHeight < res.getScaledHeight()) {
            int scrollY = (int) map(this.scrolled, 0, this.maxScroll, 10, res.getScaledHeight() - scrollHeight - 10);
            RenderLib.drawRectangle(
                    0xa0000000,
                    this.res.getScaledWidth() - 5,
                    scrollY,
                    5,
                    scrollHeight
            );
        }

        for (GuiModule module : this.modules) {
            module.draw();
            module.checkHover(mouseX, mouseY);
        }
    }

    private float map(float number, float in_min, float in_max, float out_min, float out_max) {
        return (number - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            for (GuiModule module : modules) {
                module.click();
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
        if (this.scrolled > this.maxScroll)
            this.scrolled = this.maxScroll;
        if (this.scrolled < 0)
            this.scrolled = 0;
    }

    private class GuiModule {
        private Module module;
        private int i;

        private boolean isHovered;
        private String showCode;
        private int showCodeLoc;

        private int x;
        private int y;

        private GuiModule(Module module, int i) {
            this.module = module;
            this.i = i;

            this.isHovered = false;
            showCode = "show code >";
            showCodeLoc = showCode.length();

            this.x = 20;
            this.y = getY(i);
        }

        private int getY(int i) {
            return i * 110 + 10 - scrolled;
        }

        private void click() {
            if (isHovered) {
                Minecraft.getMinecraft().displayGuiScreen(new ModuleGui(module));
            }
        }

        private void checkHover(int mouseX, int mouseY) {
            int width = res.getScaledWidth() - 40;
            int height = 105;

            isHovered = (mouseX > x + width - RenderLib.getStringWidth("show code >") - 2
                && mouseX < x + width - 2
                && mouseY > y + height - 12
                && mouseY < y + height - 2);
        }

        private void tick() {
            if (isHovered) {
                if (showCodeLoc > 0)
                    showCodeLoc--;
            } else {
                if (showCodeLoc < showCode.length())
                    showCodeLoc++;
            }
        }

        private void draw() {
            x = 20;
            y = getY(i);
            int width = res.getScaledWidth() - 40;
            int height = 105;

            // background
           RenderLib.drawRectangle(0x80000000, x, y, width, height);

            // name
            String name = (this.module.getMetadata().getName() == null) ? this.module.getName() : this.module.getMetadata().getName();
            RenderLib.drawStringWithShadow(
                    ChatLib.addColor(name),
                    x + 2,
                    y + 2,
                    0xffffffff
            );

            // version
            if (this.module.getMetadata().getVersion() != null) {
                String version = ChatFormatting.GRAY  + "v" + this.module.getMetadata().getVersion();
                RenderLib.drawStringWithShadow(
                        version,
                        x + width - RenderLib.getStringWidth(version) - 2,
                        y + 2,
                        0xffffffff
                );
            }

            // line break
            RenderLib.drawRectangle(0xa0000000, x + 2, y+12, width - 4, 2);

            // description
            String description = (this.module.getMetadata().getDescription() == null) ? "No description provided" : this.module.getMetadata().getDescription();
            ArrayList<String> descriptionLines = RenderLib.lineWrap(new ArrayList<>(Arrays.asList(description.split("\n"))), width - 5, 6);
            for (int j = 0; j < descriptionLines.size(); j++) {
                RenderLib.drawStringWithShadow(
                        ChatLib.addColor(descriptionLines.get(j)),
                        x + 2,
                        y + 20 + j * 10,
                        0xffffffff
                );
            }

            // directory
            RenderLib.drawStringWithShadow(
                    ChatFormatting.DARK_GRAY + "/mods/ChatTriggers/modules/" + this.module.getName() + "/",
                    x + 2,
                    y + height - 12,
                    0xffffffff
            );

            // show code
            String finalShowCode = showCode.substring(0, showCodeLoc) + "&r" + showCode.substring(showCodeLoc);
            RenderLib.drawStringWithShadow(
                    ChatFormatting.DARK_GRAY + ChatLib.addColor(finalShowCode),
                    x + width - RenderLib.getStringWidth("show code >") - 2,
                    y + height - 12,
                    0xffffffff
            );
        }
    }
}
