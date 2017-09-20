package com.chattriggers.jsct.objects;

import com.chattriggers.jsct.JSCT;
import com.chattriggers.jsct.libs.ChatLib;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.Collections;

public class Display {
    private ArrayList<String> lines;
    @Getter @Setter
    private int renderX;
    @Getter @Setter
    private int renderY;
    @Getter @Setter
    private boolean shouldRender;

    public Display() {
        lines = new ArrayList<>();

        //TODO: Default render X & Y?
        renderX = 0;
        renderY = 0;

        shouldRender = false;

        JSCT.getInstance().getDisplayHandler().registerDisplay(this);
    }

    /**
     * Sets a display line to a certain string.
     * @param lineNumber the line number to set (0 based)
     * @param line the string to set the line to
     * @return the display to allow for method chaining
     */
    public Display setLine(int lineNumber, String line) {
        lines.set(lineNumber, ChatLib.addColor(line));
        return this;
    }

    /**
     * Adds as many lines as you specify onto the end of
     * the display (appends them).
     * @param lines a variable amount of lines to add
     * @return the display to allow for method chaining
     */
    public Display addLines(String... lines) {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = ChatLib.addColor(lines[i]);
        }

        Collections.addAll(this.lines, lines);
        return this;
    }

    /**
     * Clears all the lines in the display.
     * @return the display to allow for method chaining
     */
    public Display clearLines() {
        lines.clear();
        return this;
    }

    /**
     * Set the X and Y render position of the display
     * @param renderX the x coordinate
     * @param renderY the y coordinate
     * @return the display to allow for method chaining
     */
    public Display setRenderLoc(int renderX, int renderY) {
        this.renderX = renderX;
        this.renderY = renderY;
        return this;
    }

    /**
     * Renders the display on to the player's screen.
     */
    public void render() {
        if (!shouldRender) return;

        FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
        int i = 0;

        for (String line : lines) {
            ren.drawStringWithShadow(line, renderX, renderY + (i*10), 0xffffffff);

            i++;
        }
    }
}
