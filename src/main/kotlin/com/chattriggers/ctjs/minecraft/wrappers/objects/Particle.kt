package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.launch.mixins.asMixin
import com.chattriggers.ctjs.launch.mixins.transformers.ParticleAccessor
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCParticle

//#if MC>10809
//$$import com.chattriggers.ctjs.minecraft.mixins.MixinParticle
//#endif

@External
class Particle(val underlyingEntity: MCParticle) {
    fun getX() = underlyingEntity.asMixin<ParticleAccessor>().x

    fun getY() = underlyingEntity.asMixin<ParticleAccessor>().y

    fun getZ() = underlyingEntity.asMixin<ParticleAccessor>().z

    fun setX(x: Double) = apply {
        underlyingEntity.setPos(x, getY(), getZ())
    }

    fun setY(y: Double) = apply {
        underlyingEntity.setPos(getX(), y, getZ())
    }

    fun setZ(z: Double) = apply {
        underlyingEntity.setPos(getX(), getY(), z)
    }

    fun scale(scale: Float) = apply {
        underlyingEntity.scale(scale)
    }

    fun multiplyVelocity(multiplier: Float) = apply {
        val accessor = underlyingEntity.asMixin<ParticleAccessor>()
        underlyingEntity.setVelocity(
            accessor.velocityX * multiplier,
            accessor.velocityY * multiplier,
            accessor.velocityZ * multiplier,
        )
    }

    fun setColor(r: Float, g: Float, b: Float) = apply {
        underlyingEntity.setColor(r, g, b)
    }

    fun setColor(r: Float, g: Float, b: Float, a: Float) = apply {
        setColor(r, g, b)
        setAlpha(a)
    }

    fun setColor(color: Long) = apply {
        val red = (color shr 16 and 255).toFloat() / 255.0f
        val blue = (color shr 8 and 255).toFloat() / 255.0f
        val green = (color and 255).toFloat() / 255.0f
        val alpha = (color shr 24 and 255).toFloat() / 255.0f

        setColor(red, green, blue, alpha)
    }

    fun setAlpha(a: Float) = apply {
        underlyingEntity.asMixin<ParticleAccessor>().invokeSetColorAlpha(a)
    }

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particle's max age (in ticks)
     */
    fun setMaxAge(maxAge: Int) = apply {
        underlyingEntity.maxAge = maxAge
    }

    fun remove() = apply {
        underlyingEntity.markDead()
    }

    override fun toString(): String {
        return underlyingEntity.toString()
    }
}
