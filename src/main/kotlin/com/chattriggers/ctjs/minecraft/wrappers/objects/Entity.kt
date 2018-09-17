package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MathHelper
import net.minecraft.entity.EntityLivingBase
import java.util.*

@External
open class Entity(val entity: MCEntity) {
    fun getX() = entity.posX

    fun getY() = entity.posY

    fun getZ() = entity.posZ

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
        //$$ return MathHelper.wrapDegrees(this.entity.rotationPitch);
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
        //$$ return MathHelper.wrapDegrees(this.entity.rotationYaw);
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
        return if (this.entity is EntityLivingBase)
            this.entity.health
        else -1f
    }

    fun getRiding(): Entity? {
        val riding = this.entity.ridingEntity

        return if (riding == null)
            null
        else
            Entity(riding)
    }

    fun getRider(): Entity? {
        //#if MC<=10809
        return if (this.entity.riddenByEntity == null)
            null
        else
            Entity(this.entity.riddenByEntity)
        //#else
        //$$ return if (getRiders().isEmpty())
        //$$     null
        //$$ else
        //$$     getRiders().get(0)
        //#endif
    }

    fun getRiders(): List<Entity> {
        //#if MC<=10809
        return listOf()
        //#elseif
        //$$ return this.entity.getPassengers().map {
        //$$     Entity(it)
        //$$ }
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

    override fun toString(): String {
        return ("Entity{"
                + getName()
                + ",x:" + getX()
                + ",y:" + getY()
                + ",z:" + getZ()
                + "}")
    }
}