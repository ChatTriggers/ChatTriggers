package com.chattriggers.ctjs.minecraft.wrappers.objects.entity

import com.chattriggers.ctjs.minecraft.libs.Tessellator
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Vec3i
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import gg.essential.universal.utils.MCWorld
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.MovementType
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.math.sqrt

@External
open class Entity(val entity: MCEntity?) {
    fun getX() = entity?.pos?.x ?: 0.0

    fun getY() = entity?.pos?.y ?: 0.0

    fun getZ() = entity?.pos?.z ?: 0.0

    fun getPos() = Vec3i(getX(), getY(), getZ())

    fun getLastX() = entity?.lastRenderX ?: 0.0

    fun getLastY() = entity?.lastRenderY ?: 0.0

    fun getLastZ() = entity?.lastRenderZ ?: 0.0

    fun getRenderX() = getLastX() + (getX() - getLastX()) * Tessellator.partialTicks

    fun getRenderY() = getLastY() + (getY() - getLastY()) * Tessellator.partialTicks

    fun getRenderZ() = getLastZ() + (getZ() - getLastZ()) * Tessellator.partialTicks

    /**
     * Gets the pitch, the horizontal direction the entity is facing towards.
     * This has a range of -180 to 180.
     *
     * @return the entity's pitch
     */
    fun getPitch() = MathHelper.wrapDegrees(entity?.pitch ?: 0f).toDouble()

    /**
     * Gets the yaw, the vertical direction the entity is facing towards.
     * This has a range of -180 to 180.
     *
     * @return the entity's yaw
     */
    fun getYaw() = MathHelper.wrapDegrees(entity?.yaw ?: 0f).toDouble()

    /**
     * Gets the entity's x motion.
     * This is the amount the entity will move in the x direction next tick.
     *
     * @return the entity's x motion
     */
    fun getMotionX(): Double = entity?.velocity?.x ?: 0.0

    /**
     * Gets the entity's y motion.
     * This is the amount the entity will move in the y direction next tick.
     *
     * @return the entity's y motion
     */
    fun getMotionY(): Double = entity?.velocity?.y ?: 0.0

    /**
     * Gets the entity's z motion.
     * This is the amount the entity will move in the z direction next tick.
     *
     * @return the entity's z motion
     */
    fun getMotionZ(): Double = entity?.velocity?.z ?: 0.0

    fun getRiding(): Entity? = entity?.rootVehicle?.let(::Entity)

    fun getRider(): Entity? = entity?.firstPassenger?.let(::Entity)

    fun getRiders(): List<Entity> = entity?.passengerList?.map(::Entity) ?: emptyList()

    /**
     * Checks whether the entity is dead.
     * This is a fairly loose term, dead for a particle could mean it has faded,
     * while dead for an entity means it has no health.
     *
     * @return whether an entity is dead
     */
    fun isDead() = entity?.isAlive?.not() ?: false

    /**
     * Gets the entire width of the entity's hitbox
     *
     * @return the entity's width
     */
    fun getWidth() = entity?.width ?: 0f

    /**
     * Gets the entire height of the entity's hitbox
     *
     * @return the entity's height
     */
    fun getHeight() = entity?.height ?: 0f

    /**
     * Gets the height of the eyes on the entity,
     * can be added to its Y coordinate to get the actual Y location of the eyes.
     *
     * @return the height of the entity's eyes
     */
    fun getEyeHeight() = entity?.pose?.let { entity.getEyeHeight(it) } ?: 0f

    /**
     * Gets the name of the entity, could be "Villager",
     * or, if the entity has a custom name, it returns that.
     *
     * @return the (custom) name of the entity
     */
    fun getName(): String = entity?.name?.let { TextComponent(it).getFormattedText() } ?: ""

    /**
     * Gets the Java class name of the entity, for example "EntityVillager"
     *
     * @return the entity's class name
     */
    fun getClassName(): String = entity?.javaClass?.simpleName ?: ""

    /**
     * Gets the Java UUID object of this entity.
     * Use of [UUID.toString] in conjunction is recommended.
     *
     * @return the entity's uuid
     */
    fun getUUID(): UUID? = entity?.uuid

    /**
     * Gets the entity's air level.
     *
     * The returned value will be an integer. If the player is not taking damage, it
     * will be between 300 (not in water) and 0. If the player is taking damage, it
     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
     *
     * @return the entity's air level
     */
    fun getAir(): Int = entity?.air ?: 300

    fun setAir(air: Int) = apply {
        entity?.air = air
    }

    fun distanceTo(other: Entity): Float = other.entity?.let(::distanceTo) ?: 0f

