package com.chattriggers.ctjs.minecraft.libs.renderer;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Used in {@link Renderer#image(String)}
 */
public class Image {
    @Getter
    private float x;
    @Getter
    private float y;
    @Getter
    private int textureWidth;
    @Getter
    private int textureHeight;
    @Getter
    private int textureX;
    @Getter
    private int textureY;
    @Getter
    private float scale;
    @Getter
    private String resourceName;
    @Getter
    private String resourceDomain;

    Image(String resourceName) {
        this.resourceName = resourceName;

        this.x = 0;
        this.y = 0;
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.textureX = 0;
        this.textureY = 0;
        this.scale = 0;
        this.resourceDomain = "ctjs.images";
    }

    /**
     * Sets the image x position.
     *
     * @param x the x position
     * @return the image to allow for method chaining
     */
    public Image setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the image y position.
     *
     * @param y the y position
     * @return the image to allow for method chaining
     */
    public Image setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * Sets the image scale.
     *
     * @param scale the scale
     * @return the image to allow for method chaining
     */
    public Image setScale(float scale) {
        this.scale = scale;
        return this;
    }

    /**
     * Sets the texture width.
     *
     * @param width the width of the texture
     * @return the image to allow for method chaining
     */
    public Image setTextureWidth(int width) {
        this.textureWidth = width;
        return this;
    }

    /**
     * Sets the texture height.
     *
     * @param height the height of the texture
     * @return the image to allow for method chaining
     */
    public Image setTextureHeight(int height) {
        this.textureHeight = height;
        return this;
    }

    /**
     * Sets the texture x location.
     *
     * @param x the x location
     * @return the image to allow for method chaining
     */
    public Image setTextureX(int x) {
        this.textureX = x;
        return this;
    }

    /**
     * Sets the texture y location.
     *
     * @param y the y location
     * @return the image to allow for method chaining
     */
    public Image setTextureY(int y) {
        this.textureY = y;
        return this;
    }

    /**
     * Sets the image resource name.
     *
     * @param name the name
     * @return the image to allow for method chaining
     */
    public Image setResourceName(String name) {
        this.resourceName = name;
        return this;
    }

    /**
     * Sets the image resource domain.
     *
     * @param domain the domain
     * @return the image to allow for method chaining
     */
    public Image setResourceDomain(String domain) {
        this.resourceDomain = domain;
        return this;
    }

    /**
     * Downloads an image to store at the resource name location.
     *
     * @param url          the url to download the image from
     * @param shouldResize if the image should be resized to 256x256
     * @return the image to allow for method chaining
     */
    public Image download(String url, boolean shouldResize) {
        try {
            BufferedImage image = ImageIO.read(new URL(url));

            if (shouldResize) {
                BufferedImage resized = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = resized.createGraphics();
                g.drawImage(image, 0, 0, 256, 256, null);
                g.dispose();

                File resourceFile = new File(CTJS.getInstance().getAssetsDir(), this.resourceName);

                if (resourceFile.exists()) {
                    resourceFile.delete();
                }

                resourceFile.createNewFile();

                ImageIO.write(resized, "png", resourceFile);
            } else {
                File resourceFile = new File(CTJS.getInstance().getAssetsDir(), this.resourceName);
                resourceFile.createNewFile();

                ImageIO.write(image, "png", resourceFile);
            }
        } catch (IOException e) {
            Console.getConsole().printStackTrace(e);
        }

        return this;
    }

    /**
     * Draws the image on screen
     *
     * @return the image to allow for method chaining
     */
    public Image draw() {
        ResourceLocation rl = new ResourceLocation(this.resourceDomain, this.resourceName);

        Client.getMinecraft().getTextureManager().bindTexture(rl);

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glTranslatef(this.x, this.y, 100);
        GL11.glScalef(this.scale, this.scale, this.scale);
        Client.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, this.textureX, this.textureY, this.textureWidth, this.textureHeight);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();

        return this;
    }
}
