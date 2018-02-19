package com.chattriggers.ctjs.minecraft.objects;

import com.chattriggers.ctjs.minecraft.wrappers.World;
import net.minecraft.client.particle.EntityFX;

public class ParticleEffect extends EntityFX {
    public ParticleEffect(double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(World.getWorld(), x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public ParticleEffect(double x, double y, double z) {
        this(x, y, z, 0, 0, 0);
    }

//    public ParticleEffect setTexture(String textureName) {
//        super.tex
//    }

    public ParticleEffect scale(float scale) {
        super.multipleParticleScaleBy(scale);

        return this;
    }

    public ParticleEffect multiplyVelocity(float multiplier) {
        super.multiplyVelocity(multiplier);

        return this;
    }

    public ParticleEffect setColor(float r, float g, float b) {
        super.setRBGColorF(r, g, b);

        return this;
    }

    public ParticleEffect setColor(float r, float g, float b, float a) {
        setColor(r, g, b);

        setAlpha(a);

        return this;
    }

    public ParticleEffect setColor(int color) {
        float red = (float)(color >> 16 & 255) / 255.0F;
        float blue = (float)(color >> 8 & 255) / 255.0F;
        float green = (float)(color & 255) / 255.0F;
        float alpha = (float)(color >> 24 & 255) / 255.0F;

        setColor(red, green, blue, alpha);

        return this;
    }

    public ParticleEffect setAlpha(float a) {
        super.setAlphaF(a);

        return this;
    }

    public ParticleEffect remove() {
        super.setDead();

        return this;
    }
}
