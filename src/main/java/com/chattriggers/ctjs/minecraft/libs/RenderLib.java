package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import java.util.ArrayList;

@UtilityClass
@SideOnly(Side.CLIENT)
public class RenderLib {
    public static final int BLACK = color(0, 0, 0, 255);
    public static final int DARK_BLUE = color(0, 0, 190, 255);
    public static final int DARK_GREEN = color(0, 190, 0, 255);
    public static final int DARK_AQUA = color(0, 190, 190, 255);
    public static final int DARK_RED = color(190, 0, 0, 255);
    public static final int DARK_PURPLE = color(190, 0, 190, 255);
    public static final int GOLD = color(217, 163, 52, 255);
    public static final int GRAY = color(190, 190, 190, 255);
    public static final int DARK_GRAY = color(63, 63, 63, 255);
    public static final int BLUE = color(63, 63, 254, 255);
    public static final int GREEN = color(63, 254, 63, 255);
    public static final int AQUA = color(63, 254, 254, 255);
    public static final int RED = color(254, 63, 63, 255);
    public static final int LIGHT_PURPLE = color(254, 63, 254, 255);
    public static final int YELLOW = color(254, 254, 63, 255);
    public static final int WHITE = color(255, 255, 255, 255);

    /**
     * Gets a color based off of a hex integer input
     *
     * @param color the hex integer
     * @return the color
     */
    public static int getColor(int color) {
        switch (color) {
            case (0):
                return BLACK;
            case (1):
                return DARK_BLUE;
            case (2):
                return DARK_GREEN;
            case (3):
                return DARK_AQUA;
            case (4):
                return DARK_RED;
            case (5):
                return DARK_PURPLE;
            case (6):
                return GOLD;
            case (7):
                return GRAY;
            case (8):
                return DARK_GRAY;
            case (9):
                return BLUE;
            case (10):
                return GREEN;
            case (11):
                return AQUA;
            case (12):
                return RED;
            case (13):
                return LIGHT_PURPLE;
            case (14):
                return YELLOW;
            default:
                return WHITE;
        }
    }

    /**
     * Gets the font renderer object.
     *
     * @return the font renderer object
     */
    public static FontRenderer getFontRenderer() {
        return Client.getMinecraft().fontRendererObj;
    }

    /**
     * Gets a strings width.
     *
     * @param text the text to get the width of
     * @return the width of the text
     */
    public static int getStringWidth(String text) {
        return getFontRenderer().getStringWidth(text);
    }

    /**
     * Returns a wrapped list of lines based on a max width with a max number of lines.
     *
     * @param lines    the input list of lines
     * @param width    the max width of a line
     * @param maxLines the max number of lines
     * @return the wrapped line list
     */
    public static ArrayList<String> lineWrap(ArrayList<String> lines, int width, int maxLines) {
        int lineWrapIterator = 0;
        Boolean lineWrapContinue = true;
        Boolean addExtra = false;

        lines = new ArrayList<>(lines);

        while (lineWrapContinue) {
            String line = lines.get(lineWrapIterator).replace("\u0009", "     ");
            if (line.contains(" ") && getStringWidth(line) > width) {
                String[] lineParts = line.split(" ");
                StringBuilder lineBefore = new StringBuilder();
                StringBuilder lineAfter = new StringBuilder();

                Boolean fillBefore = true;
                for (String linePart : lineParts) {
                    if (fillBefore) {
                        if (getStringWidth(lineBefore.toString() + linePart) < width)
                            lineBefore.append(linePart).append(" ");
                        else
                            fillBefore = false;
                    }

                    if (!fillBefore) {
                        lineAfter.append(" ").append(linePart);
                    }
                }

                lines.set(lineWrapIterator, lineBefore.toString());

                if (lines.size() < maxLines) {
                    lines.add(lineWrapIterator + 1, lineAfter.toString());
                } else {
                    addExtra = true;
                    lineWrapContinue = false;
                }
            }

            lineWrapIterator++;
            if (lineWrapIterator >= lines.size()) {
                lineWrapContinue = false;
            }
            if (lines.size() > maxLines) {
                addExtra = true;
                while (lines.size() > maxLines) {
                    lines.remove(lines.size() - 1);
                }
                break;
            }
        }

        if (addExtra) lines.add("...");

        return lines;
    }

