package com.chattriggers.ctjs.mixins.entity;

import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC<=11202
@Mixin(EntityFX.class)
public interface EntityFXAccessor {
    @Accessor
    int getParticleMaxAge();

    @Accessor
    void setParticleMaxAge(int age);
}
//#endif
