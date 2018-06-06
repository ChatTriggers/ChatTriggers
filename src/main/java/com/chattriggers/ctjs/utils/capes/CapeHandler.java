package com.chattriggers.ctjs.utils.capes;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CapeHandler {
    @Getter
    private static CapeHandler instance;

    public CapeHandler() {
        instance = this;

        bindTexture("https://i.imgur.com/lzBi0WE.png", "capes/ct/supporter");
    }

    public ResourceLocation getCapeResource(AbstractClientPlayer player) {
        // TODO add player checking
        return new ResourceLocation("capes/ct/supporter");
    }

    private void bindTexture(String url, String resource) {
        IImageBuffer iib = new IImageBuffer() {
            ImageBufferDownload ibd = new ImageBufferDownload();

            public BufferedImage parseUserSkin(BufferedImage var1) {
                return parseCape(var1);
            }

            public void skinAvailable() {}
        };

        ResourceLocation rl = new ResourceLocation(resource);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.getTexture(rl);
        ThreadDownloadImageData textureCape = new ThreadDownloadImageData(null, url, null, iib);
        textureManager.loadTexture(rl, textureCape);
    }

    private BufferedImage parseCape(BufferedImage img) {
        int imageWidth = 64;
        int imageHeight = 32;

        int srcWidth = img.getWidth();
        int srcHeight = img.getHeight();
        while ((imageWidth < srcWidth) || (imageHeight < srcHeight)) {
            imageWidth *= 2;
            imageHeight *= 2;
        }
        BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return imgNew;
    }
}
