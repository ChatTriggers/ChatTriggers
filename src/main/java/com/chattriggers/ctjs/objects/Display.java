package com.chattriggers.ctjs.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.handlers.DisplayHandler;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.libs.RenderLib;
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
    @Getter @Setter
    private ArrayList<DisplayLine> lines;
    @Getter @Setter
    private float renderX;
    @Getter @Setter
    private float renderY;
    @Getter @Setter
    private boolean shouldRender;
    @Getter
    private DisplayHandler.Background background;
    @Getter
    private int backgroundColor;
    @Getter
    private DisplayHandler.Align align;
    @Getter
    private DisplayHandler.Order order;

    private FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;

    public Display() {
        lines = new ArrayList<>();

        renderX = 0;
        renderY = 0;

        shouldRender = false;

        background = DisplayHandler.Background.NONE;
        backgroundColor = 0x50000000;
        align = DisplayHandler.Align.LEFT;
        order = DisplayHandler.Order.DOWN;

        CTJS.getInstance().getDisplayHandler().registerDisplay(this);
    }



    /**
     * Sets a display's background type.
     * Use {@link DisplayHandler.Background}.
     * @param background the type of background
     * @return the display to allow for method chaining
     */
    public Display setBackground(DisplayHandler.Background background) {
        this.background = background;
        return this;
    }

    /**
     * Sets a display's background type using string input.
     * Use {@link DisplayHandler.Background}.
     * @param background the type of background
     * @return the display to allow for method chaining
     */
    public Display setBackground(String background) {
        switch (background.toUpperCase()) {
            case("FULL"):
                this.background = DisplayHandler.Background.FULL;
                break;
            case("PER_LINE"):
            case("PER LINE"):
                this.background = DisplayHandler.Background.PER_LINE;
                break;
        }
        return this;
    }

    /**
     * Sets a display's background color.
     * @param color the integer color of the background
     * @return the display to allow for method chaining
     */
    public Display setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }


    
    /**
     * Sets a display's text alignment.
     * Use {@link DisplayHandler.Align}.
     * @param align the type of alignment
     * @return the display to allow for method chaining
     */
    public Display setAlign(DisplayHandler.Align align) {
        this.align = align;
        return this;
    }

    /**
     * Sets a display's text alignment using string input.
     * Use {@link DisplayHandler.Align}.
     * @param align the type of alignment
     * @return the display to allow for method chaining
     */
    public Display setAlign(String align) {
        this.align = DisplayHandler.Align.getAlignByName(align);

        return this;
    }



    /**
     * Sets a display's line order.
     * Use {@link DisplayHandler.Order}.
     * @param order the order of lines
     * @return the display to allow method chaining
     */
    public Display setOrder(DisplayHandler.Order order) {
        this.order = order;
        return this;
    }

    /**
     * Sets a display's line order using string input.
     * Use {@link DisplayHandler.Order}.
     * @param order the order of lines
     * @return the display to allow method chaining
     */
    public Display setOrder(String order) {
        switch (order.toUpperCase()) {
            case("DOWN"):
                this.order = DisplayHandler.Order.DOWN;
                break;
            case("UP"):
                this.order = DisplayHandler.Order.UP;
                break;
        }
        return this;
    }



    /**
     * Sets a display line to a string.
     * @param lineNumber the line number to set (0 based)
     * @param line the string to set the line to
     * @return the display to allow for method chaining
     */
    public Display setLine(int lineNumber, String line) {
        while (this.lines.size() - 1 < lineNumber)
            this.lines.add(new DisplayLine(""));

        this.lines.set(lineNumber, new DisplayLine(line));

        return this;
    }

    /**
     * Sets a display line to a DisplayLine
     * @param lineNumber the line number to set (0 based)
     * @param line the DisplayLine to set the line to
     * @return the display to allow for method chaining
     */
    public Display setLine(int lineNumber, DisplayLine line) {
        while (this.lines.size() - 1 < lineNumber)
            this.lines.add(new DisplayLine(""));

        this.lines.set(lineNumber, line);

        return this;
    }

    /**
     * Gets a DisplayLine from a line in a display.
     * @param lineNumber the line number to get
     * @return the string in line of display
     */
    public DisplayLine getLine(int lineNumber) {
        try {
            return lines.get(lineNumber);
        } catch (Exception exception) {
            return new DisplayLine("");
        }
    }

    /**
     * Adds as many lines as you specify onto the end of
     * the display (appends them).
     * @param lines a variable amount of strings to add
     * @return the display to allow for method chaining
     */
    public Display addLines(String... lines) {
        for (String line : lines)
            this.lines.add(new DisplayLine(line));
        return this;
    }

    /**
     * Adds as many DisplayLines as you specify onto the
     * end of the display (appends them).
     * @param lines a variable amount of DisplayLine to add
     * @return the display to allow for method chaining
     */
    public Display addLines(DisplayLine... lines) {
        Collections.addAll(this.lines, lines);
        return this;
    }

    /**
     * Adds one line to a display.
     * @param line the string to add
     * @return the display to allow for method chaining
     */
    public Display addLine(String line) {
        return addLines(line);
    }

    /**
     * Adds one DisplayLine to a display.
     * @param line the DisplayLine to add
     * @return the display to allow for method chaining
     */
    public Display addLine(DisplayLine line) {
        return addLines(line);
    }

    /**
     * Adds a number of empty lines to a display.
     * @param lines the number of lines to add
     * @return the display to allow for method chaining
     */
    public Display addLines(int lines) {
        for (int i=0; i<lines; i++) {
            this.lines.add(new DisplayLine(""));
        }
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
     * Set the X and Y render position of the display.
     * @param renderX the x coordinate
     * @param renderY the y coordinate
     * @return the display to allow for method chaining
     */
    public Display setRenderLoc(int renderX, int renderY) {
        this.renderX = renderX;
        this.renderY = renderY;
        return this;
    }



    // Renders the display on to the player's screen.
    public void render() {
        if (!shouldRender) return;

        int maxWidth = 0;
        for (DisplayLine line : lines) {
            if (ren.getStringWidth(line.getText()) > maxWidth)
                maxWidth = ren.getStringWidth(line.getText());
        }

        if (this.background == DisplayHandler.Background.FULL) {
            if (this.order == DisplayHandler.Order.DOWN)
                drawBackground(this.renderX, this.renderY, maxWidth, lines.size()*10);
            else if (this.order == DisplayHandler.Order.UP)
                drawBackground(this.renderX, this.renderY + 10, maxWidth, -lines.size()*10);
        }

        int i = 0;

        for (DisplayLine line : lines) {
            if (!line.getText().equals("") && this.background == DisplayHandler.Background.PER_LINE)
                drawBackground(this.renderX, this.renderY + (i*10), ren.getStringWidth(line.getText()), 10);

            drawLine(line, this.renderX, this.renderY + (i * 10), maxWidth);

            if (order == DisplayHandler.Order.DOWN)
                i++;
            else if (order == DisplayHandler.Order.UP)
                i--;
        }
    }

    // helper method to draw background with align
    private void drawBackground(float x, float y, float width, float height) {
        if (this.align == DisplayHandler.Align.LEFT)
            drawRect(x, y, x + width, y + height, this.backgroundColor);
        if (this.align == DisplayHandler.Align.RIGHT)
            drawRect(x - width, y, x, y + height, this.backgroundColor);
        if (this.align == DisplayHandler.Align.CENTER)
            drawRect(x - width/2, y, x + width/2, y + height, this.backgroundColor);
    }

    // helper method to draw line with align
    private void drawLine(DisplayLine line, float x, float y, int maxWidth) {
        if (this.align == DisplayHandler.Align.LEFT)
            line.draw(x, 0,  y, maxWidth);
        else if (this.align == DisplayHandler.Align.RIGHT)
            line.draw(x,  - ren.getStringWidth(line.getText()), y, -maxWidth);
        else if (this.align == DisplayHandler.Align.CENTER)
            line.draw(x,  - ren.getStringWidth(line.getText())/2, y, 0);
    }

    private void drawRect(float left, float top, float right, float bottom, int color) {
        if (left < right) {
            float i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            float j = top;
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

    public static class DisplayLine {
        @Getter
        String text;
        int color;
        DisplayHandler.Align align;
        Boolean shadow;

        public DisplayLine(String text) {
            this.text = ChatLib.addColor(text);

            this.color = 0xffffffff;
            this.align = DisplayHandler.Align.LEFT;
            this.shadow = true;
        }

        /**
         * Sets the alignment of the line (based on max width of display)
         * @param align the alignment
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setAlign(String align) {
            this.align = DisplayHandler.Align.getAlignByName(align);
            return this;
        }

        /**
         * Sets the text color of the line
         * @param color the integer color
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setColor(int color) {
            this.color = color;
            return this;
        }

        /**
         * Sets if a drop shadow is drawn (true by default)
         * @param shadow if the shadow is drawn
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setShadow(boolean shadow) {
            this.shadow = shadow;
            return this;
        }

        private void draw(float x, float xOffset, float y, float maxWidth) {
            if (this.text.equals("")) return;

            if (this.align == DisplayHandler.Align.LEFT) {
                if (this.shadow)
                    RenderLib.drawStringWithShadow(this.text, x + xOffset, y, this.color);
                else
                    RenderLib.drawString(this.text, x + xOffset, y, 1, this.color, false);
            } else if (this.align == DisplayHandler.Align.RIGHT) {
                if (this.shadow)
                    RenderLib.drawStringWithShadow(this.text, x - RenderLib.getStringWidth(text) + maxWidth, y, this.color);
                else
                    RenderLib.drawString(this.text, x - RenderLib.getStringWidth(text) + maxWidth, y, 1, this.color, false);
            } else if (this.align == DisplayHandler.Align.CENTER) {
                if (this.shadow)
                    RenderLib.drawStringWithShadow(this.text, x - RenderLib.getStringWidth(text) / 2 + maxWidth / 2, y, this.color);
                else
                    RenderLib.drawString(this.text, x - RenderLib.getStringWidth(text) / 2 + maxWidth / 2, y, 1, this.color, false);
            }
        }
    }
}
