package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(
        method = "onDeath",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;"
        )
    )
    public void injectSetHealth(DamageSource source, CallbackInfo ci) {
        TriggerType.EntityDeath.triggerAll(this);
    }

    @Inject(
        method = "damage",
        at = @At("TAIL")
    )
    public void injectDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        TriggerType.EntityDamage.triggerAll(this);
    }
}