    /**
     * Draws a string to the screen.
     *
     * @param text  the text to draw
     * @param x     the x coordinate on screen
     * @param y     the y coordinate on screen
     * @param scale scales the text size
     * @param color the color
     */
    public static void drawString(String text, float x, float y, float scale, int color, Boolean dropShadow) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.scale(scale, scale, scale);
        getFontRenderer().drawString(text, x / scale, y / scale, color, dropShadow);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    /**
     * Draws a string to the screen.
     *
     * @param text  the text to draw
     * @param x     the x coordinate on screen
     * @param y     the y coordinate on screen
     * @param color the color
     */
    public static void drawString(String text, float x, float y, int color) {
        drawString(text, x, y, 1, color, false);
    }

    /**
     * Draws a string to the screen.
     *
     * @param text  the text to draw
     * @param x     the x coordinate on screen
     * @param y     the y coordinate on screen
     */
    public static void drawString(String text, float x, float y) {
        drawString(text, x, y, 1, 0xffffffff, false);
    }

    /**
     * Draws a string with drop shadow to the screen.
     *
     * @param text  the text to draw
     * @param x     the x coordinate on screen
     * @param y     the y coordinate on screen
     * @param scale scales the text size
     * @param color the color
     */
    public static void drawStringWithShadow(String text, float x, float y, float scale, int color) {
        drawString(text, x, y, scale, color, true);
    }

    /**
     * Draws a string with drop shadow to the screen.
     *
     * @param text  the text to draw
     * @param x     the x coordinate on screen
     * @param y     the y coordinate on screen
     * @param color the color
     */
    public static void drawStringWithShadow(String text, float x, float y, int color) {
        drawStringWithShadow(text, x, y, 1, color);
    }

    /**
     * Draws a string with drop shadow to the screen.
     *
     * @param text  the text to draw
     * @param x     the x coordinate on screen
     * @param y     the y coordinate on screen
     */
    public static void drawStringWithShadow(String text, float x, float y) {
        drawStringWithShadow(text, x, y, 1, 0xffffffff);
    }

    /**
     * Gets a color int based on 0-255 rgba values.
     * This can be used in settings background and text color.
     *
     * @param red   value between 0 and 255
     * @param green value between 0 and 255
     * @param blue  value between 0 and 255
     * @param alpha value between 0 and 255
     * @return integer color
     */
    public static int color(int red, int green, int blue, int alpha) {
        return (limit255(alpha) * 0x1000000) + (limit255(red) * 0x10000) + (limit255(green) * 0x100) + blue;
    }

    /**
     * Gets a determined rainbow color based on step and speed.
     *
     * @param step  time elapsed
     * @param speed speed of time
     * @return integer color
     */
    public static int getRainbow(float step, float speed) {
        int red = (int) ((Math.sin(step / speed) + 0.75) * 170);
        int green = (int) ((Math.sin(step / speed + ((2 * Math.PI) / 3)) + 0.75) * 170);
        int blue = (int) ((Math.sin(step / speed + ((4 * Math.PI) / 3)) + 0.75) * 170);
        return 0xff000000 + (limit255(red) * 0x10000) + (limit255(green) * 0x100) + limit255(blue);
    }

    /**
     * Gets a determined rainbow color array based on step and speed.
     *
     * @param step  time elapsed
     * @param speed speed of time
     * @return the array of colors {red,green,blue}
     */
    public static int[] getRainbowColors(float step, float speed) {
        int red = (int) ((Math.sin(step / speed) + 0.75) * 170);
        int green = (int) ((Math.sin(step / speed + ((2 * Math.PI) / 3)) + 0.75) * 170);
        int blue = (int) ((Math.sin(step / speed + ((4 * Math.PI) / 3)) + 0.75) * 170);
        return new int[]{red, green, blue};
    }

    /**
     * Gets a determined rainbow color based on step with a default speed of 1.
     *
     * @param step time elapsed
     * @return integer color
     */
    public static int getRainbow(float step) {
        return getRainbow(step, 1f);
    }

    // helper method to limit numbers between 0 and 255
    public static int limit255(int a) {
        return (a > 255) ? 255 : (a < 0 ? 0 : a);
    }

    /**
     * gets the current resolution width scaled to guiScale.
     *
     * @return scaled width
     */
    public static int getRenderWidth() {
        ScaledResolution res = new ScaledResolution(Client.getMinecraft());
        return res.getScaledWidth();
    }

    /**
     * gets the current resolution height scaled to guiScale.
     *
     * @return scaled height
     */
    public static int getRenderHeight() {
        ScaledResolution res = new ScaledResolution(Client.getMinecraft());
        return res.getScaledHeight();
    }

