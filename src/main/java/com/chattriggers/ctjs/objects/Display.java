package com.chattriggers.ctjs.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.handlers.DisplayHandler;
import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.libs.RenderLib;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

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
        this.background = DisplayHandler.Background.getBackgroundByName(background);
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
        this.order = DisplayHandler.Order.getOrderByName(order);
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
                maxWidth = (int) Math.ceil(ren.getStringWidth(line.getText()) * line.getScale());
        }

        int i = 0;

        for (DisplayLine line : lines) {
            drawLine(line, this.renderX, this.renderY + (i * 10), maxWidth);

            if (order == DisplayHandler.Order.DOWN)
                i += line.getScale();
            else if (order == DisplayHandler.Order.UP)
                i -= line.getScale();
        }
    }

    // helper method to draw line with align
    private void drawLine(DisplayLine line, float x, float y, int maxWidth) {
        if (this.align == DisplayHandler.Align.LEFT)
            line.drawLeft(x, y, maxWidth, this.background, this.backgroundColor);
        else if (this.align == DisplayHandler.Align.RIGHT)
            line.drawRight(x, y, maxWidth, this.background, this.backgroundColor);
        else if (this.align == DisplayHandler.Align.CENTER)
            line.drawCenter(x, y, maxWidth, this.background, this.backgroundColor);
    }

    public static class DisplayLine {
        @Getter
        String text;

        int textWidth;

        int color;
        DisplayHandler.Align align;
        Boolean shadow;
        @Getter
        float scale;
        DisplayHandler.Background background;
        Integer backgroundColor;

        public DisplayLine(String text) {
            this.text = ChatLib.addColor(text);
            this.textWidth = RenderLib.getStringWidth(this.text);

            this.color = 0xffffffff;
            this.align = DisplayHandler.Align.NONE;
            this.shadow = true;
            this.scale = 1;
            this.background = DisplayHandler.Background.NONE;
            backgroundColor = null;
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
         * Sets if a drop shadow is drawn (true by default).
         * @param shadow if the shadow is drawn
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setShadow(boolean shadow) {
            this.shadow = shadow;
            return this;
        }

        /**
         * Sets the scale of the text (1 by default).
         * @param scale the scale of the text
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setScale(float scale) {
            this.scale = scale;
            this.textWidth = (int) Math.ceil(RenderLib.getStringWidth(text) * scale);
            return this;
        }

        /**
         * Sets the lines background (NONE by default).
         * If set to NONE, the line will inherit the background from the display.
         * @param background the background type
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setBackground(String background) {
            this.background = DisplayHandler.Background.getBackgroundByName(background);
            return this;
        }

        /**
         * Sets the line background color.
         * If not set, the line will inherit the background color from the display.
         * @param backgroundColor the background color int
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        private void drawLeft(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor) {
            // full background
            if (this.backgroundColor != null) backgroundColor = this.backgroundColor;

            if (this.background != DisplayHandler.Background.NONE)
                background = this.background;
            if (background == DisplayHandler.Background.FULL)
                RenderLib.drawRectangle(backgroundColor, x - 1, y - 1, maxWidth + 2, 10 * this.scale);

            // blank line
            if (this.text.equals("")) return;

            // text and per line background
            if (this.align == DisplayHandler.Align.NONE) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x, y, this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.LEFT) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x, y, this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.RIGHT) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - this.textWidth + maxWidth - 1, y-1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - this.textWidth + maxWidth, y, this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.CENTER) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - this.textWidth / 2 + maxWidth / 2 - 1, y-1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - this.textWidth / 2 + maxWidth / 2, y, this.scale, this.color, this.shadow);
            }
        }

        private void drawRight(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor) {
            // full background
            if (this.backgroundColor != null) backgroundColor = this.backgroundColor;

            if (this.background != DisplayHandler.Background.NONE)
                background = this.background;
            if (background == DisplayHandler.Background.FULL)
                RenderLib.drawRectangle(backgroundColor, x - maxWidth - 1, y - 1, maxWidth + 2, 10 * this.scale);

            // blank line
            if (this.text.equals("")) return;

            // text and per line background
            if (this.align == DisplayHandler.Align.NONE) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - this.textWidth - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - this.textWidth, y, this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.LEFT) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - maxWidth - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - maxWidth, y, this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.RIGHT) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - this.textWidth - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - this.textWidth, y , this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.CENTER) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - this.textWidth / 2 - maxWidth / 2 - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - this.textWidth / 2 - maxWidth / 2, y, this.scale, this.color, this.shadow);
            }
        }

        private void drawCenter(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor) {
            // full background
            if (this.backgroundColor != null) backgroundColor = this.backgroundColor;

            if (this.background != DisplayHandler.Background.NONE)
                background = this.background;
            if (background == DisplayHandler.Background.FULL)
                RenderLib.drawRectangle(backgroundColor, x - maxWidth / 2 - 1, y - 1, maxWidth + 2, 10 * this.scale);

            // blank line
            if (this.text.equals("")) return;

            // text and per line background
            if (this.align == DisplayHandler.Align.NONE) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - this.textWidth / 2 - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - this.textWidth / 2, y, this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.LEFT) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - maxWidth / 2 - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - maxWidth / 2, y, this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.RIGHT) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x + maxWidth / 2 - this.textWidth - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x + maxWidth / 2 - this.textWidth, y , this.scale, this.color, this.shadow);
            } else if (this.align == DisplayHandler.Align.CENTER) {
                if (background == DisplayHandler.Background.PER_LINE)
                    RenderLib.drawRectangle(backgroundColor, x - this.textWidth / 2 - 1, y - 1, this.textWidth + 2, 10 * this.scale);
                RenderLib.drawString(this.text, x - this.textWidth / 2, y, this.scale, this.color, this.shadow);
            }
        }
    }
}
