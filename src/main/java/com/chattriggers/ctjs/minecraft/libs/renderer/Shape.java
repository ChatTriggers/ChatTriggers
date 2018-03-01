package com.chattriggers.ctjs.minecraft.libs.renderer;

import lombok.Getter;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import javax.vecmath.Vector2d;
import java.util.ArrayList;

public class Shape {
    @Getter
    ArrayList<Vector2d> vertexes;
    @Getter
    int color;
    @Getter
    int drawMode;

    Shape(int color) {
        this.color = color;

        this.vertexes = new ArrayList<>();
        this.drawMode = 9;
    }

    /**
     * Sets the GL draw mode for the shape.<br>
     * 0 = points<br>
     * 1 = lines<br>
     * 2 = line loop<br>
     * 3 = line strip<br>
     * 5 = triangles<br>
     * 5 = triangle strip<br>
     * 6 = triangle fan<br>
     * 7 = quads<br>
     * 8 = quad strip<br>
     * 9 = polygon
     *
     * @param mode the draw mode
     * @return the shape to allow for method chaining
     */
    public Shape setDrawMode(int mode) {
        this.drawMode = mode;
        return this;
    }

    /**
     * Sets the shape color.
     *
     * @param color {@link Renderer#color(int, int, int, int)}
     * @return the shape to allow for method chaining
     */
    public Shape setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * Adds a vertex to the shape.
     *
     * @param x the x position
     * @param y the y position
     * @return the shape to allow for method chaining
     */
    public Shape addVertex(float x, float y) {
        this.vertexes.add(new Vector2d(x, y));
        return this;
    }

    /**
     * Inserts a vertex into the shape
     *
     * @param i the index of the insertion
     * @param x the x position
     * @param y the y position
     * @return the shape to allow for method chaining
     */
    public Shape insertVertex(int i, float x, float y) {
        this.vertexes.add(i, new Vector2d(x, y));
        return this;
    }

    /**
     * Removes a vertex from the shape
     *
     * @param i the index to remove
     * @return the shape to allow for method chaining
     */
    public Shape removeVertex(int i) {
        this.vertexes.remove(i);
        return this;
    }

    /**
     * Sets the shape as a line pointing from x1 y1 to x2 y2
     *
     * @param x1        the x starting position
     * @param y1        the y starting position
     * @param x2        the x ending position
     * @param y2        the y ending position
     * @param thickness the thickness
     * @return the shape to allow for method chaining
     */
    public Shape setLine(float x1, float y1, float x2, float y2, float thickness) {
        this.vertexes.clear();

        double theta = -Math.atan2(y2 - y1, x2 - x1);
        double i = Math.sin(theta) * (thickness / 2);
        double j = Math.cos(theta) * (thickness / 2);

        double ax = x1 + i;
        double ay = y1 + j;
        double dx = x1 - i;
        double dy = y1 - j;

        double bx = x2 + i;
        double by = y2 + j;
        double cx = x2 - i;
        double cy = y2 - j;

        this.vertexes.add(new Vector2d(ax, ay));
        this.vertexes.add(new Vector2d(bx, by));
        this.vertexes.add(new Vector2d(cx, cy));
        this.vertexes.add(new Vector2d(dx, dy));

        this.drawMode = 9;

        return this;
    }

    /**
     * Sets the shape as a circle.
     *
     * @param x      the x position
     * @param y      the y position
     * @param radius the radius
     * @param steps  the number of steps to take to complete the circle
     * @return the shape to allow for method chaining
     */
    public Shape setCircle(float x, float y, float radius, int steps) {
        this.vertexes.clear();

        double theta = 2 * Math.PI / steps;
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);

        double xHolder;
        double circleX = 1;
        double circleY = 0;

        for (int i = 0; i < steps; i++) {
            this.vertexes.add(new Vector2d(x, y));
            this.vertexes.add(new Vector2d(circleX * radius + x, circleY * radius + y));
            xHolder = circleX;
            circleX = cos * circleX - sin * circleY;
            circleY = sin * xHolder + cos * circleY;
            this.vertexes.add(new Vector2d(circleX * radius + x, circleY * radius + y));
        }

        this.drawMode = 5;

        return this;
    }

    /**
     * Draws the shape onto the client's overlay.
     *
     * @return the shape to allow for method chaining
     */
    public Shape draw() {
        float a = (float) (this.color >> 24 & 255) / 255.0F;
        float r = (float) (this.color >> 16 & 255) / 255.0F;
        float g = (float) (this.color >> 8 & 255) / 255.0F;
        float b = (float) (this.color & 255) / 255.0F;

        GlStateManager.pushMatrix();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);

        worldrenderer.begin(this.drawMode, DefaultVertexFormats.POSITION);

        for (Vector2d vertex : this.vertexes)
            worldrenderer.pos(vertex.getX(), vertex.getY(), 0.0D).endVertex();

        tessellator.draw();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();

        return this;
    }
}
