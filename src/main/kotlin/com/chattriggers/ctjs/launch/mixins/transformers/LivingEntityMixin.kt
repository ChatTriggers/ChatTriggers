package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

@Mixin(LivingEntity::class)
class LivingEntityMixin {
    @Inject(
        method = ["onDeath"],
        at = [At(
            value = "INVOKE",
            target = "net/minecraft/entity/damage/DamageSource.getAttacker()Lnet/minecraft/entity/Entity;",
        )],
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun injectSetHealth(entity: LivingEntity, source: DamageSource) {
        TriggerType.EntityDeath.triggerAll(entity)
    }

    @Inject(
        method = ["damage"],
        at = [At("TAIL")],
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    fun injectDamage(entity: LivingEntity) {
        TriggerType.EntityDamage.triggerAll(entity)
    }
}
