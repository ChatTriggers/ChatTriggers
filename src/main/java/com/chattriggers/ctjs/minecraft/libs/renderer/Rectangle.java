package com.chattriggers.ctjs.minecraft.libs.renderer;

import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Rectangle {
    @Getter
    private int color;
    @Getter
    private float x;
    @Getter
    private float y;
    @Getter
    private float width;
    @Getter
    private float height;

    @Getter
    private boolean dropShadow;
    @Getter
    private int shadowColor;
    @Getter
    private float offsetX;
    @Getter
    private float offsetY;

    @Getter
    private boolean outline;
    @Getter
    private int outlineColor;
    @Getter
    private float thickness;

    Rectangle(int color, float x, float y, float width, float height) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.dropShadow = false;
        this.outline = false;
    }

    /**
     * Sets the rectangle {@link Renderer#color(int, int, int, int)}.
     *
     * @param color the {@link Renderer#color(int, int, int, int)}
     * @return the rectangle to allow for method chaining
     */
    public Rectangle setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the rectangle x position.
     *
     * @param x the x position
     * @return the rectangle to allow for method chaining
     */
    public Rectangle setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the rectangle y position.
     *
     * @param y the y position
     * @return the rectangle to allow for method chaining
     */
    public Rectangle setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * Sets the rectangle width.
     *
     * @param width the width
     * @return the rectangle to allow for method chaining
     */
    public Rectangle setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     * Sets the rectangle height.
     *
     * @param height the height
     * @return the rectangle to allow for method chaining
     */
    public Rectangle setHeight(float height) {
        this.height = height;
        return this;
    }

    /**
     * Sets the drop shadow of the rectangle.
     *
     * @param color   the {@link Renderer#color(int, int, int, int)} of the drop shadow
     * @param offsetX the x offset of the drop shadow
     * @param offsetY the y offset of the drop shadow
     * @return the rectangle to allow for method chaining
     */
    public Rectangle setShadow(int color, float offsetX, float offsetY) {
        this.dropShadow = true;

        this.shadowColor = color;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

        return this;
    }

    /**
     * Sets the outline of the rectangle.
     *
     * @param color     the {@link Renderer#color(int, int, int, int)} of the outline
     * @param thickness the thickness of the outline
     * @return the rectangle to allow for method chaining
     */
    public Rectangle setOutline(int color, float thickness) {
        this.outline = true;

        this.outlineColor = color;
        this.thickness = thickness;

        return this;
    }

    /**
     * Draws the rectangle onto the client's overlay.
     *
     * @return the rectangle to allow for method chaining
     */
    public Rectangle draw() {
        dropShadow();
        outline();
        drawRect(this.color, this.x, this.y, this.width, this.height);

        return this;
    }

    // helper method to draw the outline
    private void outline() {
        if (!outline) return;

        drawRect(this.outlineColor, this.x - this.thickness, this.y - this.thickness, this.width + this.thickness * 2, this.height + this.thickness * 2);
    }

    // helper method to draw the drop shadow
    private void dropShadow() {
        if (!dropShadow) return;

        drawRect(this.shadowColor, this.x + this.offsetX, this.y + this.height, this.width, this.offsetY);
        drawRect(this.shadowColor, this.x + this.width, this.y + this.offsetY, this.offsetX, this.height - this.offsetY);
    }

    // helper method to draw a rectangle
    private void drawRect(int color, float x, float y, float width, float height) {
        float x2 = x + width;
        float y2 = y + height;

        if (x > x2) {
            float k = x;
            x = x2;
            x2 = k;
        }
        if (y > y2) {
            float k = y;
            y = y2;
            y2 = k;
        }

        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y2, 0.0D).endVertex();
        worldrenderer.pos(x2, y2, 0.0D).endVertex();
        worldrenderer.pos(x2, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.color(1, 1, 1, 1);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
