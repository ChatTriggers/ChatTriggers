package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Invoker
    Particle invokeSpawnParticle(
        ParticleEffect parameters,
        boolean alwaysSpawn,
        boolean canSpawnOnMinimal,
        double x,
        double y,
        double z,
        double velocityX,
        double velocityY,
        double velocityZ
    );
}
