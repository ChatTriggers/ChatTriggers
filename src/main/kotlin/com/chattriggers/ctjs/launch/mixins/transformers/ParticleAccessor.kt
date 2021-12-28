package com.chattriggers.ctjs.launch.mixins.transformers

import net.minecraft.client.particle.Particle
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor
import org.spongepowered.asm.mixin.gen.Invoker

@Mixin(Particle::class)
interface ParticleAccessor {
    @Accessor
    fun getX(): Double

    @Accessor
    fun getY(): Double

    @Accessor
    fun getZ(): Double

    @Accessor
    fun getVelocityX(): Double

    @Accessor
    fun getVelocityY(): Double

    @Accessor
    fun getVelocityZ(): Double

    @Invoker
    fun invokeSetColorAlpha(alpha: Float)
}
