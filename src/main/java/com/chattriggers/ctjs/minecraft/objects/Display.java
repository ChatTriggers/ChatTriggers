package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.handlers.DisplayHandler;
import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.RenderLib;
import com.chattriggers.ctjs.minecraft.wrappers.objects.Client;
import com.chattriggers.ctjs.triggers.OnRegularTrigger;
import com.chattriggers.ctjs.triggers.OnTrigger;
import com.chattriggers.ctjs.triggers.TriggerType;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Display {
    @Getter
    @Setter
    private ArrayList<DisplayLine> lines;
    @Getter
    @Setter
    private float renderX;
    @Getter
    @Setter
    private float renderY;
    @Getter
    @Setter
    private boolean shouldRender;
    @Getter
    private DisplayHandler.Background background;
    @Getter
    private int backgroundColor;
    @Getter
    private int textColor;
    @Getter
    private DisplayHandler.Align align;
    @Getter
    private DisplayHandler.Order order;
    @Getter
    private int minWidth;

    public Display() {
        this.lines = new ArrayList<>();

        this.renderX = 0;
        this.renderY = 0;

        this.shouldRender = false;

        this.background = DisplayHandler.Background.NONE;
        this.backgroundColor = 0x50000000;
        this.textColor = 0xffffffff;
        this.align = DisplayHandler.Align.LEFT;
        this.order = DisplayHandler.Order.DOWN;

        this.minWidth = 0;

        CTJS.getInstance().getDisplayHandler().registerDisplay(this);
    }


    /**
     * Sets a display's background type.
     * Use {@link com.chattriggers.ctjs.minecraft.handlers.DisplayHandler.Background}.
     *
     * @param background the type of background
     * @return the display to allow for method chaining
     */
    public Display setBackground(DisplayHandler.Background background) {
        this.background = background;
        return this;
    }

    /**
     * Sets a display's background type using string input.
     * Use {@link com.chattriggers.ctjs.minecraft.handlers.DisplayHandler.Background}.
     *
     * @param background the type of background
     * @return the display to allow for method chaining
     */
    public Display setBackground(String background) {
        this.background = DisplayHandler.Background.getBackgroundByName(background);
        return this;
    }

    /**
     * Sets a display's background color.
     *
     * @param color the integer color of the background
     * @return the display to allow for method chaining
     */
    public Display setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    /**
     * Sets a display's text color.
     *
     * @param color the integer color of the text
     * @return the display to allow for method chaining
     */
    public Display setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    /**
     * Sets a display's text alignment.
     * Use {@link DisplayHandler.Align}.
     *
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
     *
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
     *
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
     *
     * @param order the order of lines
     * @return the display to allow method chaining
     */
    public Display setOrder(String order) {
        this.order = DisplayHandler.Order.getOrderByName(order);
        return this;
    }


    /**
     * Sets a display line to a string.
     *
     * @param lineNumber the line number to set (0 based)
     * @param line       the string to set the line to
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
     *
     * @param lineNumber the line number to set (0 based)
     * @param line       the DisplayLine to set the line to
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
     *
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
     *
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
     *
     * @param lines a variable amount of DisplayLine to add
     * @return the display to allow for method chaining
     */
    public Display addLines(DisplayLine... lines) {
        Collections.addAll(this.lines, lines);
        return this;
    }

    /**
     * Adds one line to a display.
     *
     * @param line the string to add
     * @return the display to allow for method chaining
     */
    public Display addLine(String line) {
        return addLines(line);
    }

    /**
     * Adds one DisplayLine to a display.
     *
     * @param line the DisplayLine to add
     * @return the display to allow for method chaining
     */
    public Display addLine(DisplayLine line) {
        return addLines(line);
    }

    /**
     * Clears all the lines in the display.
     *
     * @return the display to allow for method chaining
     */
    public Display clearLines() {
        lines.clear();
        return this;
    }

    /**
     * Sets the minimum width of a display
     *
     * @param width the width to set
     * @return the display to allow for method chaining
     */
    public Display setMinWidth(int width) {
        this.minWidth = width;
        return this;
    }

    /**
     * Set the X and Y render position of the display.
     *
     * @param renderX the x coordinate
     * @param renderY the y coordinate
     * @return the display to allow for method chaining
     */
    public Display setRenderLoc(float renderX, float renderY) {
        this.renderX = renderX;
        this.renderY = renderY;
        return this;
    }


    // Renders the display on to the player's screen.
    public void render() {
        if (!shouldRender) return;

        int maxWidth = this.minWidth;
        for (DisplayLine line : lines) {
            if (RenderLib.getStringWidth(line.getText()) > maxWidth)
                maxWidth = (int) Math.ceil(RenderLib.getStringWidth(line.getText()) * line.getScale());
        }

        float i = 0;

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
            line.drawLeft(x, y, maxWidth, this.background, this.backgroundColor, this.textColor);
        else if (this.align == DisplayHandler.Align.RIGHT)
            line.drawRight(x, y, maxWidth, this.background, this.backgroundColor, this.textColor);
        else if (this.align == DisplayHandler.Align.CENTER)
            line.drawCenter(x, y, maxWidth, this.background, this.backgroundColor, this.textColor);
    }

    public static class DisplayLine {
        @Getter
        private String text;
        private int textWidth;

        private Integer textColor;
        private DisplayHandler.Align align;
        private Boolean shadow;
        @Getter
        private float scale;
        private DisplayHandler.Background background;
        private Integer backgroundColor;

        private OnTrigger onClicked;
        private OnTrigger onHovered;
        private OnTrigger onDragged;
        private HashMap<Integer, Boolean> mouseState;
        private HashMap<Integer, Float[]> draggedState;

        public DisplayLine(String text) {
            this.text = ChatLib.addColor(text);
            this.textWidth = RenderLib.getStringWidth(this.text);

            this.textColor = null;
            this.align = DisplayHandler.Align.NONE;
            this.shadow = true;
            this.scale = 1;
            this.background = DisplayHandler.Background.NONE;
            this.backgroundColor = null;

            this.onClicked = null;
            this.mouseState = new HashMap<>();
            for (int i = 0; i < 5; i++)
                this.mouseState.put(i, false);
            this.draggedState = new HashMap<>();
        }

        /**
         * Sets the line's text.<br>
         * Use this to update a display line instead of
         * re-instancing it every display update for dynamic lines
         *
         * @param text the text
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setText(String text) {
            this.text = ChatLib.addColor(text);
            this.textWidth = RenderLib.getStringWidth(this.text);
            return this;
        }

        /**
         * Sets the alignment of the line (based on max width of display)
         *
         * @param align the alignment
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setAlign(String align) {
            this.align = DisplayHandler.Align.getAlignByName(align);
            return this;
        }

        /**
         * Sets the text color of the line
         *
         * @param textColor the integer color
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * Sets if a drop shadow is drawn (true by default).
         *
         * @param shadow if the shadow is drawn
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setShadow(boolean shadow) {
            this.shadow = shadow;
            return this;
        }

        /**
         * Sets the scale of the text (1 by default).
         *
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
         * Use {@link com.chattriggers.ctjs.minecraft.handlers.DisplayHandler.Background}
         *
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
         * Use {@link com.chattriggers.ctjs.minecraft.handlers.DisplayHandler.Background}
         *
         * @param backgroundColor the background color int
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        /**
         * Registers a method to be run when the line is clicked (pressed or released).<br>
         * Arguments passed through to method:<br>
         * int mouseX<br>
         * int mouseY<br>
         * int button<br>
         * int button state
         *
         * @param methodName the method to run
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine registerClicked(String methodName) {
            onClicked = new OnRegularTrigger(methodName, TriggerType.OTHER);
            return this;
        }

        /**
         * Registers a method to be run when the line is hovered over.<br>
         * Arguments passed through to method:<br>
         * int mouseX<br>
         * int mouseY
         *
         * @param methodName the method to run
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine registerHovered(String methodName) {
            onHovered = new OnRegularTrigger(methodName, TriggerType.OTHER);
            return this;
        }

        /**
         * Registers a method to be run when the line is dragged.<br>
         * Arguments passed through to method:<br>
         * int deltaMouseX<br>
         * int deltaMouseY<br>
         * int mouseX<br>
         * int mouseY<br>
         * int button
         *
         * @param methodName the method to run
         * @return the DisplayLine to allow for method chaining
         */
        public DisplayLine registerDragged(String methodName) {
            onDragged = new OnRegularTrigger(methodName, TriggerType.OTHER);
            return this;
        }

        private void handleInput(float x, float y, float width, float height) {
            if (!Mouse.isCreated()) return;

            for (int button = 0; button < 5; button++)
                handleDragged(button);

            if (Client.getMouseX() > x && Client.getMouseX() < x + width
                    && Client.getMouseY() > y && Client.getMouseY() < y + height) {

                handleHovered();

                for (int button = 0; button < 5; button++) {
                    if (Mouse.isButtonDown(button) == this.mouseState.get(button)) continue;
                    handleClicked(button);
                    this.mouseState.put(button, Mouse.isButtonDown(button));
                    if (Mouse.isButtonDown(button))
                        this.draggedState.put(button, new Float[]{Client.getMouseX(), Client.getMouseY()});
                }
            }

            // remove dragged values if button is not down
            for (int button = 0; button < 5; button++) {
                if (Mouse.isButtonDown(button)) continue;
                if (!this.draggedState.containsKey(button)) continue;

                this.draggedState.remove(button);
            }
        }

        private void handleClicked(int button) {
            if (this.onClicked == null) return;

            this.onClicked.trigger(
                    Client.getMouseX(),
                    Client.getMouseY(),
                    button,
                    Mouse.isButtonDown(button)
            );
        }

        private void handleHovered() {
            if (this.onHovered == null) return;

            this.onHovered.trigger(
                    Client.getMouseX(),
                    Client.getMouseY()
            );
        }

        private void handleDragged(int button) {
            if (this.onDragged == null) return;

            if (!this.draggedState.containsKey(button))
                return;

            this.onDragged.trigger(
                    Client.getMouseX() - this.draggedState.get(button)[0],
                    Client.getMouseY() - this.draggedState.get(button)[1],
                    Client.getMouseX(),
                    Client.getMouseY(),
                    button
            );

            this.draggedState.put(button, new Float[]{Client.getMouseX(), Client.getMouseY()});
        }

        private void drawFullBG(DisplayHandler.Background bg, int color, float x, float y, float width, float height) {
            if (bg == DisplayHandler.Background.FULL)
                RenderLib.drawRectangle(color, x, y, width, height);
        }

        private void drawPerLineBG(DisplayHandler.Background bg, int color, float x, float y, float width, float height) {
            if (bg == DisplayHandler.Background.PER_LINE)
                RenderLib.drawRectangle(color, x, y, width, height);
        }

        private void drawLeft(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor, int textColor) {
            DisplayHandler.Background bg = background;
            int bgColor = backgroundColor;
            int textCol = textColor;

            // get default line
            if (this.backgroundColor != null)
                bgColor = this.backgroundColor;
            if (this.background != DisplayHandler.Background.NONE)
                bg = this.background;
            if (this.textColor != null)
                textCol = this.textColor;

            // full background
            drawFullBG(bg, bgColor, x - 1, y - 1, maxWidth + 2, 10 * this.scale);

            // blank line
            if (this.text.equals("")) return;

            // text and per line background
            float xOff = x;

            if (this.align == DisplayHandler.Align.RIGHT) {
                xOff = x - this.textWidth + maxWidth;
            } else if (this.align == DisplayHandler.Align.CENTER) {
                xOff = x - this.textWidth / 2 + maxWidth / 2;
            }

            drawPerLineBG(bg, bgColor, xOff - 1, y - 1, this.textWidth + 2, 10 * this.scale);
            RenderLib.drawString(this.text, xOff, y, this.scale, textCol, this.shadow);

            handleInput(xOff - 1, y - 1, this.textWidth + 2, 10 * this.scale);
        }

        private void drawRight(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor, int textColor) {
            DisplayHandler.Background bg = background;
            int bgColor = backgroundColor;
            int textCol = textColor;

            // get default line
            if (this.backgroundColor != null)
                bgColor = this.backgroundColor;
            if (this.background != DisplayHandler.Background.NONE)
                bg = this.background;
            if (this.textColor != null)
                textCol = this.textColor;

            // full background
            drawFullBG(bg, bgColor, x - maxWidth - 1, y - 1, maxWidth + 2, 10 * this.scale);

            // blank line
            if (this.text.equals("")) return;

            // text and per line background\
            float xOff = x - this.textWidth;

            if (this.align == DisplayHandler.Align.LEFT) {
                xOff = x - maxWidth;
            } else if (this.align == DisplayHandler.Align.CENTER) {
                xOff = x - this.textWidth / 2 - maxWidth / 2;
            }

            drawPerLineBG(bg, bgColor, xOff - 1, y - 1, this.textWidth + 2, 10 * this.scale);
            RenderLib.drawString(this.text, xOff, y, this.scale, textCol, this.shadow);

            handleInput(xOff - 1, y - 1, this.textWidth + 2, 10 * this.scale);
        }

        private void drawCenter(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor, int textColor) {
            DisplayHandler.Background bg = background;
            int bgColor = backgroundColor;
            int textCol = textColor;

            // get default line
            if (this.backgroundColor != null)
                bgColor = this.backgroundColor;
            if (this.background != DisplayHandler.Background.NONE)
                bg = this.background;
            if (this.textColor != null)
                textCol = this.textColor;

            // full background
            drawFullBG(bg, bgColor, x - maxWidth / 2 - 1, y - 1, maxWidth + 2, 10 * this.scale);

            // blank line
            if (this.text.equals("")) return;

            // text and per line background
            float xOff = x - this.textWidth / 2;

            if (this.align == DisplayHandler.Align.LEFT) {
                xOff = x - maxWidth / 2;
            } else if (this.align == DisplayHandler.Align.RIGHT) {
                xOff = x + maxWidth / 2 - this.textWidth;
            }

            drawPerLineBG(bg, bgColor, xOff - 1, y - 1, this.textWidth + 2, 10 * this.scale);
            RenderLib.drawString(this.text, xOff, y, this.scale, textCol, this.shadow);

            handleInput(xOff - 1, y - 1, this.textWidth + 2, 10 * this.scale);
        }
    }
}
