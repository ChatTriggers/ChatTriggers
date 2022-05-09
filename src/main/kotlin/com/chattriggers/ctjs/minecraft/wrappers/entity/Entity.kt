package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.world.Chunk
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.utils.Vec3i
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MCMathHelper
import net.minecraft.util.Vec3
import net.minecraft.world.World
import java.util.*

open class Entity(val entity: MCEntity) {
    fun getX() = entity.posX

    fun getY() = entity.posY

    fun getZ() = entity.posZ

    fun getPos() = Vec3i(getX(), getY(), getZ())

    fun getLastX() = entity.lastTickPosX

    fun getLastY() = entity.lastTickPosY

    fun getLastZ() = entity.lastTickPosZ

    fun getRenderX() = getLastX() + (getX() - getLastX()) * Renderer.partialTicks

    fun getRenderY() = getLastY() + (getY() - getLastY()) * Renderer.partialTicks

    fun getRenderZ() = getLastZ() + (getZ() - getLastZ()) * Renderer.partialTicks

    /**
     * Gets the pitch, the horizontal direction the entity is facing towards.
     * This has a range of -180 to 180.
     *
     * @return the entity's pitch
     */
    fun getPitch(): Double {
        //#if MC<=10809
        return MCMathHelper.wrapAngleTo180_float(entity.rotationPitch).toDouble()
        //#else
        //$$ return MathHelper.wrapDegrees(entity.rotationPitch).toDouble()
        //#endif
    }

    /**
     * Gets the yaw, the vertical direction the entity is facing towards.
     * This has a range of -180 to 180.
     *
     * @return the entity's yaw
     */
    fun getYaw(): Double {
        //#if MC<=10809
        return MCMathHelper.wrapAngleTo180_float(entity.rotationYaw).toDouble()
        //#else
        //$$ return MathHelper.wrapDegrees(entity.rotationYaw).toDouble()
        //#endif
    }

    /**
     * Gets the entity's x motion.
     * This is the amount the entity will move in the x direction next tick.
     *
     * @return the entity's x motion
     */
    fun getMotionX(): Double = entity.motionX

    /**
     * Gets the entity's y motion.
     * This is the amount the entity will move in the y direction next tick.
     *
     * @return the entity's y motion
     */
    fun getMotionY(): Double = entity.motionY

    /**
     * Gets the entity's z motion.
     * This is the amount the entity will move in the z direction next tick.
     *
     * @return the entity's z motion
     */
    fun getMotionZ(): Double = entity.motionZ

    fun getRiding(): Entity? {
        return entity.ridingEntity?.let(::Entity)
    }

    fun getRider(): Entity? {
        //#if MC<=10809
        return entity.riddenByEntity?.let(::Entity)
        //#else
        //$$ return riders.firstOrNull()?.let(::Entity)
        //#endif
    }

    fun getRiders(): List<Entity> {
        //#if MC<=10809
        return emptyList()
        //#else
        //$$ return entity.passengers.map(::Entity)
        //#endif
    }

    /**
     * Checks whether the entity is dead.
     * This is a fairly loose term, dead for a particle could mean it has faded,
     * while dead for an entity means it has no health.
     *
     * @return whether an entity is dead
     */
    fun isDead(): Boolean = entity.isDead

    /**
     * Gets the entire width of the entity's hitbox
     *
     * @return the entity's width
     */
    fun getWidth(): Float = entity.width

    /**
     * Gets the entire height of the entity's hitbox
     *
     * @return the entity's height
     */
    fun getHeight(): Float = entity.height

    /**
     * Gets the height of the eyes on the entity,
     * can be added to its Y coordinate to get the actual Y location of the eyes.
     * This value defaults to 85% of an entity's height, however is different for some entities.
     *
     * @return the height of the entity's eyes
     */
    fun getEyeHeight(): Float = entity.eyeHeight

    /**
     * Gets the name of the entity, could be "Villager",
     * or, if the entity has a custom name, it returns that.
     *
     * @return the (custom) name of the entity
     */
    open fun getName(): String = entity.name

    /**
     * Gets the Java class name of the entity, for example "EntityVillager"
     *
     * @return the entity's class name
     */
    fun getClassName(): String = entity.javaClass.simpleName

