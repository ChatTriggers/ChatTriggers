//package com.chattriggers.ctjs.utils.capes;
//
//import com.chattriggers.ctjs.minecraft.libs.FileLib;
//import com.google.gson.Gson;
//import lombok.Getter;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.AbstractClientPlayer;
//import net.minecraft.client.renderer.IImageBuffer;
//import net.minecraft.client.renderer.ImageBufferDownload;
//import net.minecraft.client.renderer.ThreadDownloadImageData;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.util.ResourceLocation;
//
//import java.awt.*;
//import java.awt.image.BufferedImage;
//
//public class CapeHandler {
//    @Getter
//    private static CapeHandler instance;
//    @Getter
//    private Special special;
//
//    public CapeHandler() {
//        instance = this;
//
//        this.special = new Gson().fromJson(
//                FileLib.getUrlContent("http://167.99.3.229/tracker/special.json"),
//                Special.class
//        );
//
//        bindTexture("http://167.99.3.229/assets/images/supporter_cape.png", "capes/ct/supporter");
//        bindTexture("http://167.99.3.229/assets/images/developer_cape.png", "capes/ct/developer");
//    }
//
//    public ResourceLocation getCapeResource(AbstractClientPlayer player) {
//        for (String supporter : this.special.supporters) {
//            if (!supporter.equals(player.getUniqueID().toString())) continue;
//            return new ResourceLocation("capes/ct/supporter");
//        }
//
//        for (String developer : this.special.developers) {
//            if (!developer.equals(player.getUniqueID().toString())) continue;
//            return new ResourceLocation("capes/ct/developer");
//        }
//
//        return null;
//    }
//
//    private void bindTexture(String url, String resource) {
//        IImageBuffer iib = new IImageBuffer() {
//            ImageBufferDownload ibd = new ImageBufferDownload();
//
//            public BufferedImage parseUserSkin(BufferedImage var1) {
//                return parseCape(var1);
//            }
//
//            public void skinAvailable() {}
//        };
//
//        ResourceLocation rl = new ResourceLocation(resource);
//        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
//        textureManager.getTexture(rl);
//        ThreadDownloadImageData textureCape = new ThreadDownloadImageData(null, url, null, iib);
//        textureManager.loadTexture(rl, textureCape);
//    }
//
//    private BufferedImage parseCape(BufferedImage img) {
//        int imageWidth = 64;
//        int imageHeight = 32;
//
//        int srcWidth = img.getWidth();
//        int srcHeight = img.getHeight();
//        while ((imageWidth < srcWidth) || (imageHeight < srcHeight)) {
//            imageWidth *= 2;
//            imageHeight *= 2;
//        }
//        BufferedImage imgNew = new BufferedImage(imageWidth, imageHeight, 2);
//        Graphics g = imgNew.getGraphics();
//        g.drawImage(img, 0, 0, null);
//        g.dispose();
//        return imgNew;
//    }
//
//    private class Special {
//        @Getter
//        private String[] supporters = null;
//        @Getter
//        private String[] developers = null;
//    }
//}
