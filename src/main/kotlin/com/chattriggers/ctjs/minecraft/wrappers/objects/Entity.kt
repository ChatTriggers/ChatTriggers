package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.minecraft.libs.Tessellator
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockFace
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Vec3i
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MCMathHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.Vec3
import net.minecraft.world.World
import java.util.*

@External
open class Entity(val entity: MCEntity) {
    fun getX() = entity.posX

    fun getY() = entity.posY

    fun getZ() = entity.posZ

    fun getPos() = Vec3i(getX(), getY(), getZ())

    fun getLastX() = entity.lastTickPosX

    fun getLastY() = entity.lastTickPosY

    fun getLastZ() = entity.lastTickPosZ

    fun getRenderX() = getLastX() + (getX() - getLastX()) * Tessellator.partialTicks

    fun getRenderY() = getLastY() + (getY() - getLastY()) * Tessellator.partialTicks

    fun getRenderZ() = getLastZ() + (getZ() - getLastZ()) * Tessellator.partialTicks

    var face: BlockFace? = null

    /**
     * Gets the pitch, the horizontal direction the entity is facing towards.
     * This has a range of -180 to 180.
     *
     * @return the entity's pitch
     */
    fun getPitch(): Double {
        //#if MC<=10809
        return MCMathHelper.wrapAngleTo180_float(this.entity.rotationPitch).toDouble()
        //#else
        //$$ return MathHelper.wrapDegrees(this.entity.rotationPitch).toDouble()
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
        return MCMathHelper.wrapAngleTo180_float(this.entity.rotationYaw).toDouble()
        //#else
        //$$ return MathHelper.wrapDegrees(this.entity.rotationYaw).toDouble()
        //#endif
    }

    /**
     * Gets the entity's x motion.
     * This is the amount the entity will move in the x direction next tick.
     *
     * @return the player's x motion
     */
    fun getMotionX(): Double = this.entity.motionX

    /**
     * Gets the entity's y motion.
     * This is the amount the entity will move in the y direction next tick.
     *
     * @return the player's y motion
     */
    fun getMotionY(): Double = this.entity.motionY

    /**
     * Gets the entity's z motion.
     * This is the amount the entity will move in the z direction next tick.
     *
     * @return the player's z motion
     */
    fun getMotionZ(): Double = this.entity.motionZ

    /**
     * Gets the entity's health, -1 if not a living entity
     *
     * @return the entity's health
     */
    fun getHP(): Float {
        return if (this.entity is EntityLivingBase) {
            this.entity.health
        } else -1f
    }

    fun getMaxHP(): Float {
        return if (this.entity is EntityLivingBase) {
            this.entity.maxHealth
        } else -1f
    }

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
        //#elseif
        //$$ return this.entity.passengers.map(::Entity)
        //#endif
    }

    /**
     * Checks whether or not the entity is dead.
     * This is a fairly loose term, dead for a particle could mean it has faded,
     * while dead for an entity means it has no health.
     *
     * @return whether or not an entity is dead
     */
    fun isDead(): Boolean = this.entity.isDead

    /**
     * Gets the entire width of the entity's hitbox
     *
     * @return the entity's width
     */
    fun getWidth(): Float = this.entity.width

    /**
     * Gets the entire height of the entity's hitbox
     *
     * @return the entity's height
     */
    fun getHeight(): Float = this.entity.height

    /**
     * Gets the height of the eyes on the entity,
     * can be added to its Y coordinate to get the actual Y location of the eyes.
     * This value defaults to 85% of an entity's height, however is different for some entities.
     *
     * @return the height of the entity's eyes
     */
    fun getEyeHeight(): Float = this.entity.eyeHeight

    /**
     * Gets the name of the entity, could be "Villager",
     * or, if the entity has a custom name, it returns that.
     *
     * @return the (custom) name of the entity
     */
    open fun getName(): String = this.entity.name

    /**
     * Gets the Java class name of the entity, for example "EntityVillager"
     *
     * @return the entity's class name
     */
    fun getClassName(): String = this.entity.javaClass.simpleName

    /**
     * Gets the Java UUID object of this entity.
     * Use of [UUID.toString] in conjunction is recommended.
     *
     * @return the entity's uuid
     */
    fun getUUID(): UUID = this.entity.uniqueID

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

    fun getDistanceWalked() = entity.distanceWalkedModified / 0.6

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

    fun getWorld(): World = entity.entityWorld

    override fun toString(): String {
        return "Entity{name=${getName()}, x=${getX()}, y=${getY()}, z=${getZ()}}"
    }
}
