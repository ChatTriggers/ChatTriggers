package com.chattriggers.ctjs.launch.mixins.transformers.render;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderGlobal.class)
public interface RenderGlobalAccessor {
    @Invoker
    EntityFX invokeSpawnEntityFX(
        int particleId,
        boolean ignoreRange,
        double xCoord,
        double yCoord,
        double zCoord,
        double xSpeed,
        double ySpeed,
        double zSpeed,
        int... extraArgs
    );
}
