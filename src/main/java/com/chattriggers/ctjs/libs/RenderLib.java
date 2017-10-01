package com.chattriggers.ctjs.libs;

import com.chattriggers.ctjs.CTJS;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@UtilityClass
@SideOnly(Side.CLIENT)
public class RenderLib {
    /**
     * Gets a strings width.
     * @param text the text to get the width of
     * @return the width of the text
     */
    public static int getStringWidth(String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }

    /**
     * Draws a string to the screen.
     * @param text the text to draw
     * @param x the x coordinate on screen
     * @param y the y coordinate on screen
     * @param color the color
     */
    public static void drawString(String text, float x, float y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color, false);
    }

    /**
     * Draws a string with drop shadow to the screen.
     * @param text the text to draw
     * @param x the x coordinate on screen
     * @param y the y coordinate on screen
     * @param color the color
     */
    public static void drawStringWithShadow(String text, float x, float y, int color) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, x, y, color);
    }

    /**
     * Gets a color int based on 0-255 rgba values.
     * This can be used in settings background and text color.
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
     * Draws a shape with a certain amount of sides, centered around
     * the x and y parameters. Ex. 5 segments makes a pentagon, 360
     * makes a circle.
     * @param segments the number of sides the shape should have
     * @param color the color of the shape
     * @param x the x coordinate for the shape to be centered around
     * @param y the y coordinate for the shape to be centered around
     * @param r the radius of the shape
     */
    public static void drawShape(int color, int segments, float x, float y, float r) {
        double theta = 2 * Math.PI / (segments);
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);

        double xHolder;
        double unitCircleX = 1;
        double unitCircleY = 0;

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GlStateManager.color(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for(int i = 0; i < segments; i++) {
            GL11.glVertex2d(unitCircleX * r + x, unitCircleY * r + y);
            xHolder = unitCircleX;
            unitCircleX = cos * unitCircleX - sin * unitCircleY;
            unitCircleY = sin * xHolder + cos * unitCircleY;
        }
        GL11.glEnd();
    }

    /**
     * Draws a rectangle on screen with location x and y with width and height.
     * @param color the color of the rectangle
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width
     * @param height the height
     */
    public static void drawRectangle(int color, float x, float y, float width, float height) {
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

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y2, 0.0D).endVertex();
        worldrenderer.pos(x2, y2, 0.0D).endVertex();
        worldrenderer.pos(x2, y, 0.0D).endVertex();
        worldrenderer.pos(x, y, 0.0D).endVertex();
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
     * Draws an image to the screen.<br>
     * Images must be in the imports /assets/ directory on launch, <br>
     * and have a size of 256x256.
     * @param resourceName the file name, i.e. block.png
     * @param renderXLoc the x position on the screen to render to
     * @param renderYLoc the y position on the screen to render to
     * @param textureMapX the x position on the image to start rendering from, usually 0
     * @param textureMapY the y position on the image to start rendering from, usually 0
     * @param textureWidth the width of the image to render, usually 256
     * @param textureHeight the height of the image to render, usually 256
     * @param scale the scale of the image, can use decimals, i.e. 0.5, 1.5
     */
    public static void drawImage(String resourceName, int renderXLoc, int renderYLoc,
                                 int textureMapX, int textureMapY, int textureWidth, int textureHeight, float scale) {

        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        ResourceLocation rl = new ResourceLocation("ctjs.images", resourceName);

        Minecraft.getMinecraft().getTextureManager().bindTexture(rl);

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslatef(renderXLoc, renderYLoc, 0);
        GL11.glScalef(scale, scale, scale);
        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, textureMapX, textureMapY, textureWidth, textureHeight);
        GL11.glPopMatrix();
    }

    public static void downloadImage(String url, String resourceName) {
        try {
            BufferedImage image = ImageIO.read(new URL(url));
            BufferedImage resized = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resized.createGraphics();
            g.drawImage(image, 0, 0, 256, 256, null);
            g.dispose();

            File resourceFile = new File(CTJS.getInstance().getAssetsDir(), resourceName);
            resourceFile.createNewFile();

            ImageIO.write(resized, "png", resourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
