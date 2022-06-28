package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.libs.MathLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.Player
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.minecraft.wrappers.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.minecraft.wrappers.world.Chunk
import com.chattriggers.ctjs.minecraft.wrappers.world.block.BlockPos
import com.chattriggers.ctjs.minecraft.wrappers.utils.Vec3i
import net.minecraft.util.Vec3
import java.util.*

//#if MC>=11701
//$$ import com.chattriggers.ctjs.launch.mixins.transformers.entity.EntityAccessor
//$$ import com.chattriggers.ctjs.utils.kotlin.asMixin
//$$ import gg.essential.universal.wrappers.message.UTextComponent
//$$ import net.minecraft.core.Registry
//$$ import net.minecraft.world.entity.MoverType
//$$ import net.minecraft.world.level.chunk.LevelChunk
//$$ import kotlin.math.sqrt
//#endif

// TODO(BREAKING) Removed all the server-side methods
// TODO(BREAKING) Made subclasses use entity field instead of making new ones
open class Entity(open val entity: net.minecraft.entity.Entity) {
    //#if MC<=11202
    fun getX() = entity.posX

    fun getY() = entity.posY

    fun getZ() = entity.posZ
    //#else
    //$$ fun getX() = entity.x
    //$$
    //$$ fun getY() = entity.y
    //$$
    //$$ fun getZ() = entity.z
    //#endif

    fun getPos() = Vec3i(getX(), getY(), getZ())

    //#if MC<=11202
    fun getLastX() = entity.lastTickPosX

    fun getLastY() = entity.lastTickPosY

