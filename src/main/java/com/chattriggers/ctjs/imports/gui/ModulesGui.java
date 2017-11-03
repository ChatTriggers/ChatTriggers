package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.imports.Module;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.libs.RenderLib;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ModulesGui extends GuiScreen {
    private FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
    private ArrayList<GuiModule> modules = new ArrayList<>();

    private int scrolled;
    private int maxScroll;

    private ScaledResolution res;

    public ModulesGui(ArrayList<Module> modules) {
        int i = 0;
        for (Module module : modules) {
            this.modules.add(new GuiModule(module, i));
            i++;
        }

        this.scrolled = 0;

        this.res = new ScaledResolution(Minecraft.getMinecraft());
        this.maxScroll = this.modules.size() * 110 + 10 - this.res.getScaledHeight();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

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
        }
    }

    private float map(float number, float in_min, float in_max, float out_min, float out_max) {
        return (number - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
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

        private GuiModule(Module module, int i) {
            this.module = module;
            this.i = i;
        }

        private void open() {
            Minecraft.getMinecraft().displayGuiScreen(new ModuleGui(this.module));
        }

        private int getY(int i) {
            return i * 110 + 10 - scrolled;
        }

        private void draw() {
            int x = 20;
            int y = getY(i);
            int width = res.getScaledWidth() - 40;
            int height = 105;

            // background
           RenderLib.drawRectangle(0x80000000, x, y, width, height);

            // name
            String name = (this.module.getMetadata().getName() == null) ? this.module.getName() : this.module.getMetadata().getName();
            ren.drawStringWithShadow(
                    ChatLib.addColor(name),
                    x + 2,
                    y + 2,
                    0xffffffff
            );

            // version
            if (this.module.getMetadata().getVersion() != null) {
                String version = ChatFormatting.GRAY  + "v" + this.module.getMetadata().getVersion();
                ren.drawStringWithShadow(
                        version,
                        x + width - ren.getStringWidth(version) - 2,
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
                ren.drawStringWithShadow(
                        ChatLib.addColor(descriptionLines.get(j)),
                        x + 2,
                        y + 20 + j * 10,
                        0xffffffff
                );
            }

            // directory
            ren.drawStringWithShadow(
                    ChatFormatting.DARK_GRAY + "/mods/ChatTriggers/modules/" + this.module.getName() + "/",
                    x + 2,
                    y + height - 12,
                    0xffffffff
            );

            // show code
            ren.drawStringWithShadow(
                    ChatLib.addColor("show code >"),
                    x + width - ren.getStringWidth("show code >") - 2,
                    y + height - 12,
                    0xffffffff
            );
        }
    }
}
