package com.chattriggers.ctjs.minecraft.objects.display;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

import java.util.ArrayList;
import java.util.Collections;

@Accessors(chain = true)
public class Display {
    /**
     * -- GETTER --
     * Gets the list of lines currently displayed
     *
     * @return An ArrayList of {@link DisplayLine} objects
     *
     * -- SETTER --
     * Sets the list of lines currently displayed
     * @param lines An ArrayList of {@link DisplayLine} objects
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private ArrayList<DisplayLine> lines;

    /**
     * -- GETTER --
     * Gets the x position of the display
     *
     * @return The x position of the display
     *
     * -- SETTER --
     * Sets the x position of the display
     * @param renderX The new x location
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private float renderX;

    /**
     * -- GETTER --
     * Gets the y position of the display
     *
     * @return The y position of the display
     *
     * -- SETTER --
     * Sets the y position of the display
     * @param renderY The new y location
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private float renderY;

    /**
     * -- GETTER --
     * Gets the render status of the display (boolean)
     *
     * @return True if the display is set to render
     *
     * -- SETTER --
     * Sets whether or not the display should render
     * @param shouldRender True if the display should render
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private boolean shouldRender;

    /**
     * -- GETTER --
     * Gets a display's background type
     *
     * @return The display's background
     *
     * -- SETTER --
     * Sets a display's background type.
     * @param background The new display background
     * @return The Display object to allow for method chaining
     * @see DisplayHandler.Background
     * @see DisplayHandler.Background
     */
    @Getter
    @Setter
    private DisplayHandler.Background background;

    /**
     * -- GETTER --
     * Gets the display's background color
     *
     * @return The display's background color
     *
     * -- SETTER --
     * Sets the display's background color
     * @param backgroundColor The integer color of the background
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private int backgroundColor;

    /**
     * -- GETTER --
     * Gets the display's text color
     *
     * @return The display's text color
     *
     * -- SETTER --
     * Sets the display's text color
     * @param textColor The integer color of the text
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private int textColor;

    /**
     * -- GETTER --
     * Gets the display's text alignment
     *
     * @return The display's text alignment
     *
     * -- SETTER --
     * Sets the displays text alignment
     * @param align The new display alignment
     * @return The Display object to allow for method chaining
     * @see DisplayHandler.Align
     * @see DisplayHandler.Align
     */
    @Getter
    @Setter
    private DisplayHandler.Align align;

    /**
     * -- GETTER --
     * Gets the display's line order
     *
     * @return The display's line order
     *
     * -- SETTER --
     * Sets the display's line order
     * @param order The new display line order
     * @return The Display object to allow for method chaining
     * @see DisplayHandler.Order
     * @see DisplayHandler.Order
     */
    @Getter
    @Setter
    private DisplayHandler.Order order;

    /**
     * -- GETTER --
     * Gets the minimum width of the display
     *
     * @return The minimum width of the display
     *
     * -- SETTER --
     * Sets the minimum width of the display
     * @param minWidth The new minimum display width
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private int minWidth;

    public Display() {
        setLines(new ArrayList<>());

        setRenderX(0f);
        setRenderY(0f);

        setShouldRender(true);

        setBackground(DisplayHandler.Background.NONE);
        setBackgroundColor(0x50000000);
        setTextColor(0xffffffff);
        setAlign(DisplayHandler.Align.LEFT);
        setOrder(DisplayHandler.Order.DOWN);

        setMinWidth(0);

        DisplayHandler.getInstance().registerDisplay(this);
    }

    /**
     * Sets a display's background type using string input.
     * <p>
     *     Input can be:<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"full"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"per line"<br>
     * </p>
     *
     * @param background the type of background
     * @return the display to allow for method chaining
     */
    @Tolerate
    public Display setBackground(String background) {
        this.background = DisplayHandler.Background.getBackgroundByName(background);
        return this;
    }

    /**
     * Sets a display's text alignment using string input.
     * <p>
     *     Input can be:<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"right"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"left"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"center"
     * </p>
     *
     * @param align the type of alignment
     * @return the display to allow for method chaining
     */
    @Tolerate
    public Display setAlign(String align) {
        this.align = DisplayHandler.Align.getAlignByName(align);
        return this;
    }

    /**
     * Sets a display's line order using string input.
     * <p>
     *     Input can be:<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"up"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"down"
     * </p>
     *
     * @param order the order of lines
     * @return the display to allow method chaining
     */
    @Tolerate
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
        return setLine(lineNumber, new DisplayLine(line));
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
            if (line.getTextWidth() > maxWidth)
                maxWidth = (int) Math.ceil(line.getTextWidth());
        }

        float i = 0;

        for (DisplayLine line : lines) {
            drawLine(line, this.renderX, this.renderY + (i * 10), maxWidth);

            if (order == DisplayHandler.Order.DOWN)
                i += line.getText().getScale();
            else if (order == DisplayHandler.Order.UP)
                i -= line.getText().getScale();
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
}
