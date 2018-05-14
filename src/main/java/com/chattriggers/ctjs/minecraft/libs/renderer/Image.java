package com.chattriggers.ctjs.minecraft.libs.renderer;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Used in {@link Renderer#image(String, String)} )}
 */
@Accessors(chain = true)
public class Image {
    @Getter
    private BufferedImage imageToLoad;

    @Getter
    private DynamicTexture texture;

    private Image(BufferedImage image) {
        MinecraftForge.EVENT_BUS.register(this);

        this.imageToLoad = image;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void render(RenderGameOverlayEvent event) {
        if (imageToLoad != null) {
            texture = new DynamicTexture(imageToLoad);
            imageToLoad = null;
        }
    }

    /**
     * Downloads an image to store at the resource name location.
     *
     * @param url          The url to download the image from
     * @return The Image object to allow for method chaining
     */
    public static Image load(String name, String url) {
        File resourceFile = new File(CTJS.getInstance().getAssetsDir(), name);

        try {
            if (resourceFile.exists()) {
                return new Image(ImageIO.read(resourceFile));
            }

            BufferedImage image = ImageIO.read(new URL(url));
            ImageIO.write(image, "png", resourceFile);
            return new Image(image);
        } catch (Exception e) {
            Console.getInstance().out.println("Error loading image " + name + " from url " + url);
            Console.getInstance().printStackTrace(e);
        }

        return null;
    }

    /**
     * Loads an image from a path. Use ./ for the .minecraft folder.
     *
     * @param path The path to load the image from
     * @return The Image object to allow for method chaining
     */
    public static Image load(String path) {
        File file = new File(path);

        try {
            return new Image(ImageIO.read(file));
        } catch (IOException e) {
            Console.getInstance().out.println("Error loading image with path " + path);
            Console.getInstance().printStackTrace(e);
        }

        return null;
    }

    /**
     * Draws the image on screen with scale
     *
     * @return The Image object to allow for method chaining
     */
    public Image draw(int x, int y, int size) {
        Renderer.drawImage(this, x, y, size);

        return this;
    }

    /**
     * Draws the image on screen
     *
     * @return the Image object to allow for method chaining
     */
    public Image draw(int x, int y) {
        Renderer.drawImage(this, x, y, 256);

        return this;
    }
}
