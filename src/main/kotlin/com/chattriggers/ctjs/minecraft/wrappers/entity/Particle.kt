package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.utils.Vec3i
import com.chattriggers.ctjs.minecraft.wrappers.world.Chunk
import com.chattriggers.ctjs.utils.kotlin.asMixin
import net.minecraft.client.particle.EntityFX
import java.awt.Color

//#if MC<=11202
import com.chattriggers.ctjs.mixins.entity.EntityFXAccessor
//#elseif MC>=11701
//$$ import com.chattriggers.ctjs.mixins.ParticleAccessor
//$$ import kotlin.math.max
//#endif

// TODO(BREAKING): No longer extends entity (particles in newer versions don't inherit from Entity)
class Particle(val entity: EntityFX) {
    fun scale(scale: Float) = apply {
        //#if MC<=11202
        entity.multipleParticleScaleBy(scale)
        //#else
        //$$ entity.scale(scale)
        //#endif
    }

    fun multiplyVelocity(multiplier: Float) = apply {
        //#if MC<=11202
        entity.multiplyVelocity(multiplier)
        //#else
        //$$ entity.setParticleSpeed(getMotionX() * multiplier, getMotionY() * multiplier, getMotionZ() * multiplier)
        //#endif
    }

    /**
     * Sets the color of the particle.
     * @param red the red value between 0 and 1.
     * @param green the green value between 0 and 1.
     * @param blue the blue value between 0 and 1.
     */
    fun setColor(red: Float, green: Float, blue: Float) = apply {
        //#if MC<=11202
        entity.setRBGColorF(red, green, blue)
        //#else
        //$$ entity.setColor(red, green, blue)
        //#endif
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
        //#if MC<=11202
        entity.setAlphaF(alpha)
        //#else
        //$$ entity.asMixin<ParticleAccessor>().setAlpha(alpha)
        //#endif
    }

    /**
     * Returns the color of the Particle
     *
     * @return A [java.awt.Color] with the R, G, B and A values
     */
    fun getColor(): Color = Color(
        //#if MC<=11202
        entity.redColorF,
        entity.greenColorF,
        entity.blueColorF,
        entity.alpha,
        //#else
        //$$ entity.asMixin<ParticleAccessor>().rCol,
        //$$ entity.asMixin<ParticleAccessor>().gCol,
        //$$ entity.asMixin<ParticleAccessor>().bCol,
        //$$ entity.asMixin<ParticleAccessor>().alpha,
        //#endif
    )

    /**
     * Gets the amount of ticks this particle will live for
     *
     * @return the particle's max age (in ticks)
     */
    fun getMaxAge(): Int {
        //#if MC<=11202
        return entity.asMixin<EntityFXAccessor>().particleMaxAge
        //#else
        //$$ return entity.asMixin<ParticleAccessor>().lifetime
        //#endif
    }

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particle's max age (in ticks)
     */
    fun setMaxAge(maxAge: Int) = apply {
        //#if MC<=11202
        entity.asMixin<EntityFXAccessor>().particleMaxAge = maxAge
        //#else
        //$$ entity.asMixin<ParticleAccessor>().lifetime = maxAge
        //#endif
    }

    fun remove() = apply {
        //#if MC<=11202
        entity.setDead()
        //#else
        //$$ entity.remove()
        //#endif
    }

    /**************************
     * GENERIC ENTITY METHODS *
     **************************/

    fun getPos() = Vec3i(getX(), getY(), getZ())

    fun getRenderX() = getLastX() + (getX() - getLastX()) * Renderer.partialTicks

    fun getRenderY() = getLastY() + (getY() - getLastY()) * Renderer.partialTicks

    fun getRenderZ() = getLastZ() + (getZ() - getLastZ()) * Renderer.partialTicks

    fun isAirborne() = !isOnGround()

    fun setPosition(x: Double, y: Double, z: Double) = apply {
        setX(x)
        setY(y)
        setZ(z)
    }