    fun distanceTo(other: MCEntity): Float = entity?.distanceTo(other) ?: 0f

    fun distanceTo(blockPos: BlockPos): Float = sqrt(entity?.squaredDistanceTo(
        blockPos.x.toDouble(),
        blockPos.y.toDouble(),
        blockPos.z.toDouble()
    ) ?: 0.0).toFloat()

    fun distanceTo(x: Float, y: Float, z: Float): Float = sqrt(entity?.squaredDistanceTo(
        x.toDouble(),
        y.toDouble(),
        z.toDouble()
    ) ?: 0.0).toFloat()

    fun isOnGround() = entity?.isOnGround ?: true

    fun isAirborne() = !isOnGround()

    // TODO(VERIFY)
    fun isCollided() = entity?.let { it.horizontalCollision || it.verticalCollision } ?: false

    fun getDistanceWalked() = (entity?.distanceTraveled ?: 0f) / 0.6f

    fun getStepHeight() = entity?.stepHeight ?: 0f

    fun hasNoClip() = entity?.noClip ?: false

    fun getTicksExisted() = entity?.age ?: 0

    // TODO("fabric")
    // fun getFireResistance() = entity.fireResistance

    fun isImmuneToFire() = entity?.isFireImmune ?: false

    fun isInWater() = entity?.isTouchingWater ?: false

    fun isWet() = entity?.isWet ?: false

    // TODO("fabric"): What exactly does this do?
    // fun getDimension() = entity.dimension

    fun setPosition(x: Double, y: Double, z: Double) = apply {
        entity?.setPosition(x, y, z)
    }

    fun setAngles(yaw: Float, pitch: Float) = apply {
        entity?.yaw = yaw
        entity?.pitch = pitch
    }

    fun getMaxInPortalTime() = entity?.maxNetherPortalTime ?: 0

    // TODO(BREAKING): Changed from seconds to ticks
    fun setOnFire(ticks: Int) = apply {
        entity?.isOnFire = true
        entity?.fireTicks = ticks
    }

    fun extinguish() = apply {
        entity?.extinguish()
    }

    fun move(x: Double, y: Double, z: Double) = apply {
        entity?.move(MovementType.SELF, Vec3d(x, y, z))
    }

    fun isSilent() = entity?.isSilent ?: false

    fun setIsSilent(silent: Boolean) = apply {
        entity?.isSilent = silent
    }

    fun isInLava() = entity?.isInLava ?: false

    fun addVelocity(x: Double, y: Double, z: Double) = apply {
        entity?.addVelocity(x, y, z)
    }

    fun getLookVector(partialTicks: Float) = entity?.getRotationVec(partialTicks) ?: Vec3d.ZERO

    // TODO(BREAKING): Removed partialTicks argument
    fun getEyePosition() = entity?.eyePos ?: 0

    fun canBeCollidedWith() = entity?.collides() ?: false

    fun canBePushed() = entity?.isPushable ?: false

    // TODO("fabric"): Add ItemEntity wrapper?
    fun dropItem(item: Item, size: Int): ItemEntity? = entity?.dropItem(item.item, size)

    fun isSneaking() = entity?.isSneaking ?: false

    fun setIsSneaking(sneaking: Boolean) = apply {
        entity?.isSneaking = sneaking
    }

    fun isSprinting() = entity?.isSprinting ?: false

    fun setIsSprinting(sprinting: Boolean) = apply {
        entity?.isSprinting = sprinting
    }

    fun isInvisible() = entity?.isInvisible ?: false

    fun setIsInvisible(invisible: Boolean) = apply {
        entity?.isInvisible = invisible
    }

    // TODO(BREAKING): Remove these methods, as it seems like only player entities have
    //                 the concept of eating
    // fun isEating() = entity.isEating
    //
    // fun setIsEating(eating: Boolean) = apply {
    //     entity.isEating = eating
    // }

    fun isOutsideBorder() = entity?.boundingBox?.let {
        World.getWorld()?.worldBorder?.contains(it)
    } ?: false

    // TODO(BREAKING): Remove this
    // fun setIsOutsideBorder(outside: Boolean) = apply {
    //     entity.isOutsideBorder = outside
    // }

    fun isBurning(): Boolean = entity?.let { (it.isInLava || it.isOnFire) && !it.isFireImmune } ?: false

    fun getWorld(): net.minecraft.world.World? = entity?.entityWorld

    override fun toString(): String {
        return "Entity{name=${getName()}, x=${getX()}, y=${getY()}, z=${getZ()}}"
    }
}
