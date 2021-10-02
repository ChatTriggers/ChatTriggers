package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.minecraft.libs.Tessellator
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockFace
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.objects.block.Vec3i
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import java.util.*

//#if MC==11602
//$$ import net.minecraft.entity.MoverType
//$$ import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
//$$ import net.minecraft.util.math.vector.Vector3d
//#else
import net.minecraft.util.Vec3
//#endif

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
        return MathHelper.wrapAngleTo180_float(this.entity.rotationPitch).toDouble()
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
        return MathHelper.wrapAngleTo180_float(this.entity.rotationYaw).toDouble()
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
    //#if MC==11602
    //$$ fun getMotionX(): Double = this.entity.motion.x
    //#else
    fun getMotionX(): Double = this.entity.motionX
    //#endif

    /**
     * Gets the entity's y motion.
     * This is the amount the entity will move in the y direction next tick.
     *
     * @return the player's y motion
     */
    //#if MC==11602
    //$$ fun getMotionY(): Double = this.entity.motion.y
    //#else
    fun getMotionY(): Double = this.entity.motionY
    //#endif

    /**
     * Gets the entity's z motion.
     * This is the amount the entity will move in the z direction next tick.
     *
     * @return the player's z motion
     */
    //#if MC==11602
    //$$ fun getMotionZ(): Double = this.entity.motion.z
    //#else
    fun getMotionZ(): Double = this.entity.motionZ
    //#endif

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
        //$$ return entity.ridingEntity?.let(::Entity)
        //#endif
    }

    fun getRiders(): List<Entity> {
        //#if MC<=10809
        return emptyList()
        //#else
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
    //#if MC==11602
    //$$ open fun getName(): String = TextComponent(this.entity.name).getFormattedText()
    //#else
    open fun getName(): String = this.entity.name
    //#endif

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

    fun distanceTo(blockPos: BlockPos): Float = distanceTo(
        blockPos.x.toFloat(),
        blockPos.y.toFloat(),
        blockPos.z.toFloat(),
    )

    fun distanceTo(x: Float, y: Float, z: Float): Float {
        //#if MC==11602
        //$$ return entity.getDistanceSq(
        //#else
        return entity.getDistance(
        //#endif
            x.toDouble(),
            y.toDouble(),
            z.toDouble()
        ).toFloat()
    }

    //#if MC==11602
    //$$ fun isOnGround() = entity.isOnGround
    //#else
    fun isOnGround() = entity.onGround
    //#endif

    //#if MC==11602
    //$$ fun isCollided() = entity.collidedVertically || entity.collidedHorizontally
    //#else
    fun isCollided() = entity.isCollided
    //#endif

    fun getDistanceWalked() = entity.distanceWalkedModified / 0.6

    fun getStepHeight() = entity.stepHeight

    fun hasNoClip() = entity.noClip

    fun getTicksExisted() = entity.ticksExisted

    // TODO(1.16.2)
    //#if MC==10809
    fun getFireResistance() = entity.fireResistance
    //#endif

    fun isImmuneToFire() = entity.isImmuneToFire

    fun isInWater() = entity.isInWater

    fun isWet() = entity.isWet

    fun isAirborne() = entity.isAirBorne

    // TODO(1.16.2)
    //#if MC==10809
    fun getDimension() = entity.dimension
    //#endif

    fun setPosition(x: Double, y: Double, z: Double) = apply {
        entity.setPosition(x, y, z)
    }

    // TODO(1.16.2)
    //#if MC==10809
    fun setAngles(yaw: Float, pitch: Float) = apply {
        entity.setAngles(yaw, pitch)
    }
    //#endif

    fun getMaxInPortalTime() = entity.maxInPortalTime

    fun setOnFire(seconds: Int) = apply {
        entity.setFire(seconds)
    }

    fun extinguish() = apply {
        entity.extinguish()
    }

    fun move(x: Double, y: Double, z: Double) = apply {
        //#if MC==11602
        //$$ entity.move(MoverType.SELF, Vector3d(x, y, z))
        //#else
        entity.moveEntity(x, y, z)
        //#endif
    }

    fun isSilent() = entity.isSilent

    fun setIsSilent(silent: Boolean) = apply {
        entity.isSilent = silent
    }

    fun isInLava() = entity.isInLava

    fun addVelocity(x: Double, y: Double, z: Double) = apply {
        entity.addVelocity(x, y, z)
    }

    //#if MC==11602
    //$$ fun getLookVector(partialTicks: Float): Vector3d = entity.getLook(partialTicks)
    //#else
    fun getLookVector(partialTicks: Float): Vec3 = entity.getLook(partialTicks)
    //#endif

    //#if MC==11602
    //$$ fun getEyePosition(partialTicks: Float): Vector3d = entity.getEyePosition(partialTicks)
    //#else
    fun getEyePosition(partialTicks: Float): Vec3 = entity.getPositionEyes(partialTicks)
    //#endif

    fun canBeCollidedWith() = entity.canBeCollidedWith()

    fun canBePushed() = entity.canBePushed()

    //#if MC==11602
    //$$ fun dropItem(item: Item, size: Int) = entity.entityDropItem(item.item, size)
    //#else
    fun dropItem(item: Item, size: Int) = entity.dropItem(item.item, size)
    //#endif

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

    // TODO(1.16.2)
    //#if MC==10809
    fun isEating() = entity.isEating

    fun setIsEating(eating: Boolean) = apply {
        entity.isEating = eating
    }

    fun isOutsideBorder() = entity.isOutsideBorder

    fun setIsOutsideBorder(outside: Boolean) = apply {
        entity.isOutsideBorder = outside
    }
    //#endif

    fun getWorld(): World = entity.entityWorld

    override fun toString(): String {
        return "Entity{name=${getName()}, x=${getX()}, y=${getY()}, z=${getZ()}}"
    }
}
