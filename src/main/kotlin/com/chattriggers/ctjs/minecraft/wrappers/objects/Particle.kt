package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.External

internal typealias MCParticle = net.minecraft.client.particle.EntityFX

@External
class Particle(val underlyingEntity: MCParticle) {
    // TODO(1.16.2)
    //#if MC==11602
    //$$fun getX() = 0.0
    //$$fun getY() = 0.0
    //$$fun getZ() = 0.0
    //#else
    fun getX() = underlyingEntity.posX

    fun getY() = underlyingEntity.posY

    fun getZ() = underlyingEntity.posZ
    //#endif

    fun setX(x: Double) = apply {
        underlyingEntity.setPosition(x, getY(), getZ())
    }

    fun setY(y: Double) = apply {
        underlyingEntity.setPosition(getX(), y, getZ())
    }

    fun setZ(z: Double) = apply {
        underlyingEntity.setPosition(getX(), getY(), z)
    }

    fun scale(scale: Float) = apply {
        this.underlyingEntity.multipleParticleScaleBy(scale)
    }

    fun multiplyVelocity(multiplier: Float) = apply {
        this.underlyingEntity.multiplyVelocity(multiplier)
    }

    fun setColor(r: Float, g: Float, b: Float) = apply {
        this.underlyingEntity.setRBGColorF(r, g, b)
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
        // TODO(1.16.2)
        //#if MC==10809
        this.underlyingEntity.setAlphaF(a)
        //#endif
    }

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particles max age (in ticks)
     */
    fun setMaxAge(maxAge: Int) = apply {
        this.underlyingEntity.particleMaxAge = maxAge
    }

    fun remove() = apply {
        //#if MC<=10809
        this.underlyingEntity.setDead()
        //#else
        //$$ this.underlyingEntity.setExpired()
        //#endif
    }

    override fun toString(): String {
        return underlyingEntity.toString()
    }
}
