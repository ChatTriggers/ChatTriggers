package com.chattriggers.ctjs.minecraft.libs.renderer;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

//#if MC<=10809
import net.minecraft.client.renderer.WorldRenderer;
//#else
//$$ import net.minecraft.client.renderer.BufferBuilder;
//#endif

import javax.vecmath.Vector2d;
import java.util.ArrayList;

/**
 * Used in {@link Renderer#shape(int)}
 */
@Accessors(chain = true)
public class Shape {
    /**
     * -- GETTER --
     * Gets an array list of vector vertices
     *
     * @return An ArrayList of Java Vector2d objects
     */
    @Getter
    ArrayList<Vector2d> vertexes;

    /**
     * -- GETTER --
     * Gets the shape color
     *
     * @return The shape color
     *
     * -- SETTER --
     * Sets the shape color
     *
     * @param color The new shape color
     * @return The Shape object to allow for method chaining
     */
    @Getter @Setter
    int color;

    /**
     * -- GETTER --
     * Gets the GL draw mode of the shape
     *
     * @see Shape#setDrawMode(int)
     * @return The GL draw mode of the shape
     *
     * -- SETTER --
     * Sets the GL draw mode of the shape. Possible draw modes are:<br>
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
     * @param drawMode The new shape draw mode
     * @return The Shape object to allow for method chaining
     */
    @Getter @Setter
    int drawMode;

    Shape(int color) {
        this.color = color;

        this.vertexes = new ArrayList<>();
        this.drawMode = 9;
    }

    public Shape clone() {
        Shape clone = new Shape(this.color);
        clone.vertexes.addAll(this.vertexes);
        clone.setDrawMode(this.drawMode);
        return clone;
    }

    /**
     * Adds a vertex to the shape.
     *
     * @param x The x position
     * @param y The y position
     * @return The shape to allow for method chaining
     */
    public Shape addVertex(float x, float y) {
        this.vertexes.add(new Vector2d(x, y));
        return this;
    }

    /**
     * Inserts a vertex into the shape
     *
     * @param i The index of the insertion
     * @param x The x position
     * @param y The y position
     * @return The shape to allow for method chaining
     */
    public Shape insertVertex(int i, float x, float y) {
        this.vertexes.add(i, new Vector2d(x, y));
        return this;
    }

    /**
     * Removes a vertex from the shape
     *
     * @param i The index to remove
     * @return The shape to allow for method chaining
     */
    public Shape removeVertex(int i) {
        this.vertexes.remove(i);
        return this;
    }

    /**
     * Sets the shape as a line pointing from x1 y1 to x2 y2
     *
     * @param x1        The x starting position
     * @param y1        The y starting position
     * @param x2        The x ending position
     * @param y2        The y ending position
     * @param thickness The thickness
     * @return The shape to allow for method chaining
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
     * @param x      The x position
     * @param y      The y position
     * @param radius The radius
     * @param steps  The number of steps to take to complete the circle
     * @return The shape to allow for method chaining
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
     * @return The shape to allow for method chaining
     */
    public Shape draw() {
        float a = (float) (this.color >> 24 & 255) / 255.0F;
        float r = (float) (this.color >> 16 & 255) / 255.0F;
        float g = (float) (this.color >> 8 & 255) / 255.0F;
        float b = (float) (this.color & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        //#if MC<=10809
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        //#else
        //$$ BufferBuilder worldrenderer = tessellator.getBuffer();
        //#endif

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (!Renderer.colorized)
            GlStateManager.color(r, g, b, a);

        worldrenderer.begin(this.drawMode, DefaultVertexFormats.POSITION);

        for (Vector2d vertex : this.vertexes)
            worldrenderer.pos(vertex.getX(), vertex.getY(), 0.0D).endVertex();

        tessellator.draw();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        Renderer.finishDraw();

        return this;
    }
}