    fun move(x: Double, y: Double, z: Double) = apply {
        setX(getX() + x)
        setY(getY() + y)
        setZ(getZ() + z)
    }

    //#if MC<=11202
    fun getX() = entity.posX

    fun getY() = entity.posY

    fun getZ() = entity.posZ

    fun setX(x: Double) = apply { entity.posX = x }

    fun setY(y: Double) = apply { entity.posY = y }

    fun setZ(z: Double) = apply { entity.posZ = z }

    fun getLastX() = entity.lastTickPosX

    fun getLastY() = entity.lastTickPosY

    fun getLastZ() = entity.lastTickPosZ

    fun getMotionX() = entity.motionX

    fun getMotionY() = entity.motionY

    fun getMotionZ() = entity.motionZ

    fun isOnGround() = entity.onGround

    fun hasNoClip() = entity.noClip

    fun getTicksExisted() = entity.ticksExisted

    fun isDead() = entity.isDead

    fun getWidth() = entity.width

    fun getHeight() = entity.height

    fun getName() = entity.name

    fun addVelocity(x: Double, y: Double, z: Double) = apply {
        entity.addVelocity(x, y, z)
    }

    fun getWorld() = entity.worldObj

    fun getChunk() = Chunk(getWorld().getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ))
    //#else
    //$$ fun getX() = entity.asMixin<ParticleAccessor>().x
    //$$
    //$$ fun getY() = entity.asMixin<ParticleAccessor>().y
    //$$
    //$$ fun getZ() = entity.asMixin<ParticleAccessor>().z
    //$$
    //$$ fun setX(x: Double) = apply { entity.asMixin<ParticleAccessor>().x = x }
    //$$
    //$$ fun setY(y: Double) = apply { entity.asMixin<ParticleAccessor>().y = y }
    //$$
    //$$ fun setZ(z: Double) = apply { entity.asMixin<ParticleAccessor>().z = z }
    //$$
    //$$ fun getLastX() = entity.asMixin<ParticleAccessor>().lastTickPosX
    //$$
    //$$ fun getLastY() = entity.asMixin<ParticleAccessor>().lastTickPosY
    //$$
    //$$ fun getLastZ() = entity.asMixin<ParticleAccessor>().lastTickPosZ
    //$$
    //$$ fun getMotionX() = entity.asMixin<ParticleAccessor>().motionX
    //$$
    //$$ fun getMotionY() = entity.asMixin<ParticleAccessor>().motionY
    //$$
    //$$ fun getMotionZ() = entity.asMixin<ParticleAccessor>().motionZ
    //$$
    //$$ fun isOnGround() = entity.asMixin<ParticleAccessor>().onGround
    //$$
    //$$ fun hasNoClip() = !entity.asMixin<ParticleAccessor>().hasPhysics
    //$$
    //$$ fun getTicksExisted() = entity.asMixin<ParticleAccessor>().age
    //$$
    //$$ fun isDead() = entity.asMixin<ParticleAccessor>().isRemoved
    //$$
    //$$ fun getWidth() = max(entity.boundingBox.xsize, entity.boundingBox.zsize)
    //$$
    //$$ fun getHeight() = entity.boundingBox.ysize
    //$$
    //$$ // TODO(BREAKING) Removed this as it always returned "unknown"
    //$$ //fun getName() {}
    //$$
    //$$ fun addVelocity(x: Double, y: Double, z: Double) = apply {
    //$$     entity.asMixin<ParticleAccessor>().motionX += x
    //$$     entity.asMixin<ParticleAccessor>().motionY += y
    //$$     entity.asMixin<ParticleAccessor>().motionZ += z
    //$$ }
    //$$
    //$$ fun getWorld() = entity.asMixin<ParticleAccessor>().level
    //$$
    //$$ fun getChunk() = Chunk(getWorld().getChunk(getX().toInt(), getZ().toInt()))
    //#endif

    override fun toString(): String {
        return entity.toString()
    }
}
