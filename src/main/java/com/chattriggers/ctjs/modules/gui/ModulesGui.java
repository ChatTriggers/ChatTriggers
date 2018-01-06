package com.chattriggers.ctjs.modules.gui;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.libs.MathLib;
import com.chattriggers.ctjs.libs.RenderLib;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ModulesGui extends GuiScreen {
    private ArrayList<GuiModule> modules = new ArrayList<>();

    private int scrolled;
    private int maxScroll;

    public ModulesGui(ArrayList<Module> modules) {
        int i = 0;
        for (Module module : modules) {
            this.modules.add(new GuiModule(module, i));
            i++;
        }

        this.scrolled = 0;
        this.maxScroll = this.modules.size() * 110 + 10 - RenderLib.getRenderHeight();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.maxScroll = this.modules.size() * 110 + 10 - RenderLib.getRenderHeight();

        drawBackground(0);

        // draw scroll bar
        int scrollHeight = RenderLib.getRenderHeight() - this.maxScroll;
        if (scrollHeight < 20) scrollHeight = 20;
        if (scrollHeight < RenderLib.getRenderHeight()) {
            int scrollY = (int) MathLib.map(this.scrolled, 0, this.maxScroll, 10, RenderLib.getRenderHeight() - scrollHeight - 10);
            RenderLib.drawRectangle(
                    0xa0000000,
                    RenderLib.getRenderWidth() - 5,
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

        private int x;
        private int y;

        private String name;

        private GuiModule(Module module, int i) {
            this.module = module;
            this.i = i;

            this.isHovered = false;

            this.x = 20;
            this.y = getY(i);

            name = ChatLib.addColor(this.module.getMetadata().getDisplayName() == null
                    ? this.module.getName()
                    : this.module.getMetadata().getDisplayName());
        }

        private int getY(int i) {
            return i * 110 + 10 - scrolled;
        }

        private void click() {
            if (isHovered) {
                CTJS.getInstance().getGuiHandler().openGui(new ModuleGui(module));
            }
        }

        private void checkHover(int mouseX, int mouseY) {
            int width = RenderLib.getRenderWidth() - 40;
            int height = 105;

            isHovered = (mouseX > x + width - RenderLib.getStringWidth("show code >") - 2
                && mouseX < x + width - 2
                && mouseY > y + height - 12
                && mouseY < y + height - 2);
        }

        private void draw() {
            x = 20;
            y = getY(i);
            int width = RenderLib.getRenderWidth() - 40;
            int height = 105;

            // background
           RenderLib.drawRectangle(0x80000000, x, y, width, height);

            // name
            RenderLib.drawStringWithShadow(
                    name,
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
            String description = (this.module.getMetadata().getDescription() == null)
                    ? "No description provided"
                    : this.module.getMetadata().getDescription();
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
                    ChatFormatting.DARK_GRAY + "/config/ChatTriggers/modules/" + this.module.getName() + "/",
                    x + 2,
                    y + height - 12,
                    0xffffffff
            );

            // show code
            String finalShowCode = isHovered ? "show code >" : "&8show code >";
            RenderLib.drawStringWithShadow(
                    ChatLib.addColor(finalShowCode),
                    x + width - RenderLib.getStringWidth("show code >") - 2,
                    y + height - 12,
                    0xffffffff
            );
        }
    }
}
