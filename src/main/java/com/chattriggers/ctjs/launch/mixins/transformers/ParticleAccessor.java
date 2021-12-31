package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor
    double getX();

    @Accessor
    double getY();

    @Accessor
    double getZ();

    @Accessor
    double getVelocityX();

    @Accessor
    double getVelocityY();

    @Accessor
    double getVelocityZ();

    @Invoker
    void invokeSetColorAlpha(float alpha);
}
