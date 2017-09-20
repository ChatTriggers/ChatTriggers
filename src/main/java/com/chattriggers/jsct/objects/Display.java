package com.chattriggers.jsct.objects;

import com.chattriggers.jsct.JSCT;
import com.chattriggers.jsct.libs.ChatLib;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

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
    @Getter
    private Background background;
    @Getter
    private int backgroundColor;

    public Display() {
        lines = new ArrayList<>();

        //TODO: Default render X & Y?
        renderX = 0;
        renderY = 0;

        shouldRender = false;

        background = Background.NONE;
        backgroundColor = 0x50000000;

        JSCT.getInstance().getDisplayHandler().registerDisplay(this);
    }

    public enum Background {
        NONE, FULL, PER_LINE
    }

    /**
     * Sets a display's background type
     * @param background the type of background
     * @return the display to allow for method chaining
     */
    public Display setBackground(Background background) {
        this.background = background;
        return this;
    }

    /**
     * Sets a display's background color
     * @param color the color of the background
     * @return the display to allow for method chaining
     */
    public Display setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public static int color(int red, int green, int blue, int alpha) {
        return (alpha * 0x1000000) + (red * 0x10000) + (green * 0x100) + blue;
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
     * Gets a string from a line in a display
     * @param lineNumber the line number to get
     * @return the string in line of display
     */
    public String getLine(int lineNumber) {
        return lines.get(lineNumber);
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
            if (background == Background.PER_LINE)
                drawRect(renderX, renderY + (i*10), renderX + ren.getStringWidth(line), renderY + (i*10) + 10, backgroundColor);

            ren.drawStringWithShadow(line, renderX, renderY + (i*10), 0xffffffff);

            i++;
        }
    }

    //TODO: move somewhere to acutally be used in js
    private void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left+1, bottom-1, 0.0D).endVertex();
        worldrenderer.pos(right-2, bottom-1, 0.0D).endVertex();
        worldrenderer.pos(right-2, top-1, 0.0D).endVertex();
        worldrenderer.pos(left+1, top-1, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
