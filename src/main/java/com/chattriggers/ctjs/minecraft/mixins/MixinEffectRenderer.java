package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.wrappers.objects.Particle;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.EnumParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(EffectRenderer.class)
public class MixinEffectRenderer {
    @Inject(
            method = "spawnEffectParticle(IDDDDDD[I)Lnet/minecraft/client/particle/EntityFX;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/EffectRenderer;addEffect(Lnet/minecraft/client/particle/EntityFX;)V"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onSpawnEffectParticle(int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int parameters[], CallbackInfoReturnable<EntityFX> cir, IParticleFactory iparticlefactory, EntityFX entityfx) {
        TriggerType.SPAWN_PARTICLE.triggerAll(
                new Particle(entityfx),
                EnumParticleTypes.getParticleFromId(particleId),
                cir
        );
    }
}
