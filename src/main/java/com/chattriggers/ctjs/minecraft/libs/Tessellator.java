package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.minecraft.wrappers.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.util.ResourceLocation;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

@Accessors(chain = true)
public class Tessellator {
    @Getter
    private ArrayList<Vector3d> vertexes;
    @Getter
    @Setter
    private String resourceName;
    @Getter
    @Setter
    private String resourceDomain;

    public Tessellator() {
        this.vertexes = new ArrayList<>();
        this.resourceDomain = "ctjs.images";
    }

    public Tessellator pos(float x, float y, float z) {
        this.vertexes.add(new Vector3d(x, y, z));
        return this;
    }

    public void draw() {
        //TODO add drawing
        ResourceLocation rl = new ResourceLocation(this.resourceDomain, this.resourceName);
        Client.getMinecraft().getTextureManager().bindTexture(rl);

        vertexes.clear();
    }
}