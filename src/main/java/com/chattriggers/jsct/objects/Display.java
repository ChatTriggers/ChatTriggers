package com.chattriggers.jsct.objects;

import com.chattriggers.jsct.JSCT;
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
     */
    public void setLine(int lineNumber, String line) {
        lines.set(lineNumber, line);
    }

    /**
     * Adds as many lines as you specify onto the end of
     * the display (appends them).
     * @param lines a variable amount of lines to add
     */
    public void addLines(String... lines) {
        Collections.addAll(this.lines, lines);
    }

    /**
     * Clears all the lines in the display.
     */
    public void clearLines() {
        lines.clear();
    }

    /**
     * Renders the display on to the player's screen.
     */
    public void render() {
        FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
        int i = 0;

        for (String line : lines) {
            ren.drawStringWithShadow(line, renderX, renderY + (i*10), 0xffffffff);

            i++;
        }
    }
}
