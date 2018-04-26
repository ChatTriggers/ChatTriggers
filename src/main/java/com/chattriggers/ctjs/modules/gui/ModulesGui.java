package com.chattriggers.ctjs.modules.gui;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.MathLib;
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.libs.renderer.Text;
import com.chattriggers.ctjs.minecraft.objects.gui.GuiHandler;
import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.utils.config.Config;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;

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
        this.maxScroll = this.modules.size() * 110 + 10 - Renderer.screen.getHeight();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.pushMatrix();

        this.maxScroll = this.modules.size() * 110 + 10 - Renderer.screen.getHeight();

        drawBackground(0);

        // draw scroll bar
        int scrollHeight = Renderer.screen.getHeight() - this.maxScroll;
        if (scrollHeight < 20) scrollHeight = 20;
        if (scrollHeight < Renderer.screen.getHeight()) {
            int scrollY = (int) MathLib.map(this.scrolled, 0, this.maxScroll, 10, Renderer.screen.getHeight() - scrollHeight - 10);
            Renderer.drawRect(
                    0xa0000000,
                    Renderer.screen.getWidth() - 5,
                    scrollY,
                    5,
                    scrollHeight
            );
        }

        for (GuiModule module : this.modules) {
            module.draw();
            module.checkHover(mouseX, mouseY);
        }

        GlStateManager.popMatrix();
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
                GuiHandler.getInstance().openGui(new ModuleGui(module));
            }
        }

        private void checkHover(int mouseX, int mouseY) {
            int width = Renderer.screen.getWidth() - 40;
            int height = 105;

            isHovered = (mouseX > x + width - Renderer.getStringWidth("show code >") - 2
                && mouseX < x + width - 2
                && mouseY > y + height - 12
                && mouseY < y + height - 2);
        }

        private void draw() {
            x = 20;
            y = getY(i);
            int width = Renderer.screen.getWidth() - 40;
            int height = 105;

            // background
            Renderer.drawRect(0x80000000, x, y, width, height);

            // name
            Renderer.text(name, x + 2, y + 2)
                    .setShadow(true).draw();

            // version
            if (this.module.getMetadata().getVersion() != null) {
                String version = ChatFormatting.GRAY  + "v" + this.module.getMetadata().getVersion();
                Renderer.text(version, x + width - Renderer.getStringWidth(version) - 2, y + 2)
                        .setShadow(true).draw();
            }

            // line break
            Renderer.drawRect(0xa0000000, x + 2, y+12, width - 4, 2);

            // description
            String description = (this.module.getMetadata().getDescription() == null)
                    ? "No description provided"
                    : this.module.getMetadata().getDescription();
            Text desc = Renderer.text(description, x + 2, y + 20).setWidth(width - 5).setMaxLines(6).draw();
            if (desc.exceedsMaxLines())
                Renderer.text("...", x + 2, y + 73).draw();


            // directory
            Renderer.text(ChatFormatting.DARK_GRAY + Config.getInstance().getModulesFolder().value + this.module.getName() + "/", x + 2, y + height - 12)
                    .setShadow(true).draw();

            // show code
            String finalShowCode = isHovered ? "show code >" : "&8show code >";
            Renderer.text(ChatLib.addColor(finalShowCode), x + width - Renderer.getStringWidth("show code >") - 2, y + height - 12)
                    .setShadow(true).draw();
        }
    }
}
