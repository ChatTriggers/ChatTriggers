package com.chattriggers.ctjs.minecraft.wrappers.objects;

import com.chattriggers.ctjs.utils.console.Console;
import lombok.Getter;
import net.minecraft.client.particle.EntityFX;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Particle {
    @Getter
    private EntityFX underlyingEntity;

    public Particle(EntityFX entityFX) {
        this.underlyingEntity = entityFX;
    }

    public void scale(float scale) {
        if (underlyingEntity == null) {
            Console.getConsole().out.println("Particle is null, returning!");
            return;
        }

        this.underlyingEntity.multipleParticleScaleBy(scale);
    }

    public void multiplyVelocity(float multiplier) {
        if (underlyingEntity == null) {
            Console.getConsole().out.println("Particle is null, returning!");
            return;
        }

        this.underlyingEntity.multiplyVelocity(multiplier);
    }

    public void setColor(float r, float g, float b) {
        if (underlyingEntity == null) {
            Console.getConsole().out.println("Particle is null, returning!");
            return;
        }

        this.underlyingEntity.setRBGColorF(r, g, b);
    }

    public void setColor(float r, float g, float b, float a) {
        if (underlyingEntity == null) {
            Console.getConsole().out.println("Particle is null, returning!");
            return;
        }

        setColor(r, g, b);
        setAlpha(a);
    }

    public void setColor(int color) {
        if (underlyingEntity == null) {
            Console.getConsole().out.println("Particle is null, returning!");
            return;
        }

        float red = (float) (color >> 16 & 255) / 255.0F;
        float blue = (float) (color >> 8 & 255) / 255.0F;
        float green = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;

        setColor(red, green, blue, alpha);
    }

    public void setAlpha(float a) {
        if (underlyingEntity == null) {
            Console.getConsole().out.println("Particle is null, returning!");
            return;
        }

        this.underlyingEntity.setAlphaF(a);
    }

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particles max age (in ticks)
     */
    public void setMaxAge(int maxAge) {
        if (underlyingEntity == null) {
            Console.getConsole().out.println("Particle is null, returning!");
            return;
        }

        ReflectionHelper.setPrivateValue(
                EntityFX.class,
                this.underlyingEntity,
                maxAge,
                new String[]{
                        "particleMaxAge",
                        "field_70547_e"
                }
        );
    }

    public void remove() {
        if (underlyingEntity == null) {
            Console.getConsole().out.println("Particle is null, returning!");
            return;
        }

        this.underlyingEntity.setDead();
    }
}
