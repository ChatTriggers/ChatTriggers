package com.chattriggers.ctjs.launch.mixins.transformers.render;

//#if MC<=11202
import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent;
import com.chattriggers.ctjs.minecraft.wrappers.entity.Particle;
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
public class EffectRendererMixin {
    @Inject(
        method = "spawnEffectParticle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/particle/EffectRenderer;addEffect(Lnet/minecraft/client/particle/EntityFX;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    void injectSpawnEffectParticle(
        int particleId,
        double xCoord,
        double yCoord,
        double zCoord,
        double xSpeed,
        double p_178927_10_,
        double p_178927_12_,
        int[] p_178927_14_,
        CallbackInfoReturnable<EntityFX> cir,
        IParticleFactory iparticlefactory,
        EntityFX entityfx
    ) {
        CancellableEvent event = new CancellableEvent();
        TriggerType.SpawnParticle.triggerAll(
            new Particle(entityfx),
            EnumParticleTypes.getParticleFromId(particleId),
            event
        );

        if (event.isCanceled())
            cir.setReturnValue(null);
    }
}
//#endif
