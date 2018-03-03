package com.chattriggers.ctjs.minecraft.libs.renderer;

import com.chattriggers.ctjs.minecraft.libs.MathLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@UtilityClass
@SideOnly(Side.CLIENT)
public class Renderer {
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
     * Gets a color int based on 0-255 rgba values.
     *
     * @param red   red value
     * @param green green value
     * @param blue  blue value
     * @param alpha alpha value
     * @return integer color
     */
    public int color(int red, int green, int blue, int alpha) {
        return (MathLib.clamp(alpha, 0, 255) * 0x1000000)
                + (MathLib.clamp(red, 0, 255) * 0x10000)
                + (MathLib.clamp(green, 0, 255) * 0x100)
                + MathLib.clamp(blue, 0, 255);
    }

    /**
     * Gets a color int based on 0-255 rgb values.
     *
     * @param red   red value
     * @param green green value
     * @param blue  blue value
     * @return integer color
     */
    public int color(int red, int green, int blue) {
        return color(red, green, blue, 255);
    }

    /**
     * Gets a determined rainbow color based on step and speed.
     *
     * @param step  time elapsed
     * @param speed speed of time
     * @return integer color
     */
    public int getRainbow(float step, float speed) {
        int red = (int) ((Math.sin(step / speed) + 0.75) * 170);
        int green = (int) ((Math.sin(step / speed + ((2 * Math.PI) / 3)) + 0.75) * 170);
        int blue = (int) ((Math.sin(step / speed + ((4 * Math.PI) / 3)) + 0.75) * 170);
        return 0xff000000
                + (MathLib.clamp(red, 0, 255) * 0x10000)
                + (MathLib.clamp(green, 0, 255) * 0x100)
                + MathLib.clamp(blue, 0, 255);
    }

    /**
     * Gets a determined rainbow color array based on step and speed.
     *
     * @param step  time elapsed
     * @param speed speed of time
     * @return the array of colors {red,green,blue}
     */
    public int[] getRainbowColors(float step, float speed) {
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
    public int getRainbow(float step) {
        return getRainbow(step, 1f);
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

    /**
     * Creates a new {@link Image} object.
     *
     * @param resourceName the name of the resource (image-name.png)
     * @return a new {@link Image} object
     */
    public Image image(String resourceName) {
        return new Image(resourceName);
    }

    /**
     * Creates a new {@link Text} object.
     *
     * @param text the text string
     * @param x    the x position
     * @param y    the y position
     * @return a new {@link Text} object
     */
    public Text text(String text, float x, float y) {
        return new Text(text, x, y);
    }

    /**
     * Creates a new {@link Rectangle} object.
     *
     * @param color  the {@link Renderer#color(int, int, int, int)} of the rectangle
     * @param x      the x position of the rectangle
     * @param y      the y position of the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     * @return a new {@link Rectangle} object
     */
    public Rectangle rectangle(int color, float x, float y, float width, float height) {
        return new Rectangle(color, x, y, width, height);
    }

    /**
     * Creates a new {@link Shape} object.
     *
     * @param color the {@link Renderer#color(int, int, int, int)} of the shape
     * @return a new {@link Shape} object
     */
    public Shape shape(int color) {
        return new Shape(color);
    }

    public class screen {
        public static int getWidth() {
            return new ScaledResolution(Client.getMinecraft()).getScaledWidth();
        }

        public static int getHeight() {
            return new ScaledResolution(Client.getMinecraft()).getScaledHeight();
        }

        public static int getScale() {
            return new ScaledResolution(Client.getMinecraft()).getScaleFactor();
        }
    }
}
