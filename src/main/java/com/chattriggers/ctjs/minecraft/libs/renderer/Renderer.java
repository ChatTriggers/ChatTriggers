package com.chattriggers.ctjs.minecraft.libs.renderer;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.MathLib;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.minecraft.wrappers.Player;
import com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@UtilityClass
@SideOnly(Side.CLIENT)
public class Renderer {
    public boolean colorized = false;

    public final int BLACK = color(0, 0, 0, 255);
    public final int DARK_BLUE = color(0, 0, 190, 255);
    public final int DARK_GREEN = color(0, 190, 0, 255);
    public final int DARK_AQUA = color(0, 190, 190, 255);
    public final int DARK_RED = color(190, 0, 0, 255);
    public final int DARK_PURPLE = color(190, 0, 190, 255);
    public final int GOLD = color(217, 163, 52, 255);
    public final int GRAY = color(190, 190, 190, 255);
    public final int DARK_GRAY = color(63, 63, 63, 255);
    public final int BLUE = color(63, 63, 254, 255);
    public final int GREEN = color(63, 254, 63, 255);
    public final int AQUA = color(63, 254, 254, 255);
    public final int RED = color(254, 63, 63, 255);
    public final int LIGHT_PURPLE = color(254, 63, 254, 255);
    public final int YELLOW = color(254, 254, 63, 255);
    public final int WHITE = color(255, 255, 255, 255);

