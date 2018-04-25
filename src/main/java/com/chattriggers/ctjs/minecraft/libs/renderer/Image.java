package com.chattriggers.ctjs.minecraft.libs.renderer;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

/**
 * Used in {@link Renderer#image(String)}
 */
@Accessors(chain = true)
public class Image {
    @Getter
    @Setter
    private float x;

    @Getter
    @Setter
    private float y;

    @Getter
    private DynamicTexture texture;

    Image(BufferedImage image) {
        this.x = 0;
        this.y = 0;

        this.texture = new DynamicTexture(image);
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
            Console.getConsole().out.println("Error loading image " + name + " from url " + url);
            Console.getConsole().printStackTrace(e);
        }

        return null;
    }

    /**
     * Draws the image on screen
     *
     * @return The Image object to allow for method chaining
     */
    public Image draw(int size) {
        Renderer.drawImage(this, size);

        return this;
    }

    int transform(int size) {
        GlStateManager.translate(x, y, 100);
        GlStateManager.scale(size / 256, size / 256, 100);
        return size / 256;
    }
}
