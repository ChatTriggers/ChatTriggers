package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCParticle

//#if MC>10809
//$$import com.chattriggers.ctjs.minecraft.mixins.MixinParticle
//#endif

@External
class Particle(val underlyingEntity: MCParticle) {
    //#if MC<=10809
    fun getX() = underlyingEntity.posX

    fun getY() = underlyingEntity.posY

    fun getZ() = underlyingEntity.posZ
    //#else
    //$$fun getX() = (underlyingEntity as MixinParticle).posX
    //$$
    //$$fun getY() = (underlyingEntity as MixinParticle).posY
    //$$
    //$$fun getZ() = (underlyingEntity as MixinParticle).posZ
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
        this.underlyingEntity.setAlphaF(a)
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
