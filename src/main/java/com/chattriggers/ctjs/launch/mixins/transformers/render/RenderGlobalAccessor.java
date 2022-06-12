package com.chattriggers.ctjs.launch.mixins.transformers.render;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//#if MC>=11701
//$$ import net.minecraft.core.particles.ParticleOptions;
//#endif

@Mixin(RenderGlobal.class)
public interface RenderGlobalAccessor {
    @Invoker
    //#if MC<=11202
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
    //#elseif MC>=11701
    //$$ Particle invokeAddParticleInternal(
    //$$     ParticleOptions arg,
    //$$     boolean bl,
    //$$     boolean bl2,
    //$$     double d,
    //$$     double e,
    //$$     double f,
    //$$     double g,
    //$$     double h,
    //$$     double i
    //$$ );
    //#endif
}
