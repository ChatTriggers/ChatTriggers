package com.chattriggers.ctjs.imports.gui;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.imports.Module;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.libs.MathLib;
import com.chattriggers.ctjs.libs.RenderLib;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ModuleGui extends GuiScreen {
    private Module module;

    private int width;
    private int infoHeight;

    private int scrolled;
    private int maxScroll;

    private String name;
    private String version;
    private ArrayList<String> description;

    private boolean isHovered;

    ModuleGui(Module module) {
        this.module = module;

        updateScaling(0, 0);

        scrolled = 0;
        isHovered = false;

        name = ChatLib.addColor(this.module.getMetadata().getName() == null
                ? this.module.getName()
                : this.module.getMetadata().getName());
        version = this.module.getMetadata().getVersion() == null
                ? ""
                : "v" + this.module.getMetadata().getVersion();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        updateScaling(mouseX, mouseY);

        drawBackground(0);

        // scrollbar
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

        // info box
        RenderLib.drawRectangle(
                0x80000000,
                20,
                10 - scrolled,
                width,
                infoHeight
        );

        // name
        RenderLib.drawStringWithShadow(
                name,
                22,
                12 - scrolled,
                0xffffffff
        );

        // version
        RenderLib.drawStringWithShadow(
                ChatFormatting.GRAY + version,
                width - RenderLib.getStringWidth(version) +18,
                12 - scrolled,
                0xffffffff
        );

        // line break
        RenderLib.drawRectangle(0xa0000000, 22, 22 - scrolled, width - 4, 2);

        // description
        for (int i = 0; i < description.size(); i++) {
            RenderLib.drawStringWithShadow(
                    ChatLib.addColor(description.get(i)),
                    22,
                    30 + i * 10 - scrolled,
                    0xffffffff
            );
        }

        // directory
        RenderLib.drawStringWithShadow(
                ChatFormatting.DARK_GRAY + "/mods/ChatTriggers/modules/" + this.module.getName() + "/",
                22,
                infoHeight - scrolled,
                0xffffffff
        );

        // back
        String finalShowCode = isHovered ? "< back" : "&8< back";
        RenderLib.drawStringWithShadow(
                ChatLib.addColor(finalShowCode),
                20 + width - RenderLib.getStringWidth("< back") - 2,
                infoHeight - scrolled,
                0xffffffff
        );

        // code
        RenderLib.drawRectangle(
                0x80000000,
                20,
                infoHeight + 20 - scrolled,
                width,
                module.getLines().size() * 9
        );

        int i = 0;
        for (String line : module.getLines()) {
            RenderLib.drawStringWithShadow(
                    line.replace("\u0009", "     "),
                    22,
                    i * 9 + infoHeight + 22 - scrolled,
                    0xffffffff
            );
            i++;
        }

        // jump up
        if (scrolled > infoHeight) {
            RenderLib.drawRectangle(
                    0x80000000,
                    width + 20,
                    height - 20,
                    20,
                    20
            );
            RenderLib.drawStringWithShadow(
                    "^",
                    width + 31 - RenderLib.getStringWidth("^")/2,
                    height - 12,
                    0xffffffff
            );
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            if (isHovered)
                Minecraft.getMinecraft().displayGuiScreen(new ModulesGui(CTJS.getInstance().getModuleManager().getModules()));

            if (scrolled > infoHeight) {
                if (mouseX > width + 20
                        && mouseX < width + 40
                        && mouseY > height - 20
                        && mouseY < height) {
                    scrolled = 0;
                }
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

    private void updateScaling(int mouseX, int mouseY) {
        width = RenderLib.getRenderWidth() - 40;

        String preDescription = module.getMetadata().getDescription() == null
                ? "No description provided"
                : module.getMetadata().getDescription();
        description = RenderLib.lineWrap(new ArrayList<>(Arrays.asList(preDescription.split("\n"))), width - 5, 100);

        infoHeight = description.size()*10 + 35;

        maxScroll = this.module.getLines().size()*9 + infoHeight - RenderLib.getRenderHeight() + 30;

        isHovered = (mouseX > 20 + width - RenderLib.getStringWidth("< back") - 2
                && mouseX < 20 + width - 2
                && mouseY > infoHeight - scrolled - 2
                && mouseY < infoHeight - scrolled + 10);
    }
}
