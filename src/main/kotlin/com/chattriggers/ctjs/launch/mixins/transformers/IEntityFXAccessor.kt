package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.particle.EntityFX
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor

@Mixin(EntityFX::class)
interface IEntityFXAccessor {
    //#if MC==11602
    //$$ @Accessor("maxAge")
    //#else
    @Accessor()
    //#endif
    fun setParticleMaxAge(maxAge: Int)
}

fun EntityFX.asMixinAccessor() = this as IEntityFXAccessor