    /**
     * Gets a color based off of a hex integer input
     *
     * @param color the hex integer
     * @return the color
     */
    public int getColor(int color) {
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
    public FontRenderer getFontRenderer() {
        //#if MC<=10809
        return Client.getMinecraft().fontRendererObj;
        //#else
        //$$ return Client.getMinecraft().fontRenderer;
        //#endif
    }

    /**
     * Gets a strings width when rendered.
     *
     * @param text the text to get the width of
     * @param removeFormatting if formatting should be removed
     * @return the width of the text
     */
    public int getStringWidth(String text, boolean removeFormatting) {
        if (removeFormatting) text = ChatLib.removeFormatting(text);
        return getFontRenderer().getStringWidth(text);
    }

    /**
     * Gets a strings width when rendered with formatting removed.
     *
     * @param text the text to get the width of
     * @return the width of the text
     */
    public int getStringWidth(String text) {
        return getStringWidth(text, true);
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
     * Gets a determined rainbow color based on step with a default speed of 1.
     *
     * @param step time elapsed
     * @return integer color
     */
    public int getRainbow(float step) {
        return getRainbow(step, 1f);
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
     * Simple method to draw a rectangle on the Client's overlay.<br>
     * For more rectangle options, use {@link Rectangle}
     *
     * @param color the color
     * @param x the x position
     * @param y the y position
     * @param width the width
     * @param height the height
     */
    public void drawRect(int color, float x, float y, float width, float height) {
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

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        //#if MC<=10809
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        //#else
        //$$ BufferBuilder worldrenderer = tessellator.getBuffer();
        //#endif

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (!Renderer.colorized)
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

        finishDraw();
    }

    /**
     * Simple method to draw a string to the Client's overlay.<br>
     * For more text options, use {@link Text}
     *
     * @param text the string
     * @param x the x position
     * @param y the y position
     */
    public void drawString(String text, float x, float y) {
        getFontRenderer().drawString(ChatLib.addColor(text), x, y, 0xffffffff, false);

        finishDraw();
    }

    /**
     * Simple method to draw a string with a drop shadow to the Client's overlay.<br>
     * For more text options, use {@link Text}
     *
     * @param text the string
     * @param x the x position
     * @param y the y position
     */
    public void drawStringWithShadow(String text, float x, float y) {
        getFontRenderer().drawString(ChatLib.addColor(text), x, y, 0xffffffff, true);

        finishDraw();
    }

    public void drawImage(Image image, double x, double y, double width, double height) {
        if (image.getTexture() == null) return;

        if (!Renderer.colorized)
            GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.scale(1, 1, 50);
        GlStateManager.bindTexture(image.getTexture().getGlTextureId());
        GlStateManager.enableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        //#if MC<=10809
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        //#else
        //$$ BufferBuilder worldrenderer = tessellator.getBuffer();
        //#endif

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        worldrenderer.pos(x,y + height,0)
                .tex(0, 1).endVertex();
        worldrenderer.pos(x + width,y + height, 0)
                .tex(1, 1).endVertex();
        worldrenderer.pos(x + width, y, 0)
                .tex(1, 0).endVertex();
        worldrenderer.pos(x, y, 0)
                .tex(0, 0).endVertex();
        tessellator.draw();

        finishDraw();
    }

    /**
     * Render the player model on to the screen
     *
     * @param player the player to render
     * @param x   the x position on the screen
     * @param y   the y position on the screen
     * @param rotate whether or not the drawn player should rotate
     */
    public void drawPlayer(Object player, int x, int y, boolean rotate) {
        float mouseX = -30;
        float mouseY = 0;

        EntityLivingBase ent = Player.getPlayer();
        if (player instanceof PlayerMP)
            ent = ((PlayerMP) player).getPlayer();

        GlStateManager.enableColorMaterial();
        RenderHelper.enableStandardItemLighting();

        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;

        GlStateManager.translate((float) x, (float) y, 50.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
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
        //#if MC<=10809
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        //#else
        //$$ rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        //#endif
        rendermanager.setRenderShadow(true);

        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        finishDraw();
    }

    /**
     * Offset by x and y.
     *
     * @param x the x offset
     * @param y the y offset
     */
    public void translate(float x, float y) {
        GlStateManager.translate(x, y, 0);
    }

    /**
     * Scales a drawn overlay object using GlStateManager.
     *
     * @param scale the scale
     */
    public void scale(float scale) {
        scale(scale, scale);
    }

    /**
     * Scales a drawn overlay object disproportionally using GlStateManager.
     *
     * @param scaleX the x scale
     * @param scaleY the y scale
     */
    public void scale(float scaleX, float scaleY) {
        GlStateManager.scale(scaleX, scaleY, 1);
    }

    /**
     * Rotate a drawn overlay object using GlStateManager.
     *
     * @param angle the angle
     */
    public void rotate(float angle) {
        GlStateManager.rotate(angle, 0, 0, 1);
    }

    /**
     * Shifts the color of a drawn overlay object using GlStateManager.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     * @param alpha the alpha value
     */
    public void colorize(int red, int green, int blue, int alpha) {
        colorized = true;

        GlStateManager.color(
                MathLib.clamp(red, 0, 255),
                MathLib.clamp(green, 0, 255),
                MathLib.clamp(blue, 0, 255),
                MathLib.clamp(alpha, 0, 255)
        );
    }

    /**
     * Shifts the color of a drawn overlay object using GlStateManager.<br>
     * This method defaults alpha to 255.
     *
     * @param red the red value
     * @param green the green value
     * @param blue the blue value
     */
    public void colorize(int red, int green, int blue) {
        colorize(red, green, blue, 255);
    }

    public void finishDraw() {
        colorized = false;

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
    }

    /**
     * Creates a new {@link Image} object. If the file with the specified name is found,
     * then it will be loaded, otherwise the file will be downloaded from the url and
     * then saved to a file with said name to be loaded from in the future.
     * <p>
     *     <strong>This instances a new object and should be treated as such.</strong><br>
     *     This should not be used to instance and draw an image every frame, instead use
     *     {@link Renderer#drawImage(Image, double, double, double, double)}
     * </p>
     *
     * @param name the name of the file to save/load from
     * @param url the url of the file to download
     * @return a new {@link Image} object
     */
    public Image image(String name, String url) {
        return Image.load(name, url);
    }

    /**
     * Creates a new {@link Text} object.
     * <p>
     *     <strong>This instances a new object and should be treated as such.</strong><br>
     *     This should not be used to instance and draw a string every frame, instead use
     *     {@link Renderer#drawString(String, float, float)} or {@link Renderer#drawStringWithShadow(String, float, float)}
     *     for simple text.
     * </p>
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
     * <p>
     *     <strong>This instances a new object and should be treated as such.</strong><br>
     *     This should not be used to instance and draw a rectangle every frame, instead use
     *     {@link Renderer#drawRect(int, float, float, float, float)}
     *     for a simple rectangle.
     * </p>
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
     * <p>
     *     <strong>This instances a new object and should be treated as such.</strong><br>
     *     This should not be used to instance and draw a shape every frame.
     * </p>
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
