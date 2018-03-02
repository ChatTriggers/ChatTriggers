package com.chattriggers.ctjs.minecraft.libs.renderer;

import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Used in {@link Renderer#text(String, float, float)}
 */
public class Text {
    @Getter
    private String string;
    @Getter
    private int color;
    @Getter
    private float x;
    @Getter
    private float y;
    @Getter
    private int width;
    @Getter
    private int maxLines;
    @Getter
    private float scale;
    @Getter
    private boolean dropShadow;

    Text(String text, float x, float y) {
        this.string = text;
        this.x = x;
        this.y = y;

        this.width = 0;
        this.maxLines = 0;
        this.color = 0xffffffff;
        this.scale = 1;
        this.dropShadow = false;
    }

    /**
     * Sets the text x position.
     *
     * @param x the x position
     * @return the text to allow for method chaining
     */
    public Text setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the text y position.
     *
     * @param y the y position
     * @return the text to allow for method chaining
     */
    public Text setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * Sets the text max width for line wrapping.
     * @param width the max width
     * @return the text to allow for method chaining
     */
    public Text setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the max number of lines to draw.
     * @param lines the max number of lines
     * @return the text to allow for method chaining
     */
    public Text setMaxLines(int lines) {
        this.maxLines = lines;
        return this;
    }

    /**
     * Gets whether or not the line wrapped text exceeds the max lines.
     * @return true if wrapped text exceeds max lines
     */
    public boolean exceedsMaxLines() {
        return this.width != 0
                && Renderer.getFontRenderer().listFormattedStringToWidth(this.string, this.width).size() > this.maxLines;

    }

    /**
     * Gets the text height based on width.
     * @return the text height
     */
    public int getHeight() {
        return this.width == 0
                ? 9
                :  Renderer.getFontRenderer().listFormattedStringToWidth(this.string, this.width).size() * 9;
    }

    /**
     * Sets the text color.
     *
     * @param color the {@link Renderer#color(int, int, int, int)}
     * @return the text to allow for method chaining
     */
    public Text setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the text scale.
     *
     * @param scale the scale of the text
     * @return the text to allow for method chaining
     */
    public Text setScale(float scale) {
        this.scale = scale;
        return this;
    }

    /**
     * Sets the drop shadow of the text.
     *
     * @param dropShadow if the text has a drop shadow
     * @return the text to allow for method chaining
     */
    public Text setShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
        return this;
    }

    /**
     * Sets the string of the text
     *
     * @param string the string of the text
     * @return the text to allow for method chaining
     */
    public Text setString(String string) {
        this.string = string;
        return this;
    }

    /**
     * Draws the text onto the client's overlay.
     *
     * @return the text to allow for method chaining
     */
    public Text draw() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.scale(this.scale, this.scale, this.scale);
        if (this.width > 0) {
            float maxLinesHolder = this.maxLines;
            float yHolder = this.y;
            for (String line : Renderer.getFontRenderer().listFormattedStringToWidth(this.string, this.width)) {
                Renderer.getFontRenderer().drawString(line, this.x / this.scale, yHolder / this.scale, this.color, this.dropShadow);
                yHolder += 9;
                maxLinesHolder--;
                if (maxLinesHolder == 0)
                    break;
            }
        } else {
            Renderer.getFontRenderer().drawString(this.string, this.x / this.scale, this.y / this.scale, this.color, this.dropShadow);
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        return this;
    }
}