    fun getLastZ() = entity.lastTickPosZ
    //#else
    //$$ fun getLastX() = entity.xOld
    //$$
    //$$ fun getLastY() = entity.yOld
    //$$
    //$$ fun getLastZ() = entity.zOld
    //#endif

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
        //#if MC<=11202
        return MathLib.wrapAngleDegrees(entity.rotationPitch.toDouble())
        //#else
        //$$ return MathLib.wrapAngleDegrees(entity.rotationVector.y.toDouble())
        //#endif
    }

    /**
     * Gets the yaw, the vertical direction the entity is facing towards.
     * This has a range of -180 to 180.
     *
     * @return the entity's yaw
     */
    fun getYaw(): Double {
        //#if MC<=11202
        return MathLib.wrapAngleDegrees(entity.rotationYaw.toDouble())
        //#else
        //$$ return MathLib.wrapAngleDegrees(entity.rotationVector.x.toDouble())
        //#endif
    }

    /**
     * Gets the entity's x motion.
     * This is the amount the entity will move in the x direction next tick.
     *
     * @return the entity's x motion
     */
    fun getMotionX(): Double {
        //#if MC<=11202
        return entity.motionX
        //#else
        //$$ return entity.deltaMovement.x
        //#endif
    }

    /**
     * Gets the entity's y motion.
     * This is the amount the entity will move in the y direction next tick.
     *
     * @return the entity's y motion
     */
    fun getMotionY(): Double {
        //#if MC<=11202
        return entity.motionY
        //#else
        //$$ return entity.deltaMovement.y
        //#endif
    }

    /**
     * Gets the entity's z motion.
     * This is the amount the entity will move in the z direction next tick.
     *
     * @return the entity's z motion
     */
    fun getMotionZ(): Double {
        //#if MC<=11202
        return entity.motionZ
        //#else
        //$$ return entity.deltaMovement.z
        //#endif
    }

    fun getRiding(): Entity? {
        //#if MC<=11202
        return entity.ridingEntity?.let(::Entity)
        //#else
        //$$ return entity.vehicle?.let(::Entity)
        //#endif
    }

    fun getRider(): Entity? = getRiders().firstOrNull()

    fun getRiders(): List<Entity> {
        //#if MC<=10809
        return listOfNotNull(entity.riddenByEntity?.let(::Entity))
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
    fun isDead(): Boolean {
        //#if MC<=11202
        return entity.isDead
        //#else
        //$$ return !entity.isAlive
        //#endif
    }

    /**
     * Gets the entire width of the entity's hitbox
     *
     * @return the entity's width
     */
    fun getWidth(): Float {
        //#if MC<=11202
        return entity.width
        //#else
        //$$ return entity.bbWidth
        //#endif
    }

    /**
     * Gets the entire height of the entity's hitbox
     *
     * @return the entity's height
     */
    fun getHeight(): Float {
        //#if MC<=11202
        return entity.height
        //#else
        //$$ return entity.bbHeight
        //#endif
    }

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
    open fun getName(): String {
        //#if MC<=11202
        return entity.name
        //#else
        //$$ return UTextComponent(entity.name).formattedText
        //#endif
    }

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
    fun getUUID(): UUID {
        //#if MC<=11202
        return entity.uniqueID
        //#else
        //$$ return entity.uuid
        //#endif
    }

    /**
     * Gets the entity's air level.
     *
     * The returned value will be an integer. If the player is not taking damage, it
     * will be between 300 (not in water) and 0. If the player is taking damage, it
     * will be between -20 and 0, getting reset to 0 every time the player takes damage.
     *
     * @return the entity's air level
     */
    fun getAir(): Int {
        //#if MC<=11202
        return entity.air
        //#else
        //$$ return entity.airSupply
        //#endif
    }

    fun getBiome(): String {
        val world = World.getWorld() ?: return ""

        //#if MC<=10809
        val chunk = world.getChunkFromBlockCoords(entity.position)
        return chunk.getBiome(entity.position, world.worldChunkManager).biomeName
        //#else
        //$$ return world.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getKey(world.getBiome(entity.blockPosition())).toString()
        //#endif
    }

    fun getLightLevel(): Int {
        //#if MC<=11202
        return World.getWorld()?.getLight(entity.position) ?: 0
        //#else
        //$$ return World.getWorld()?.lightEngine?.getRawBrightness(entity.blockPosition(), 0) ?: 0
        //#endif
    }

    fun distanceTo(other: Entity): Float = distanceTo(other.entity)

    fun distanceTo(other: net.minecraft.entity.Entity): Float {
        //#if MC<=11202
        return entity.getDistanceToEntity(other)
        //#else
        //$$ return entity.distanceTo(other)
        //#endif
    }

    fun distanceTo(blockPos: BlockPos): Float = distanceTo(blockPos.x.toFloat(), blockPos.y.toFloat(), blockPos.z.toFloat())

    fun distanceTo(x: Float, y: Float, z: Float): Float {
        //#if MC<=11202
        return entity.getDistance(
            x.toDouble(),
            y.toDouble(),
            z.toDouble()
        ).toFloat()
        //#else
        //$$ return sqrt(entity.distanceToSqr(x.toDouble(), y.toDouble(), z.toDouble())).toFloat()
        //#endif
    }

    fun isOnGround(): Boolean {
        //#if MC<=11202
        return entity.onGround
        //#else
        //$$ return entity.isOnGround
        //#endif
    }

    fun isCollided(): Boolean {
        //#if MC<=11202
        return entity.isCollided
        //#else
        //$$ return entity.horizontalCollision || entity.verticalCollision
        //#endif
    }

    fun getDistanceWalked(): Float {
        //#if MC<=11202
        return entity.distanceWalkedModified / 0.6f
        //#else
        //$$ return entity.walkDist / 0.6f
        //#endif
    }

    fun getStepHeight(): Float {
        //#if MC<=11202
        return entity.stepHeight
        //#else
        //$$ return entity.maxUpStep
        //#endif
    }

    fun hasNoClip(): Boolean {
        //#if MC<=11202
        return entity.noClip
        //#else
        //$$ return entity.noPhysics
        //#endif
    }

    fun getTicksExisted(): Int {
        //#if MC<=11202
        return entity.ticksExisted
        //#else
        //$$ return entity.tickCount
        //#endif
    }

    fun getFireResistance(): Int {
        //#if MC<=11202
        return entity.fireResistance
        //#else
        //$$ return entity.asMixin<EntityAccessor>().remainingFireTicks
        //#endif
    }

    fun isImmuneToFire(): Boolean {
        //#if MC<=11202
        return entity.isImmuneToFire
        //#else
        //$$ return entity.fireImmune()
        //#endif
    }

    fun isInWater() = entity.isInWater

    fun isWet(): Boolean {
        //#if MC<=11202
        return entity.isWet
        //#else
        //$$ return entity.isInWaterRainOrBubble
        //#endif
    }

    fun isAirborne(): Boolean {
        //#if MC<=11202
        return entity.isAirBorne
        //#else
        //$$ return !entity.isOnGround
        //#endif
    }

    fun getNBT(): NBTTagCompound {
        val compound = net.minecraft.nbt.NBTTagCompound()
        //#if MC<=11202
        entity.writeToNBT(compound)
        //#elseif MC>=11701
        //$$ entity.saveWithoutId(compound)
        //#endif
        return NBTTagCompound(compound)
    }

    fun getDimension(): World.Dimension {
        //#if MC<=11202
        return World.Dimension.values()[entity.dimension + 1]
        //#elseif MC>=11701
        //$$ return when (entity.level.dimension()) {
        //$$     net.minecraft.world.level.Level.NETHER -> World.Dimension.Nether
        //$$     net.minecraft.world.level.Level.OVERWORLD -> World.Dimension.Overworld
        //$$     net.minecraft.world.level.Level.END -> World.Dimension.End
        //$$     else -> World.Dimension.Overworld
        //$$ }
        //#endif
    }

    fun getMaxInPortalTime(): Int {
        //#if MC<=11202
        return entity.maxInPortalTime
        //#else
        //$$ return entity.portalWaitTime
        //#endif
    }

    fun isSilent() = entity.isSilent

    fun isInLava() = entity.isInLava

    fun getLookVector(partialTicks: Float): Vec3 {
        //#if MC<=11202
        return entity.getLook(partialTicks)
        //#else
        //$$ return entity.getViewVector(partialTicks)
        //#endif
    }

    fun getEyePosition(partialTicks: Float): Vec3 {
        //#if MC<=11202
        return entity.getPositionEyes(partialTicks)
        //#else
        //$$ return entity.getEyePosition(partialTicks)
        //#endif
    }

    fun canBeCollidedWith(): Boolean {
        //#if MC<=11202
        return entity.canBeCollidedWith()
        //#else
        //$$ return entity.isPushable
        //#endif
    }

    fun canBePushed(): Boolean {
        //#if MC<=11202
        return entity.canBePushed()
        //#else
        //$$ return entity.isPushable
        //#endif
    }

    fun isSneaking(): Boolean {
        //#if MC<=11202
        return entity.isSneaking
        //#else
        //$$ return entity.isShiftKeyDown
        //#endif
    }

    fun isSprinting() = entity.isSprinting

    fun isInvisible() = entity.isInvisible

    // TODO(BREAKING) Moved isEating to EntityLivingBase

    fun isOutsideBorder(): Boolean {
        //#if MC<=11202
        return entity.isOutsideBorder
        //#else
        //$$ return entity.level.worldBorder.isWithinBounds(entity.boundingBox)
        //#endif
    }

    fun isBurning(): Boolean {
        //#if MC<=11202
        return entity.isBurning
        //#else
        //$$ return entity.isOnFire
        //#endif
    }

    fun getWorld(): net.minecraft.world.World {
        //#if MC<=11202
        return entity.entityWorld
        //#else
        //$$ return entity.level
        //#endif
    }

    fun getChunk(): Chunk {
        //#if MC<=11202
        return Chunk(getWorld().getChunkFromChunkCoords(entity.chunkCoordX, entity.chunkCoordZ))
        //#else
        //$$ return Chunk(getWorld().getChunk(entity.blockPosition()) as LevelChunk)
        //#endif
    }

    /**
     * Gets the direction the entity is facing.
     * Example: "South West"
     *
     * @return The direction the player is facing, one of the four cardinal directions
     */
    fun facing(): String {
        if (Player.getPlayer() == null) return ""

        val yaw = Player.getYaw()

        return when {
            yaw in -22.5..22.5 -> "South"
            yaw in 22.5..67.5 -> "South West"
            yaw in 67.5..112.5 -> "West"
            yaw in 112.5..157.5 -> "North West"
            yaw < -157.5 || yaw > 157.5 -> "North"
            yaw in -157.5..-112.5 -> "North East"
            yaw in -112.5..-67.5 -> "East"
            yaw in -67.5..-22.5 -> "South East"
            else -> ""
        }
    }

    override fun toString(): String {
        return "Entity{name=${getName()}, x=${getX()}, y=${getY()}, z=${getZ()}}"
    }
}
