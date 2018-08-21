package com.chattriggers.ctjs.minecraft.objects.display;

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer;
import com.chattriggers.ctjs.minecraft.libs.renderer.Text;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.triggers.OnRegularTrigger;
import com.chattriggers.ctjs.triggers.OnTrigger;
import com.chattriggers.ctjs.triggers.TriggerType;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
import org.lwjgl.input.Mouse;

import java.util.HashMap;

@Accessors(chain = true)
public class DisplayLine {
    /**
     * -- GETTER --
     * Gets the display line text
     *
     * @return The display line text
     */
    private Text text;
    public Text getText() {return this.text;}

    private int textWidth;
    public int getTextWidth() {return this.textWidth;}

    /**
     * -- GETTER --
     * Gets the line text color
     *
     * @return The line text color
     *
     * -- SETTER --
     * Sets the text color of the line
     *
     * @see Renderer#color(int, int, int, int)
     * @param textColor The integer color
     * @return The DisplayLine object to allow for method chaining
     */
    @Getter @Setter
    private Integer textColor;

    /**
     * -- GETTER --
     * Gets the line alignment
     *
     * @see DisplayHandler.Align
     * @return The display line alignment
     *
     * -- SETTER --
     * Sets the alignment of the line (based on max width of display)
     *
     * @see DisplayHandler.Align
     * @param align The new alignment
     * @return The DisplayLine object to allow for method chaining
     */
    @Getter @Setter
    private DisplayHandler.Align align;

    /**
     * -- GETTER --
     * Gets the line background
     *
     * @see DisplayHandler.Background
     * @return The line background
     *
     * -- SETTER --
     * Sets the line background (NONE by default). If set to NONE,
     * the line will inherit the background from the display.
     *
     * @see DisplayHandler.Background
     * @param background The background type
     * @return The DisplayLine to allow for method chaining
     */
    @Getter @Setter
    private DisplayHandler.Background background;

    /**
     * -- GETTER --
     * Gets the line background color
     *
     * @see DisplayLine#setBackgroundColor(Integer)
     * @return The line background color
     *
     * -- SETTER --
     * Sets the line background color. If not set, the line
     * will inherit the background color from the display.
     *
     * @see Renderer#color(int, int, int, int)
     * @param backgroundColor The background color integer
     * @return The DisplayLine object to allow for method chaining
     */
    @Getter @Setter
    private Integer backgroundColor;

    private OnTrigger onClicked;
    private OnTrigger onHovered;
    private OnTrigger onDragged;
    private HashMap<Integer, Boolean> mouseState;
    private HashMap<Integer, Float[]> draggedState;

    /**
     * Creates a new DisplayLine to be used in a {@link Display}
     *
     * @param text the text in the DisplayLine
     */
    public DisplayLine(String text) {
        setText(text);

        this.textColor = null;
        this.align = DisplayHandler.Align.NONE;
        this.background = DisplayHandler.Background.NONE;
        this.backgroundColor = null;

        this.onClicked = null;
        this.mouseState = new HashMap<>();

        for (int i = 0; i < 5; i++)
            this.mouseState.put(i, false);
        this.draggedState = new HashMap<>();
    }

    /**
     * Creates a new DisplayLine to be used in a {@link Display}
     *
     * @param text the text in the DisplayLine
     * @param config the JavaScript config object
     */
    public DisplayLine(String text, ScriptObjectMirror config) {
        setText(text);

        Object textColor = config.getOrDefault("textColor", null);
        this.textColor = (textColor == null) ? null : (int) textColor;

        this.align = (DisplayHandler.Align) config.getOrDefault("align", DisplayHandler.Align.NONE);
        this.background = (DisplayHandler.Background) config.getOrDefault("background", DisplayHandler.Background.NONE);

        Object backgroundColor = config.getOrDefault("backgroundColor", null);
        this.backgroundColor = (backgroundColor == null) ? null : (int) backgroundColor;

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
        this.text = Renderer.text(text, 0, 0);
        this.textWidth = (int) Math.ceil(Renderer.getStringWidth(this.text.getString()) * this.text.getScale());
        return this;
    }

    /**
     * Sets the line's shadow.
     *
     * @param shadow if there is text shadow
     * @return the DisplayLine to allow for method chaining
     */
    public DisplayLine setShadow(boolean shadow) {
        this.text.setShadow(shadow);
        return this;
    }

    /**
     * Sets the scale of the text (1 by default).
     *
     * @param scale the scale of the text
     * @return the DisplayLine to allow for method chaining
     */
    public DisplayLine setScale(float scale) {
        text.setScale(scale);
        this.textWidth = (int) Math.ceil(Renderer.getStringWidth(text.getString()) * scale);
        return this;
    }