    /**
     * Draws a shape with a certain amount of sides, centered around
     * the x and y parameters. Ex. 5 segments makes a pentagon, 360
     * makes a circle.
     *
     * @param segments the number of sides the shape should have
     * @param color    the color of the shape
     * @param x        the x coordinate for the shape to be centered around
     * @param y        the y coordinate for the shape to be centered around
     * @param r        the radius of the shape
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

        GlStateManager.pushMatrix();
        GlStateManager.color(red, green, blue, alpha);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i < segments; i++) {
            GL11.glVertex2d(unitCircleX * r + x, unitCircleY * r + y);
            xHolder = unitCircleX;
            unitCircleX = cos * unitCircleX - sin * unitCircleY;
            unitCircleY = sin * xHolder + cos * unitCircleY;
        }
        GL11.glEnd();

        GlStateManager.popMatrix();
    }

    /**
     * Draws a rectangle on screen with location x and y with width and height.
     *
     * @param color  the color of the rectangle
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param width  the width
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

    /**
     * Draws a line with thickness to the screen.
     *
     * @param color     color of the line
     * @param thickness thickness of the line
     * @param xy1       [x,y] array for point 1
     * @param xy2       [x,y] array for point 2
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

            GlStateManager.pushMatrix();

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

            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();

            GlStateManager.popMatrix();
        }
    }

    /**
     * Draws a polygon to the screen.
     *
     * @param color  color of the polygon
     * @param points [x,y] array for points
     */
    public static void drawPolygon(int color, Double[]... points) {
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;

        GlStateManager.pushMatrix();

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
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
    }

    /**
     * Draws an image to the screen.<br>
     * Images must be in the modules /assets/ directory on launch, <br>
     * and have a size of 256x256.
     *
     * @param resourceName  the file name, i.e. block.png
     * @param renderXLoc    the x position on the screen to render to
     * @param renderYLoc    the y position on the screen to render to
     * @param textureMapX   the x position on the image to start rendering from, usually 0
     * @param textureMapY   the y position on the image to start rendering from, usually 0
     * @param textureWidth  the width of the image to render, usually 256
     * @param textureHeight the height of the image to render, usually 256
     * @param scale         the scale of the image, can use decimals, i.e. 0.5, 1.5
     */
    public static void drawImage(String resourceName, float renderXLoc, float renderYLoc,
                                 int textureMapX, int textureMapY, int textureWidth, int textureHeight, float scale) {
        drawImage("ctjs.images", resourceName, renderXLoc, renderYLoc,
                textureMapX, textureMapY, textureWidth, textureHeight, scale);
    }

    /**
     * Draws an image to the screen.<br>
     * Images must be in the modules /assets/ directory on launch, <br>
     * and have a size of 256x256.
     *
     * @param resourceDomain the domain of the file, i.e minecraft
     * @param resourceName   the file name, i.e. block.png
     * @param renderXLoc     the x position on the screen to render to
     * @param renderYLoc     the y position on the screen to render to
     * @param textureMapX    the x position on the image to start rendering from, usually 0
     * @param textureMapY    the y position on the image to start rendering from, usually 0
     * @param textureWidth   the width of the image to render, usually 256
     * @param textureHeight  the height of the image to render, usually 256
     * @param scale          the scale of the image, can use decimals, i.e. 0.5, 1.5
     */
    public static void drawImage(String resourceDomain, String resourceName, float renderXLoc, float renderYLoc,
                                 int textureMapX, int textureMapY, int textureWidth, int textureHeight, float scale) {

        ResourceLocation rl = new ResourceLocation(resourceDomain, resourceName);

        drawImage(rl, renderXLoc, renderYLoc, textureMapX, textureMapY, textureWidth, textureHeight, scale);
    }

