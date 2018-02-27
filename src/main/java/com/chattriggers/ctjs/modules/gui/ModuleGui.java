package com.chattriggers.ctjs.modules.gui;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.Renderer;
import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.MathLib;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.*;

public class ModuleGui extends GuiScreen {
    private Module module;

    private int width;
    private int infoHeight;

    private int scrolled;
    private int maxScroll;

    private String name;
    private String version;
    private ArrayList<String> description;
    private HashMap<String, List<String>> coloredFiles;

    private boolean isHovered;

    ModuleGui(Module module) {
        this.module = module;

        updateScaling(0, 0, 0);

        scrolled = 0;
        isHovered = false;

        name = ChatLib.addColor(this.module.getMetadata().getDisplayName()== null
                ? this.module.getName()
                : this.module.getMetadata().getDisplayName());
        version = this.module.getMetadata().getVersion() == null
                ? ""
                : "v" + this.module.getMetadata().getVersion();

        coloredFiles = new HashMap<>();

        for (Map.Entry<String, List<String>> file : this.module.getFiles().entrySet()) {
            List<String> lines = new ArrayList<>();

            for (String line : file.getValue()) {
                lines.add(colorLine(line));
            }

            coloredFiles.put(file.getKey(), lines);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawBackground(0);

        // scrollbar
        drawScroll();

        // info box
        Renderer.rectangle(
                0x80000000,
                20, 10 - scrolled,
                width, infoHeight
        ).draw();

        // name
        Renderer.text(name, 22, 12 - scrolled)
                .setShadow(true).draw();

        // version
        Renderer.text(
                ChatFormatting.GRAY + version, width - Renderer.getStringWidth(version) + 18, 12 - scrolled)
                .setShadow(true).draw();

        // line break
        Renderer.rectangle(
                0xa0000000,
                22, 22 - scrolled,
                width - 4, 2
        ).draw();

        // description
        for (int i = 0; i < description.size(); i++) {
            Renderer.text(ChatLib.addColor(description.get(i)), 22, 30 + i * 10 - scrolled)
                    .setShadow(true).draw();
        }

        // directory
        Renderer.text(ChatFormatting.DARK_GRAY + CTJS.getInstance().getConfig().getModulesFolder().value + this.module.getName() + "/", 22, infoHeight - scrolled)
                .setShadow(true).draw();

        // back
        drawBack();

        // code
        int fileOffset = 20 + drawRequires();
        for (Map.Entry<String, List<String>> file : coloredFiles.entrySet()) {
            fileOffset += drawFile(file, fileOffset);
        }

        // jump up
        drawJump();

        updateScaling(mouseX, mouseY, fileOffset);
    }

    private int drawFile(Map.Entry<String, List<String>> file, int fileOffset) {
        Renderer.rectangle(
                0x80000000,
                20,
                infoHeight + fileOffset - scrolled,
                width,
                file.getValue().size() * 9 + 12
        ).draw();

        Renderer.text(ChatFormatting.DARK_GRAY + file.getKey(), 22, infoHeight + fileOffset - scrolled + 2)
                .setShadow(true)
                .draw();

        int i = 0;
        for (String line : file.getValue()) {
            Renderer.text(
                    colorLine(line).replace("\u0009", "     "), 22, i * 9 + infoHeight + fileOffset - scrolled + 12)
                    .setShadow(true)
                    .draw();
            i++;
        }

        return file.getValue().size() * 9 + 30;
    }

    private void drawJump() {
        if (scrolled > infoHeight) {
            Renderer.rectangle(
                    0x80000000,
                    width + 20,
                    height - 20,
                    20,
                    20
            ).draw();
            Renderer.text("^", width + 31 - Renderer.getStringWidth("^") / 2, height - 12)
                    .setShadow(true)
                    .draw();
        }
    }

    private void drawScroll() {
        int scrollHeight = Renderer.getRenderHeight() - this.maxScroll;
        if (scrollHeight < 20) scrollHeight = 20;
        if (scrollHeight < Renderer.getRenderHeight()) {
            int scrollY = (int) MathLib.map(this.scrolled, 0, this.maxScroll, 10, Renderer.getRenderHeight() - scrollHeight - 10);
            Renderer.rectangle(
                    0xa0000000,
                    Renderer.getRenderWidth() - 5,
                    scrollY,
                    5,
                    scrollHeight
            ).draw();
        }
    }

    private void drawBack() {
        String back = isHovered ? "< back" : "&8< back";
        Renderer.text(ChatLib.addColor(back), 20 + width - Renderer.getStringWidth("< back") - 2, infoHeight - scrolled)
                .setShadow(true)
                .draw();
    }

    private int drawRequires() {
        if (this.module.getMetadata().getRequires() == null) return 0;

        Renderer.text("Requires: " + this.module.getMetadata().getRequires(), 22, infoHeight - scrolled + 20)
                .setShadow(true)
                .draw();

        return 20;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            if (isHovered)
                CTJS.getInstance().getGuiHandler().openGui(new ModulesGui(CTJS.getInstance().getModuleManager().getModules()));

            if (scrolled > infoHeight
                    && mouseX > width + 20
                    && mouseX < width + 40
                    && mouseY > height - 20
                    && mouseY < height) {
                scrolled = 0;
            }
        }
    }

    private String colorLine(String line) {
        return ChatLib.addColor(
                line.replace("var", "&dvar&r")
                        .replace("function", "&dfunction&r")
                        .replaceAll("new (\\w+)\\(", "&dnew&r &e$1&r(")
                        .replace(" if", "&d if&r")
                        .replace("with", "&dwith&r")
                        .replace("while", "&dwhile&r")
                        .replace("catch", "&dcatch&r")
                        .replace("try", "&dtry&r")
                        .replace("else", "&delse&r")
                        .replace("=", "&b=&r")
                        .replace("|", "&b|&r")
                        .replace("^", "&b^&r")
                        .replace("//", "&7//")
                        .replace("+", "&b+&r")
                        .replace("-", "&b+&r")
                        .replaceAll("(\\d+)", "&6$1&r")
                        .replace("TriggerRegister", "&cTriggerRegister&r")
                        .replace("ChatLib", "&cChatLib&r")
                        .replace("WorldLib", "&cWorldLib&r")
                        .replace("Renderer", "&cRenderLib&r")
                        .replace("MathLib", "&cMathLib&r")
                        .replace("FileLib", "&cFileLib&r")
                        .replaceAll("(\\w+\\.)(\\w+)\\(", "$1&b$2&r(")
                        .replaceAll("\\.(\\w+)", ".&c$1&r")
                        .replaceAll("(\\w{2,})\\.", "&c$1&r.")
                        .replace("!", "&b!&r")
                        .replace("\"&", "sf8e8r9escapedquote")
        ).replace("sf8e8r9escapedquote", "\"&");
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

    private void updateScaling(int mouseX, int mouseY, int fileOffset) {
        width = Renderer.getRenderWidth() - 40;

        String preDescription = module.getMetadata().getDescription() == null
                ? "No description provided"
                : module.getMetadata().getDescription();
        description = Renderer.lineWrap(new ArrayList<>(Arrays.asList(preDescription.split("\n"))), width - 5, 100);

        infoHeight = description.size() * 10 + 35;

        maxScroll = fileOffset + infoHeight - Renderer.getRenderHeight();

        isHovered = (mouseX > 20 + width - Renderer.getStringWidth("< back") - 2
                && mouseX < 20 + width - 2
                && mouseY > infoHeight - scrolled - 2
                && mouseY < infoHeight - scrolled + 10);
    }
}
