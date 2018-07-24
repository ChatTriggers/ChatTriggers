package com.chattriggers.ctjs.minecraft.libs.renderer;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used in {@link Renderer#text(String, float, float)}
 */
@Accessors(chain = true)
public class Text {
    /**
     * -- GETTER --
     * Gets the string of the text object
     *
     * @return String value of text object
     *
     * -- SETTER --
     * Sets the string of the text object
     *
     * @param string New value of string
     * @return The Text object for method chaining
     */
    @Getter @Setter
    private String string;

    /**
     * -- GETTER --
     * Gets a list of lines based on the text width
     *
     * @return a list of lines
     */
    @Getter
    private List<String> lines;

    /**
     * -- GETTER --
     * Gets the text color
     *
     * @return A color integer
     */
    @Getter
    private int color;

    /**
     * -- GETTER --
     * Gets if the text is formatted (true by default)
     *
     * @return True if the text is formatted
     */
    @Getter
    private boolean formatted;

    /**
     * -- GETTER --
     * Gets the text x position
     *
     * @return The text x position
     *
     * -- SETTER --
     * Sets the text x position
     *
     * @param x The new text x position
     * @return The Text object for method chaining
     */
    @Getter @Setter
    private float x;

    /**
     * -- GETTER --
     * Gets the text y position
     *
     * @return The text y position
     *
     * -- SETTER --
     * Sets the text y position
     *
     * @param y The new text y position
     * @return The Text object for method chaining
     */
    @Getter @Setter
    private float y;

    /**
     * -- GETTER --
     * Gets the max width for line wrapping
     *
     * @return Max width for line wrapping
     */
    @Getter
    private int width;

    /**
     * -- GETTER --
     * Gets the max number of lines to draw
     *
     * @return Max number of lines to draw
     *
     * -- SETTER --
     * Sets the max number of lines to draw
     *
     * @param maxLines The value of maxLines
     * @return The Text object for method chaining
     */
    @Getter @Setter
    private int maxLines;

    /**
     * -- GETTER --
     * Gets the text scale
     *
     * @return The text scale
     *
     * -- SETTER --
     * Sets the text scale
     *
     * @param scale The new text scale
     * @return The Text object for method chaining
     */
    @Getter @Setter
    private float scale;

    /**
     * -- GETTER --
     * Gets the drop shadow of the text
     *
     * @return Boolean indicating if drop shadow is enabled
     *
     * -- SETTER --
     * Sets the drop shadow
     *
     * @param shadow True to enable drop shadow
     * @return The Text object for method chaining
     */
    @Getter @Setter
    private boolean shadow;

    /**
     * -- GETTER --
     * Gets the text alignment of the text
     *
     * @return String the alignment
     *
     * -- SETTER --
     * Sets the alignment of the text
     *
     * @param align The alignment
     * @return The Text object for method chaining
     */
    @Getter @Setter
    private String align;

    Text(String text, float x, float y) {
        this.string = text;
        this.lines = new ArrayList<>(Collections.singleton(this.string));
        this.formatted = true;
        updateFormatting();
        this.x = x;
        this.y = y;

        this.width = 0;
        this.maxLines = 0;
        this.color = 0xffffffff;
        this.scale = 1;
        this.shadow = false;
        this.align = "left";
    }

    private void updateFormatting() {
        if (this.formatted)
            this.string = ChatLib.addColor(this.string);
        else
            this.string = ChatLib.replaceFormatting(this.string);
    }

    /**
     * Sets if the text is formatted (true by default)
     *
     * @param formatted if the text is formatted
     * @return the Text object for method chaining
     */
    public Text setFormatted(boolean formatted) {
        this.formatted = formatted;
        updateFormatting();
        
        return this;
    }

    /**
     * Sets the text color
     *
     * @see Renderer#color(int, int, int, int)
     * @param color The new color
     * @return The Text object for method chaining
     */
    public Text setColor(int color) {
        this.color = fixAlpha(color);

        return this;
    }

    /**
     * Sets the max width for line wrapping
     *
     * @param width The new max width
     * @return The Text object for method chaining
     */
    public Text setWidth(int width) {
        this.width = width;
        this.lines = Renderer.getFontRenderer().listFormattedStringToWidth(this.string, this.width);

        return this;
    }

    /**
     * Gets whether or not the line wrapped text exceeds the max lines.
     *
     * @return True if wrapped text exceeds max lines
     */
    public boolean exceedsMaxLines() {
        return this.width != 0
                && this.lines.size() > this.maxLines;

    }

    /**
     * Gets the text height based on width.
     *
     * @return The text height
     */
    public int getHeight() {
        return this.width == 0
                ? 9
                :  this.lines.size() * 9;
    }

    public int getMaxWidth() {
        if (this.width == 0) {
            return Renderer.getStringWidth(this.string);
        } else {
            int maxWidth = 0;

            for (String line : this.lines) {
                if (Renderer.getStringWidth(line) > maxWidth)
                    maxWidth = Renderer.getStringWidth(line);
            }

            return maxWidth;
        }
    }

    /**
     * Draws the text onto the client's overlay.
     *
     * @return The text to allow for method chaining
     */
    public Text draw() {
        GlStateManager.enableBlend();
        GlStateManager.scale(this.scale, this.scale, this.scale);
        if (this.width > 0) {
            float maxLinesHolder = this.maxLines;
            float yHolder = this.y;
            for (String line : this.lines) {
                Renderer.getFontRenderer().drawString(line, getXAlign(line), yHolder / this.scale, this.color, this.shadow);
                yHolder += 9;
                maxLinesHolder--;
                if (maxLinesHolder == 0)
                    break;
            }
        } else {
            Renderer.getFontRenderer().drawString(this.string, getXAlign(this.string), this.y / this.scale, this.color, this.shadow);
        }
        GlStateManager.disableBlend();

        Renderer.finishDraw();

        return this;
    }

    // helper method to fix alpha
    private int fixAlpha(int color) {
        int alpha = color >> 24 & 255;
        if (alpha < 10)
            return Renderer.color(color >> 16 & 255, color >> 8 & 255, color & 255, 10);
        return color;
    }

    // helper method to get the x alignment
    private float getXAlign(String string) {
        float x = this.x / this.scale;
        switch (this.align.toLowerCase()) {
            case("center"):
                return x - Renderer.getStringWidth(string) / 2;
            case("right"):
                return x - Renderer.getStringWidth(string);
            case("left"):
            default:
                return x;
        }
    }
}
