package com.chattriggers.ctjs.launch.mixins.transformers.render;


import com.chattriggers.ctjs.minecraft.listeners.events.CancellableEvent;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//#if MC<=11202
import net.minecraft.client.particle.IParticleFactory;
//#elseif MC>=11701
//$$ import net.minecraft.core.particles.ParticleOptions;
//#endif

@Mixin(EffectRenderer.class)
public class EffectRendererMixin {
    //TODO(BREAKING): Removed EnumParticleTypes parameter
    //#if MC<=11202
    @Inject(
            method = "spawnEffectParticle",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/particle/EffectRenderer;addEffect(Lnet/minecraft/client/particle/EntityFX;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    @SuppressWarnings("InvalidInjectorMethodSignature")
    private void chattriggers_spawnParticleTrigger(int particleId, double xCoord, double yCoord, double zCoord, double xSpeed, double p_178927_10_, double p_178927_12_, int[] p_178927_14_, CallbackInfoReturnable<EntityFX> cir, IParticleFactory iparticlefactory, EntityFX entityfx) {
    //#elseif MC>=11701
    //$$ @Inject(
    //$$         method = "createParticle",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/client/particle/ParticleEngine;add(Lnet/minecraft/client/particle/Particle;)V"
    //$$         ),
    //$$         locals = LocalCapture.CAPTURE_FAILHARD,
    //$$         cancellable = true
    //$$ )
    //$$ private void chattriggers_spawnParticleTrigger(ParticleOptions arg, double d, double e, double f, double g, double h, double i, CallbackInfoReturnable<Particle> cir, Particle entityfx) {
    //#endif
        CancellableEvent event = new CancellableEvent();
        TriggerType.SpawnParticle.triggerAll(
            new com.chattriggers.ctjs.minecraft.wrappers.entity.Particle(entityfx),
            event
        );

        if (event.isCanceled())
            cir.setReturnValue(null);
    }
}
