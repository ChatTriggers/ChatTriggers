package com.chattriggers.ctjs.utils;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class ImagesPack implements IResourcePack {

    private File imageFolder;

    public ImagesPack(File imageFolder) {
        this.imageFolder = imageFolder;
    }

    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        File resource = new File(this.imageFolder, location.getResourcePath());
        return new FileInputStream(resource);
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        return new File(this.imageFolder, location.getResourcePath()).isFile();
    }

    @Override
    public Set<String> getResourceDomains() {
        return ImmutableSet.of("ctjs.images");
    }

    @Override
    public <T extends IMetadataSection> T getPackMetadata(IMetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return null;
    }

    @Override
    public String getPackName() {
        return "ctjs images";
    }
}
