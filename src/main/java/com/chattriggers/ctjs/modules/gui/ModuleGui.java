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
        RenderLib.drawRectangle(
                0x80000000,
                20, 10 - scrolled,
                width, infoHeight
        );

        // name
        RenderLib.drawStringWithShadow(name,
                22, 12 - scrolled,
                0xffffffff
        );

        // version
        RenderLib.drawStringWithShadow(
                ChatFormatting.GRAY + version,
                width - RenderLib.getStringWidth(version) + 18, 12 - scrolled,
                0xffffffff
        );

        // line break
        RenderLib.drawRectangle(
                0xa0000000,
                22, 22 - scrolled,
                width - 4, 2
        );

        // description
        for (int i = 0; i < description.size(); i++) {
            RenderLib.drawStringWithShadow(
                    ChatLib.addColor(description.get(i)),
                    22, 30 + i * 10 - scrolled,
                    0xffffffff
            );
        }

        // directory
        RenderLib.drawStringWithShadow(
                ChatFormatting.DARK_GRAY + "/config/ChatTriggers/modules/" + this.module.getName() + "/",
                22, infoHeight - scrolled,
                0xffffffff
        );

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
        RenderLib.drawRectangle(
                0x80000000,
                20,
                infoHeight + fileOffset - scrolled,
                width,
                file.getValue().size() * 9 + 12
        );

        RenderLib.drawStringWithShadow(
                ChatFormatting.DARK_GRAY + file.getKey(),
                22,
                infoHeight + fileOffset - scrolled + 2,
                0xffffffff
        );

        int i = 0;
        for (String line : file.getValue()) {
            RenderLib.drawStringWithShadow(
                    colorLine(line).replace("\u0009", "     "),
                    22,
                    i * 9 + infoHeight + fileOffset - scrolled + 12,
                    0xffffffff
            );
            i++;
        }

        return file.getValue().size() * 9 + 30;
    }

    private void drawJump() {
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
                    width + 31 - RenderLib.getStringWidth("^") / 2,
                    height - 12,
                    0xffffffff
            );
        }
    }

    private void drawScroll() {
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
    }

    private void drawBack() {
        String back = isHovered ? "< back" : "&8< back";
        RenderLib.drawStringWithShadow(
                ChatLib.addColor(back),
                20 + width - RenderLib.getStringWidth("< back") - 2,
                infoHeight - scrolled,
                0xffffffff
        );
    }

    private int drawRequires() {
        if (this.module.getMetadata().getRequires() == null) return 0;

        RenderLib.drawStringWithShadow(
                "Requires: " + this.module.getMetadata().getRequires(),
                22,
                infoHeight - scrolled + 20,
                0xffffffff
        );

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
                        .replace("RenderLib", "&cRenderLib&r")
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
        width = RenderLib.getRenderWidth() - 40;

        String preDescription = module.getMetadata().getDescription() == null
                ? "No description provided"
                : module.getMetadata().getDescription();
        description = RenderLib.lineWrap(new ArrayList<>(Arrays.asList(preDescription.split("\n"))), width - 5, 100);

        infoHeight = description.size() * 10 + 35;

        maxScroll = fileOffset + infoHeight - RenderLib.getRenderHeight();

        isHovered = (mouseX > 20 + width - RenderLib.getStringWidth("< back") - 2
                && mouseX < 20 + width - 2
                && mouseY > infoHeight - scrolled - 2
                && mouseY < infoHeight - scrolled + 10);
    }
}
