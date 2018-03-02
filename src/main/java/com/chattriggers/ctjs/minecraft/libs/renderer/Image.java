package com.chattriggers.ctjs.minecraft.libs.renderer;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.wrappers.Client;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
public class Image {
    /**
     * -- GETTER --
     * Gets the image x position
     *
     * @return The image x position
     *
     * -- SETTER --
     * Sets the image x position
     *
     * @param x The new image x position
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
    private float x;

    /**
     * -- GETTER --
     * Gets the image y position
     *
     * @return The image y position
     *
     * -- SETTER --
     * Sets the image y position
     *
     * @param y The new image y position
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
    private float y;

    /**
     * -- GETTER --
     * Gets the texture width
     *
     * @return The texture width
     *
     * -- SETTER --
     * Sets the texture width
     *
     * @param textureWidth The new texture width
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
    private int textureWidth;

    /**
     * -- GETTER --
     * Gets the texture height
     *
     * @return The texture height
     *
     * -- SETTER --
     * Sets the texture height
     *
     * @param textureHeight The new texture height
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
    private int textureHeight;

    /**
     * -- GETTER --
     * Gets the texture x location
     *
     * @return The texture x location
     *
     * -- SETTER --
     * Sets the texture x location
     *
     * @param textureX The new texture x location
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
    private int textureX;

    /**
     * -- GETTER --
     * Gets the texture y location
     *
     * @return The texture y location
     *
     * -- SETTER --
     * Sets the texture y location
     *
     * @param textureY The new texture y location
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
    private int textureY;

    /**
     * -- GETTER --
     * Gets the image scale
     *
     * @return The image scale
     *
     * -- SETTER --
     * Sets the image scale
     *
     * @param scale The new image scale
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
    private float scale;

    /**
     * -- GETTER --
     * Gets the image resource name
     *
     * @return The image resource name
     *
     * -- SETTER --
     * Sets the image resource name
     *
     * @param resourceName The new image resource name
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
    private String resourceName;

    /**
     * -- GETTER --
     * Gets the image resource domain
     *
     * @return The image resource domain
     *
     * -- SETTER --
     * Sets the image resource domain
     *
     * @param resourceDomain The new image resource domain
     * @return The Image object to allow for method chaining
     */
    @Getter @Setter
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
     * Downloads an image to store at the resource name location.
     *
     * @param url          The url to download the image from
     * @param shouldResize If the image should be resized to 256x256
     * @return The Image object to allow for method chaining
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
     * @return The Image object to allow for method chaining
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
