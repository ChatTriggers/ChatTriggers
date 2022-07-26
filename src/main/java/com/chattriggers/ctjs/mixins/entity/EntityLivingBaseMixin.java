package com.chattriggers.ctjs.mixins.entity;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC>=11701
//$$ import net.minecraft.world.entity.EntityType;
//#endif

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {
    //#if MC<=11202
    public EntityLivingBaseMixin(World worldIn) {
        super(worldIn);
    }
    //#elseif MC>=11701
    //$$ public EntityLivingBaseMixin(EntityType<?> arg, Level arg2) {
    //$$     super(arg, arg2);
    //$$ }
    //#endif

    @Inject(
            //#if MC<=11202
            method = "attackEntityFrom",
            //#elseif MC>=11701
            //$$ method = "hurt",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC<=11202
                    target = "Lnet/minecraft/entity/EntityLivingBase;onDeath(Lnet/minecraft/util/DamageSource;)V"
                    //#elseif MC>=11701
                    //$$ target = "Lnet/minecraft/world/entity/LivingEntity;die(Lnet/minecraft/world/damagesource/DamageSource;)V"
                    //#endif
            )
    )
    private void chattriggers_entityDeathTrigger(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        TriggerType.EntityDeath.triggerAll(
                new com.chattriggers.ctjs.minecraft.wrappers.entity.Entity(this)
        );
    }
}
