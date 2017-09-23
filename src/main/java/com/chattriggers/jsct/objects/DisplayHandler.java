package com.chattriggers.jsct.objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class DisplayHandler {
    private ArrayList<Display> displays;


    public DisplayHandler() {
        displays = new ArrayList<>();
    }

    /**
     * Registers a display to be rendered every tick.
     * @param display the display to be rendered
     */
    public void registerDisplay(Display display) {
        this.displays.add(display);
    }

    public void clearDisplays() {
        displays.clear();
    }

    @SubscribeEvent
    public void renderDisplays(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            for (Display display : displays) {
                display.render();
            }
        }
    }

    // background type
    public enum Background {
        NONE, FULL, PER_LINE
    }

    // text align
    public enum Align {
        LEFT, CENTER, RIGHT
    }

    // line order
    public enum Order {
        UP, DOWN
    }

    /**
     * gets the current resolution width scaled to guiScale.
     * @return scaled width
     */
    public static int getRenderWidth() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        return res.getScaledWidth();
    }

    /**
     * gets the current resolution height scaled to guiScale.
     * @return scaled height
     */
    public static int getRenderHeight() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        return res.getScaledHeight();
    }

    /**
     * Gets a color int based on 0-255 rgba values.
     * This can be used in settings background and text color.
     * TODO: maybe move to somewhere more useful
     * @param red value between 0 and 255
     * @param green value between 0 and 255
     * @param blue value between 0 and 255
     * @param alpha value between 0 and 255
     * @return integer color
     */
    public static int color(int red, int green, int blue, int alpha) {
        if (red > 255) red = 255;
        if (green > 255) green = 255;
        if (blue > 255) blue = 255;
        if (red < 0) red = 0;
        if (green < 0) green = 0;
        if (blue < 0) blue = 0;
        return (alpha * 0x1000000) + (red * 0x10000) + (green * 0x100) + blue;
    }

    /**
     * Draws a polygon to the screen.
     * @param color color of the polygon
     * @param points [x,y] array for points
     */
    public static void drawPolygon(int color, Double[]... points) {
        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION);

        for (Double[] point : points) {
            if (point.length == 2) {
                worldrenderer.pos(point[0], point[1], 0.0D).endVertex();
            }
        }

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a line with thickness to the screen.
     * @param color color of the line
     * @param thickness thickness of the line
     * @param xy1 [x,y] array for point 1
     * @param xy2 [x,y] array for point 2
     */
    public static void drawLine(int color, Double thickness, Double[] xy1, Double[] xy2) {
        if (xy1.length == 2 && xy2.length == 2) {
            double theta = -Math.atan2(xy2[1] - xy1[1], xy2[0] - xy1[0]);
            double i = Math.sin(theta) * (thickness / 2);
            double j = Math.cos(theta) * (thickness / 2);

            double ax = xy1[0] + i;
            double ay = xy1[1] + j;
            double dx = xy1[0] - i;
            double dy = xy1[1] - j;

            double bx = xy2[0] + i;
            double by = xy2[1] + j;
            double cx = xy2[0] - i;
            double cy = xy2[1] - j;

            float a = (float) (color >> 24 & 255) / 255.0F;
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;

            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();

            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(r, g, b, a);

            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos(ax, ay, 0.0D).endVertex();
            worldrenderer.pos(bx, by, 0.0D).endVertex();
            worldrenderer.pos(cx, cy, 0.0D).endVertex();
            worldrenderer.pos(dx, dy, 0.0D).endVertex();

            tessellator.draw();

            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }
}