    /**
     * Draw Image helper method. <br>
     * This method is not meant for public use
     */
    public static void drawImage(ResourceLocation rl, float renderXLoc, float renderYLoc, int textureMapX,
                                 int textureMapY, int textureWidth, int textureHeight, float scale) {
        Client.getMinecraft().getTextureManager().bindTexture(rl);

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslatef(renderXLoc, renderYLoc, 0);
        GL11.glScalef(scale, scale, scale);
        Client.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, textureMapX, textureMapY, textureWidth, textureHeight);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }

    /**
     * Downloads and resizes an image from a url supplied when calling
     *
     * @param url url of the image to be downloaded
     * @param resourceName file name for the image when it is downloaded
     */
    public static void downloadImage(String url, String resourceName) {
        downloadImage(url, resourceName, true);
    }

    /**
     * Downloads an image from a url supplied when calling, with the option to automatically resize the image
     *
     * @param url url of the image to be downloaded
     * @param resourceName file name for the image when it is downloaded
     * @param shouldResize whether or not the file should be resized by CT
     */
    public static void downloadImage(String url, String resourceName, boolean shouldResize) {
        try {
            BufferedImage image = ImageIO.read(new URL(url));

            if (shouldResize) {
                BufferedImage resized = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = resized.createGraphics();
                g.drawImage(image, 0, 0, 256, 256, null);
                g.dispose();

                File resourceFile = new File(CTJS.getInstance().getAssetsDir(), resourceName);

                if (resourceFile.exists()) {
                    resourceFile.delete();
                }

                resourceFile.createNewFile();

                ImageIO.write(resized, "png", resourceFile);
            } else {
                File resourceFile = new File(CTJS.getInstance().getAssetsDir(), resourceName);
                resourceFile.createNewFile();

                ImageIO.write(image, "png", resourceFile);
            }
        } catch (IOException e) {
            Console.getConsole().printStackTrace(e);
        }
    }

    /**
     * Renders an item icon on screen from an {@link com.chattriggers.ctjs.minecraft.wrappers.objects.Item}
     *
     * @param x     x coordinate to render item icon to
     * @param y     y coordinate to render item icon to
     * @param scale scales the icon size
     * @param item  the {@link com.chattriggers.ctjs.minecraft.wrappers.objects.Item} to render
     */
    public static void drawItemIcon(int x, int y, float scale, com.chattriggers.ctjs.minecraft.wrappers.objects.Item item) {
        x /= scale;
        y /= scale;

        RenderItem itemRenderer = Client.getMinecraft().getRenderItem();

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.zLevel = 200.0F;

        try {
            itemRenderer.renderItemIntoGUI(item.getItemStack(), x, y);
        } catch (NullPointerException e) {
            Console.getConsole().printStackTrace(e);
        }

        GlStateManager.popMatrix();
    }

    /**
     * Renders an item icon on screen.
     *
     * @param x        x coordinate to render item icon to
     * @param y        y coordinate to render item icon to
     * @param scale    scales the icon size
     * @param item     name or id of item to render
     * @param metadata metadata of item to render
     */
    public static void drawItemIcon(int x, int y, float scale, String item, Integer metadata) {
        if (item.equals("minecraft:air")) return;

        if (item.equals("minecraft:cocoa")) {
            item = "minecraft:dye";
            metadata = 3;
        } else if ((item.equals("minecraft:dirt") || item.equals("minecraft:potato") || item.equals("minecraft:carrot"))) {
            metadata = 0;
        }

        ItemStack itemStack;
        if (metadata != null) {
            itemStack = new ItemStack(Item.getByNameOrId(item), 1, metadata);
        } else {
            itemStack = new ItemStack(Item.getByNameOrId(item));
        }

        drawItemIcon(x, y, scale, new com.chattriggers.ctjs.minecraft.wrappers.objects.Item(itemStack));
    }

    /**
     * Renders an item icon on screen.
     *
     * @param x        x coordinate to render item icon to
     * @param y        y coordinate to render item icon to
     * @param item     name or id of item to render
     * @param metadata metadata of item to render
     */
    public static void drawItemIcon(int x, int y, String item, Integer metadata) {
        drawItemIcon(x, y, 1, item, metadata);
    }

    /**
     * Renders an item icon on screen.
     *
     * @param x    x coordinate to render item icon to
     * @param y    y coordinate to render item icon to
     * @param item name or id of item to render
     */
    public static void drawItemIcon(int x, int y, String item) {
        drawItemIcon(x, y, 1, item, null);
    }

    /**
     * Render the player model on to the screen
     *
     * @param posX   the x position on the screen
     * @param posY   the y position on the screen
     * @param scale  how much to scale the entity by
     * @param rotate whether or not the drawn player should rotate
     */
    public static void drawPlayerOnScreen(int posX, int posY, int scale, boolean rotate) {
        float mouseX = -30;
        float mouseY = 0;
        EntityLivingBase ent = Player.getPlayer();

        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale((float) (-scale), (float) scale, (float) scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-45.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        if (!rotate) {
            ent.renderYawOffset = (float) Math.atan((double) (mouseX / 40.0F)) * 20.0F;
            ent.rotationYaw = (float) Math.atan((double) (mouseX / 40.0F)) * 40.0F;
            ent.rotationPitch = -((float) Math.atan((double) (mouseY / 40.0F))) * 20.0F;
            ent.rotationYawHead = ent.rotationYaw;
            ent.prevRotationYawHead = ent.rotationYaw;
        }
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Client.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
