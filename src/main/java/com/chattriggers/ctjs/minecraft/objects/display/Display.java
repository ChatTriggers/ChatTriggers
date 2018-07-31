package com.chattriggers.ctjs.minecraft.objects.display;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

import java.util.ArrayList;

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
     * Gets the x position of the Display
     *
     * @return The x position of the Display
     *
     * -- SETTER --
     * Sets the x position of the Display
     * @param renderX The new x location
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private float renderX;

    /**
     * -- GETTER --
     * Gets the y position of the Display
     *
     * @return The y position of the Display
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
     * @return True if the Display is set to render
     *
     * -- SETTER --
     * Sets whether or not the Display should render
     * @param shouldRender True if the display should render
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private boolean shouldRender;

    /**
     * -- GETTER --
     * Gets a Display's background type
     *
     * @return The Display's background
     *
     * -- SETTER --
     * Sets a Display's background type.
     * @param background The new Display background
     * @return The Display object to allow for method chaining
     * @see DisplayHandler.Background
     * @see DisplayHandler.Background
     */
    @Getter
    @Setter
    private DisplayHandler.Background background;

    /**
     * -- GETTER --
     * Gets the Display's background color
     *
     * @return The Display's background color
     *
     * -- SETTER --
     * Sets the Display's background color
     * @param backgroundColor The integer color of the background
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private int backgroundColor;

    /**
     * -- GETTER --
     * Gets the Display's text color
     *
     * @return The Display's text color
     *
     * -- SETTER --
     * Sets the Display's text color
     * @param textColor The integer color of the text
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private int textColor;

    /**
     * -- GETTER --
     * Gets the Display's text alignment
     *
     * @return The Display's text alignment
     *
     * -- SETTER --
     * Sets the Displays text alignment
     * @param align The new Display alignment
     * @return The Display object to allow for method chaining
     * @see DisplayHandler.Align
     * @see DisplayHandler.Align
     */
    @Getter
    @Setter
    private DisplayHandler.Align align;

    /**
     * -- GETTER --
     * Gets the Display's line order
     *
     * @return The Display's line order
     *
     * -- SETTER --
     * Sets the display's line order
     * @param order The new Display line order
     * @return The Display object to allow for method chaining
     * @see DisplayHandler.Order
     * @see DisplayHandler.Order
     */
    @Getter
    @Setter
    private DisplayHandler.Order order;

    /**
     * -- GETTER --
     * Gets the minimum width of the Display
     *
     * @return The minimum width of the Display
     *
     * -- SETTER --
     * Sets the minimum width of the Display
     * @param minWidth The new minimum Display width
     * @return The Display object to allow for method chaining
     */
    @Getter
    @Setter
    private int minWidth;

    /**
     * -- GETTER --
     * Gets the width of the Display
     *
     * @return The width of the Display
     */
    @Getter
    private float width;

    /**
     * -- GETTER --
     *
     * @return the height of the Display
     */
    @Getter
    private float height;

    public Display() {
        this.lines = new ArrayList<>();

        this.shouldRender = true;
        this.renderX = 0f;
        this.renderY = 0f;

        this.background = DisplayHandler.Background.NONE;
        this.backgroundColor = 0x50000000;
        this.textColor = 0xffffffff;
        this.align = DisplayHandler.Align.LEFT;
        this.order = DisplayHandler.Order.DOWN;

        this.minWidth = 0;

        DisplayHandler.getInstance().registerDisplay(this);
    }

    public Display(ScriptObjectMirror config) {
        this.lines = new ArrayList<>();

        this.shouldRender = (boolean) config.getOrDefault("shouldRender", true);
        this.renderX = (int) config.getOrDefault("renderX", 0);
        this.renderY = (int) config.getOrDefault("renderY", 0);

        this.background = (DisplayHandler.Background) config.getOrDefault("background", DisplayHandler.Background.NONE);
        this.backgroundColor = (int) config.getOrDefault("backgroundColor", 0x50000000);
        this.textColor = (int) config.getOrDefault("textColor", 0xffffffff);
        this.align = (DisplayHandler.Align) config.getOrDefault("align", DisplayHandler.Align.LEFT);
        this.order = (DisplayHandler.Order) config.getOrDefault("order", DisplayHandler.Order.DOWN);

        this.minWidth = (int) config.getOrDefault("minWidth", 0);

        DisplayHandler.getInstance().registerDisplay(this);
    }

    /**
     * Sets a Display's background type using string input.
     * <p>
     *     Input can be:<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"full"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"per line"<br>
     * </p>
     *
     * @param background the type of background
     * @return the Display to allow for method chaining
     */
    @Tolerate
    public Display setBackground(String background) {
        this.background = DisplayHandler.Background.getBackgroundByName(background);
        return this;
    }

    /**
     * Sets a Display's text alignment using string input.
     * <p>
     *     Input can be:<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"right"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"left"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"center"
     * </p>
     *
     * @param align the type of alignment
     * @return the Display to allow for method chaining
     */
    @Tolerate
    public Display setAlign(String align) {
        this.align = DisplayHandler.Align.getAlignByName(align);
        return this;
    }

    /**
     * Sets a Display's line order using string input.
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
     * Sets a Display line to a string.
     *
     * @param lineNumber the line number to set (0 based)
     * @param line       the string to set the line to
     * @return the Display to allow for method chaining
     */
    public Display setLine(int lineNumber, String line) {
        return setLine(lineNumber, new DisplayLine(line));
    }

    /**
     * Sets a Display line to a DisplayLine
     *
     * @param lineNumber the line number to set (0 based)
     * @param line       the DisplayLine to set the line to
     * @return the Display to allow for method chaining
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
     * @return the DisplayLine in the line of Display
     */
    public DisplayLine getLine(int lineNumber) {
        try {
            return lines.get(lineNumber);
        } catch (Exception exception) {
            return new DisplayLine("");
        }
    }

    /**
     * Adds multiple lines to the end of a Display.<br>
     * Arguments can be either a String or a DisplayLine.
     *     
     * @param lines the lines to add
     * @return the Display to allow for method chaining
     */
    public Display addLines(Object... lines) {
        for (Object line : lines) {
            if (line instanceof String) {
                this.lines.add(new DisplayLine((String) line));
            } else if (line instanceof DisplayLine) {
                this.lines.add((DisplayLine) line);
            }
        }
        
        return this;
    }

    /**
     * Adds one line to a Display.<br>
     * Arguments can be either a String or a DisplayLine.
     * 
     * @param line the line to add
     * @return the Display to allow for method chaining
     */
    public Display addLine(Object line) {
        return addLines(line);
    }

    /**
     * Clears all the lines in the Display.
     *
     * @return the Display to allow for method chaining
     */
    public Display clearLines() {
        lines.clear();
        return this;
    }

    /**
     * Set the X and Y render position of the Display.
     *
     * @param renderX the x coordinate
     * @param renderY the y coordinate
     * @return the Display to allow for method chaining
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

        this.width = maxWidth;

        float i = 0;

        for (DisplayLine line : lines) {
            drawLine(line, this.renderX, this.renderY + (i * 10), maxWidth);

            if (order == DisplayHandler.Order.DOWN)
                i += line.getText().getScale();
            else if (order == DisplayHandler.Order.UP)
                i -= line.getText().getScale();
        }

        this.height = Math.abs(i);
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