    /**
     * Gets the Java UUID object of this entity.
     * Use of [UUID.toString] in conjunction is recommended.
     *
     * @return the entity's uuid
     */
    fun getUUID(): UUID = entity.uniqueID

    /**
     * Gets the entity's air level.
     *
     * The returned value will be an integer. If the player is not taking damage, it
     * will be between 300 (not in water) and 0. If the player is taking damage, it
     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
     *
     * @return the entity's air level
     */
    fun getAir(): Int = entity.air

    fun setAir(air: Int) = apply {
        entity.air = air
    }

    fun distanceTo(other: Entity): Float = distanceTo(other.entity)

    fun distanceTo(other: MCEntity): Float = entity.getDistanceToEntity(other)

    fun distanceTo(blockPos: BlockPos): Float = entity.getDistance(
        blockPos.x.toDouble(),
        blockPos.y.toDouble(),
        blockPos.z.toDouble()
    ).toFloat()

    fun distanceTo(x: Float, y: Float, z: Float): Float = entity.getDistance(
        x.toDouble(),
        y.toDouble(),
        z.toDouble()
    ).toFloat()

    fun isOnGround() = entity.onGround

    fun isCollided() = entity.isCollided

    fun getDistanceWalked() = entity.distanceWalkedModified / 0.6f

    fun getStepHeight() = entity.stepHeight

    fun hasNoClip() = entity.noClip

    fun getTicksExisted() = entity.ticksExisted

    fun getFireResistance() = entity.fireResistance

    fun isImmuneToFire() = entity.isImmuneToFire

    fun isInWater() = entity.isInWater

    fun isWet() = entity.isWet

    fun isAirborne() = entity.isAirBorne

    fun getDimension() = entity.dimension

    fun setPosition(x: Double, y: Double, z: Double) = apply {
        entity.setPosition(x, y, z)
    }

    fun setAngles(yaw: Float, pitch: Float) = apply {
        entity.setAngles(yaw, pitch)
    }

    fun getMaxInPortalTime() = entity.maxInPortalTime

    fun setOnFire(seconds: Int) = apply {
        entity.setFire(seconds)
    }

    fun extinguish() = apply {
        entity.extinguish()
    }

    fun move(x: Double, y: Double, z: Double) = apply {
        entity.moveEntity(x, y, z)
    }

    fun isSilent() = entity.isSilent

    fun setIsSilent(silent: Boolean) = apply {
        entity.isSilent = silent
    }

    fun isInLava() = entity.isInLava

    fun addVelocity(x: Double, y: Double, z: Double) = apply {
        entity.addVelocity(x, y, z)
    }

    fun getLookVector(partialTicks: Float): Vec3 = entity.getLook(partialTicks)

    fun getEyePosition(partialTicks: Float): Vec3 = entity.getPositionEyes(partialTicks)

    fun canBeCollidedWith() = entity.canBeCollidedWith()

    fun canBePushed() = entity.canBePushed()

    fun dropItem(item: Item, size: Int) = entity.dropItem(item.item, size)

    fun isSneaking() = entity.isSneaking

    fun setIsSneaking(sneaking: Boolean) = apply {
        entity.isSneaking = sneaking
    }

    fun isSprinting() = entity.isSprinting

    fun setIsSprinting(sprinting: Boolean) = apply {
        entity.isSprinting = sprinting
    }

    fun isInvisible() = entity.isInvisible

    fun setIsInvisible(invisible: Boolean) = apply {
        entity.isInvisible = invisible
    }

    fun isEating() = entity.isEating

    fun setIsEating(eating: Boolean) = apply {
        entity.isEating = eating
    }

    fun isOutsideBorder() = entity.isOutsideBorder

    fun setIsOutsideBorder(outside: Boolean) = apply {
        entity.isOutsideBorder = outside
    }

    fun isBurning(): Boolean = entity.isBurning

    fun getWorld(): World = entity.entityWorld

    fun getChunk(): Chunk = Chunk(
        getWorld().getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ)
    )

    override fun toString(): String {
        return "Entity{name=${getName()}, x=${getX()}, y=${getY()}, z=${getZ()}}"
    }
}
