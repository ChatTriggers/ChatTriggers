package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCParticle
import java.awt.Color

//#if MC>10809
//$$import com.chattriggers.ctjs.minecraft.mixins.MixinParticle
//#endif

@External
class Particle(val underlyingEntity: MCParticle) : Entity(underlyingEntity) {
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
        underlyingEntity.multipleParticleScaleBy(scale)
    }

    fun multiplyVelocity(multiplier: Float) = apply {
        underlyingEntity.multiplyVelocity(multiplier)
    }

    /**
     * Sets the color of the particle.
     * @param red the red value between 0 and 1.
     * @param green the green value between 0 and 1.
     * @param blue the blue value between 0 and 1.
     */
    fun setColor(red: Float, green: Float, blue: Float) = apply {
        underlyingEntity.setRBGColorF(red, green, blue)
    }

    /**
     * Sets the color of the particle.
     * @param red the red value between 0 and 1.
     * @param green the green value between 0 and 1.
     * @param blue the blue value between 0 and 1.
     * @param alpha the alpha value between 0 and 1.
     */
    fun setColor(red: Float, green: Float, blue: Float, alpha: Float) = apply {
        setColor(red, green, blue)
        setAlpha(alpha)
    }

    fun setColor(color: Long) = apply {
        val red = (color shr 16 and 255).toFloat() / 255.0f
        val blue = (color shr 8 and 255).toFloat() / 255.0f
        val green = (color and 255).toFloat() / 255.0f
        val alpha = (color shr 24 and 255).toFloat() / 255.0f

        setColor(red, green, blue, alpha)
    }

    /**
     * Sets the alpha of the particle.
     * @param alpha the alpha value between 0 and 1.
     */
    fun setAlpha(alpha: Float) = apply {
        underlyingEntity.setAlphaF(alpha)
    }

    /**
     * Returns the color of the Particle
     *
     * @return A [java.awt.Color] with the R, G, B and A values
     */
    fun getColor(): Color = Color(
        underlyingEntity.redColorF,
        underlyingEntity.greenColorF,
        underlyingEntity.blueColorF,
        underlyingEntity.alpha
    )

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particle's max age (in ticks)
     */
    fun setMaxAge(maxAge: Int) = apply {
        underlyingEntity.particleMaxAge = maxAge
    }

    fun remove() = apply {
        //#if MC<=10809
        underlyingEntity.setDead()
        //#else
        //$$ underlyingEntity.setExpired()
        //#endif
    }

    override fun toString(): String {
        return underlyingEntity.toString()
    }
}
