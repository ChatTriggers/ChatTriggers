package com.chattriggers.ctjs.minecraft.libs.renderer;

import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;

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
    private float scale;
    @Getter
    private boolean dropShadow;

    Text(String text, float x, float y) {
        this.string = text;
        this.x = x;
        this.y = y;

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
        Renderer.getFontRenderer().drawString(this.string, this.x / this.scale, this.y / this.scale, this.color, this.dropShadow);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        return this;
    }
}