    /**
     * Sets the alignment of the line (based on max width of display)
     * <p>
     *     Input can be:<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"right"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"left"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"center"
     * </p>
     *
     * @see DisplayHandler.Align
     * @param align The new alignment
     * @return The DisplayLine object to allow for method chaining
     */
    @Tolerate
    public DisplayLine setAlign(String align) {
        this.align = DisplayHandler.Align.valueOf(align.toUpperCase());
        return this;
    }

    /**
     * Sets the lines background (NONE by default).
     * If set to NONE, the line will inherit the background from the display.
     * <p>
     *     Input can be:<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"full"<br>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"per line"<br>
     * </p>
     *
     * @see DisplayHandler.Background
     * @param background the background type
     * @return the DisplayLine to allow for method chaining
     */
    @Tolerate
    public DisplayLine setBackground(String background) {
        this.background = DisplayHandler.Background.valueOf(background.toUpperCase().replace(" ", "_"));
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
     * @param method the method to run
     * @return the DisplayLine to allow for method chaining
     */
    public DisplayLine registerClicked(Object method) {
        onClicked = new OnRegularTrigger(method, TriggerType.OTHER);
        return this;
    }

    /**
     * Registers a method to be run when the line is hovered over.<br>
     * Arguments passed through to method:<br>
     * int mouseX<br>
     * int mouseY
     *
     * @param method the method to run
     * @return the DisplayLine to allow for method chaining
     */
    public DisplayLine registerHovered(Object method) {
        onHovered = new OnRegularTrigger(method, TriggerType.OTHER);
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
     * @param method the method to run
     * @return the DisplayLine to allow for method chaining
     */
    public DisplayLine registerDragged(Object method) {
        onDragged = new OnRegularTrigger(method, TriggerType.OTHER);
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
            Renderer.drawRect(color, x, y, width, height);
    }

    private void drawPerLineBG(DisplayHandler.Background bg, int color, float x, float y, float width, float height) {
        if (bg == DisplayHandler.Background.PER_LINE)
            Renderer.drawRect(color, x, y, width, height);
    }

    public void drawLeft(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor, int textColor) {
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
        drawFullBG(bg, bgColor, x - 1, y - 1, maxWidth + 2, 10 * this.text.getScale());

        // blank line
        if ("".equals(this.text.getString())) return;

        // text and per line background
        float xOff = x;

        if (this.align == DisplayHandler.Align.RIGHT) {
            xOff = x - this.textWidth + maxWidth;
        } else if (this.align == DisplayHandler.Align.CENTER) {
            xOff = x - this.textWidth / 2 + maxWidth / 2;
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, this.textWidth + 2, 10 * this.text.getScale());
        this.text.setX(xOff).setY(y).setColor(textCol).draw();

        handleInput(xOff - 1, y - 1, this.textWidth + 2, 10 * this.text.getScale());
    }

    public void drawRight(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor, int textColor) {
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
        drawFullBG(bg, bgColor, x - maxWidth - 1, y - 1, maxWidth + 2, 10 * this.text.getScale());

        // blank line
        if ("".equals(this.text.getString())) return;

        // text and per line background\
        float xOff = x - this.textWidth;

        if (this.align == DisplayHandler.Align.LEFT) {
            xOff = x - maxWidth;
        } else if (this.align == DisplayHandler.Align.CENTER) {
            xOff = x - this.textWidth / 2 - maxWidth / 2;
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, this.textWidth + 2, 10 * this.text.getScale());
        this.text.setX(xOff).setY(y).setColor(textCol).draw();

        handleInput(xOff - 1, y - 1, this.textWidth + 2, 10 * this.text.getScale());
    }

    public void drawCenter(float x, float y, float maxWidth, DisplayHandler.Background background, int backgroundColor, int textColor) {
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
        drawFullBG(bg, bgColor, x - maxWidth / 2 - 1, y - 1, maxWidth + 2, 10 * this.text.getScale());

        // blank line
        if ("".equals(this.text.getString())) return;

        // text and per line background
        float xOff = x - this.textWidth / 2;

        if (this.align == DisplayHandler.Align.LEFT) {
            xOff = x - maxWidth / 2;
        } else if (this.align == DisplayHandler.Align.RIGHT) {
            xOff = x + maxWidth / 2 - this.textWidth;
        }

        drawPerLineBG(bg, bgColor, xOff - 1, y - 1, this.textWidth + 2, 10 * this.text.getScale());
        this.text.setX(xOff).setY(y).setColor(textCol).draw();

        handleInput(xOff - 1, y - 1, this.textWidth + 2, 10 * this.text.getScale());
    }
}