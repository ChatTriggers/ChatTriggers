//package com.chattriggers.ctjs.minecraft.libs.renderer;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//
////#if MC<=10809
//import net.minecraft.client.renderer.WorldRenderer;
////#else
////$$ import net.minecraft.client.renderer.BufferBuilder;
////#endif
//
///**
// * Used in {@link Renderer#rectangle(int, float, float, float, float)}
// */
//@Accessors(chain = true)
//public class Rectangle {
//    /**
//     * -- GETTER --
//     * Gets the rectangle {@link Renderer#color(int, int, int, int)}
//     *
//     * @return The rectangle {@link Renderer#color(int, int, int, int)}
//     *
//     * -- SETTER --
//     * Sets the rectangle {@link Renderer#color(int, int, int, int)}
//     *
//     * @param color The new {@link Renderer#color(int, int, int, int)}
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private int color;
//
//    /**
//     * -- GETTER --
//     * Gets the rectangle x position
//     *
//     * @return The rectangle x position
//     *
//     * -- SETTER --
//     * Sets the rectangle x position
//     *
//     * @param x The new rectangle x position
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private float x;
//
//    /**
//     * -- GETTER --
//     * Gets the rectangle y position
//     *
//     * @return The rectangle y position
//     *
//     * -- SETTER --
//     * Sets the rectangle y position
//     *
//     * @param y The new rectangle y position
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private float y;
//
//    /**
//     * -- GETTER --
//     * Gets the rectangle width
//     *
//     * @return The rectangle width
//     *
//     * -- SETTER --
//     * Sets the rectangle width
//     *
//     * @param width The new rectangle width
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private float width;
//
//    /**
//     * -- GETTER --
//     * Gets the rectangle height
//     *
//     * @return The rectangle height
//     *
//     * -- SETTER --
//     * Sets the rectangle height
//     *
//     * @param height The new rectangle height
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private float height;
//
//    /**
//     * -- GETTER --
//     * Gets the drop shadow of the rectangle (boolean)
//     *
//     * @return The drop shadow of the rectangle (boolean)
//     *
//     * -- SETTER --
//     * Sets the drop shadow of the rectangle
//     *
//     * @param shadow True to enable drop shadow
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private boolean shadow;
//
//    /**
//     * -- GETTER --
//     * Gets the shadow color of the rectangle
//     *
//     * @return The shadow color of the rectangle
//     *
//     * -- SETTER --
//     * Sets the shadow color of the rectangle
//     *
//     * @param shadowColor The new shadow color of the rectangle
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private int shadowColor;
//
//    /**
//     * -- GETTER --
//     * Gets the x-offset of the rectangle drop shadow
//     *
//     * @return The x-offset of the rectangle drop shadow
//     *
//     * -- SETTER --
//     * Sets the x-offset of the rectangle drop shadow
//     *
//     * @param shadowOffsetX The new drop shadow x-offset
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private float shadowOffsetX;
//
//    /**
//     * -- GETTER --
//     * Gets the y-offset of the rectangle drop shadow
//     *
//     * @return The y-offset of the rectangle drop shadow
//     *
//     * -- SETTER --
//     * Sets the y-offset of the rectangle drop shadow
//     *
//     * @param shadowOffsetY The new drop shadow y-offset
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private float shadowOffsetY;
//
//    /**
//     * -- GETTER --
//     * Gets the outline of the rectangle (boolean)
//     *
//     * @return True if the rectangle outline is enabled
//     * @see Rectangle#setOutline(int, float)
//     */
//    @Getter
//    private boolean outline;
//
//    /**
//     * -- GETTER --
//     * Gets the rectangle outline color
//     *
//     * @return The rectangle outline color
//     *
//     * -- SETTER --
//     * Sets the rectangle outline color
//     *
//     *
//     * @param outlineColor The new rectangle outline color
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private int outlineColor;
//
//    /**
//     * -- GETTER --
//     * Gets the rectangle outline thickness
//     *
//     * @return The rectangle outline thickness
//     *
//     * -- SETTER --
//     * Sets the rectangle outline thickness
//     *
//     * @param thickness The new rectangle outline thickness
//     * @return The Rectangle object to allow for method chaining
//     */
//    @Getter @Setter
//    private float thickness;
//
//    Rectangle(int color, float x, float y, float width, float height) {
//        this.color = color;
//        this.x = x;
//        this.y = y;
//        this.width = width;
//        this.height = height;
//
//        this.shadow = false;
//        this.outline = false;
//    }
//
//    /**
//     * Sets the drop shadow of the rectangle.
//     *
//     * @param color   The {@link Renderer#color(int, int, int, int)} of the drop shadow
//     * @param offsetX The x offset of the drop shadow
//     * @param offsetY The y offset of the drop shadow
//     * @return The rectangle to allow for method chaining
//     */
//    public Rectangle setShadow(int color, float offsetX, float offsetY) {
//        setShadow(true);
//
//        setShadowColor(color);
//        setShadowOffsetX(offsetX);
//        setShadowOffsetY(offsetY);
//
//        return this;
//    }
//
//    /**
//     * Sets the outline of the rectangle.
//     *
//     * @param color     The {@link Renderer#color(int, int, int, int)} of the outline
//     * @param thickness The thickness of the outline
//     * @return The rectangle to allow for method chaining
//     */
//    public Rectangle setOutline(int color, float thickness) {
//        this.outline = true;
//
//        setOutlineColor(color);
//        setThickness(thickness);
//
//        return this;
//    }
//
//    /**
//     * Draws the rectangle onto the client's overlay.
//     *
//     * @return The rectangle to allow for method chaining
//     */
//    public Rectangle draw() {
//        dropShadow();
//        outline();
//        drawRect(this.color, this.x, this.y, this.width, this.height);
//
//        Renderer.finishDraw();
//
//        return this;
//    }
//
//    // helper method to draw the outline
//    private void outline() {
//        if (!outline) return;
//
//        drawRect(this.outlineColor, this.x - this.thickness, this.y - this.thickness, this.width + this.thickness * 2, this.height + this.thickness * 2);
//    }
//
//    // helper method to draw the drop shadow
//    private void dropShadow() {
//        if (!shadow) return;
//
//        drawRect(this.shadowColor, this.x + this.shadowOffsetX, this.y + this.height, this.width, this.shadowOffsetY);
//        drawRect(this.shadowColor, this.x + this.width, this.y + this.shadowOffsetY, this.shadowOffsetX, this.height - this.shadowOffsetY);
//    }
//
//    // helper method to draw a rectangle without pushing and popping gl matrix
//    private void drawRect(int color, float x, float y, float width, float height) {
//        float x2 = x + width;
//    float y2 = y + height;
//
//        if (x > x2) {
//        float k = x;
//        x = x2;
//        x2 = k;
//    }
//        if (y > y2) {
//        float k = y;
//        y = y2;
//        y2 = k;
//    }
//
//    float a = (float) (color >> 24 & 255) / 255.0F;
//    float r = (float) (color >> 16 & 255) / 255.0F;
//    float g = (float) (color >> 8 & 255) / 255.0F;
//    float b = (float) (color & 255) / 255.0F;
//
//        GlStateManager.enableBlend();
//        GlStateManager.disableTexture2D();
//
//    Tessellator tessellator = Tessellator.getInstance();
//    //#if MC<=10809
//    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
//    //#else
//    //$$ BufferBuilder worldrenderer = tessellator.getBuffer();
//    //#endif
//
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//        if (!Renderer.INSTANCE.getColorized())
//                GlStateManager.color(r, g, b, a);
//        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
//        worldrenderer.pos(x, y2, 0.0D).endVertex();
//        worldrenderer.pos(x2, y2, 0.0D).endVertex();
//        worldrenderer.pos(x2, y, 0.0D).endVertex();
//        worldrenderer.pos(x, y, 0.0D).endVertex();
//        tessellator.draw();
//        GlStateManager.color(1, 1, 1, 1);
//
//        GlStateManager.enableTexture2D();
//        GlStateManager.disableBlend();
//}
//}
