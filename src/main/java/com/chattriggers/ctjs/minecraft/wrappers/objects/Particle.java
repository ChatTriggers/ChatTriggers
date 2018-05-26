package com.chattriggers.ctjs.minecraft.wrappers.objects;

import com.chattriggers.ctjs.minecraft.mixins.MixinEntityFX;
import lombok.Getter;
import net.minecraft.client.particle.EntityFX;

public class Particle {
    @Getter
    private EntityFX underlyingEntity;

    public Particle(EntityFX entityFX) {
        if (entityFX == null) {
            throw new NullPointerException("EntityFX is null!");
        }

        this.underlyingEntity = entityFX;
    }

    public void scale(float scale) {
        this.underlyingEntity.multipleParticleScaleBy(scale);
    }

    public void multiplyVelocity(float multiplier) {
        this.underlyingEntity.multiplyVelocity(multiplier);
    }

    public void setColor(float r, float g, float b) {
        this.underlyingEntity.setRBGColorF(r, g, b);
    }

    public void setColor(float r, float g, float b, float a) {
        setColor(r, g, b);
        setAlpha(a);
    }

    public void setColor(int color) {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float blue = (float) (color >> 8 & 255) / 255.0F;
        float green = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;

        setColor(red, green, blue, alpha);
    }

    public void setAlpha(float a) {
        this.underlyingEntity.setAlphaF(a);
    }

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particles max age (in ticks)
     */
    public void setMaxAge(int maxAge) {
        ((MixinEntityFX) this.underlyingEntity).setParticleMaxAge(maxAge);
    }

    public void remove() {
        this.underlyingEntity.setDead();
    }
}
