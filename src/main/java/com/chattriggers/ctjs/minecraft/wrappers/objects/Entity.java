//package com.chattriggers.ctjs.minecraft.wrappers.objects;
//
//import lombok.Getter;
//import net.minecraft.entity.EntityList;
//import net.minecraft.entity.EntityLivingBase;
//
////#if MC<=10809
//import net.minecraft.util.MathHelper;
////#else
////$$ import net.minecraft.util.math.MathHelper;
////$$ import java.util.List;
////$$ import java.util.stream.Collectors;
////#endif
//
//import java.util.UUID;
//
//public class Entity {
//    public net.minecraft.entity.Entity getEntity() {
//        return entity;
//    }
//
//    private net.minecraft.entity.Entity entity;
//
//    public Entity(net.minecraft.entity.Entity entity) {
//        this.entity = entity;
//    }
//
//    /**
//     * @return the entity's x coordinate
//     */
//    public double getX() {
//        return this.entity.posX;
//    }
//
//    /**
//     * @return the entity's y coordinate
//     */
//    public double getY() {
//        return this.entity.posY;
//    }
//
//    /**
//     * @return the entity's z coordinate
//     */
//    public double getZ() {
//        return this.entity.posZ;
//    }
//
//    /**
//     * Gets the pitch, the horizontal direction the entity is facing towards.
//     * This has a range of -180 to 180.
//     *
//     * @return the entity's pitch
//     */
//    public double getPitch() {
//        //#if MC<=10809
//        return MathHelper.wrapAngleTo180_float(this.entity.rotationPitch);
//        //#else
//        //$$ return MathHelper.wrapDegrees(this.entity.rotationPitch);
//        //#endif
//    }
//
//    /**
//     * Gets the yaw, the vertical direction the entity is facing towards.
//     * This has a range of -180 to 180.
//     *
//     * @return the entity's yaw
//     */
//    public double getYaw() {
//        //#if MC<=10809
//        return MathHelper.wrapAngleTo180_float(this.entity.rotationYaw);
//        //#else
//        //$$ return MathHelper.wrapDegrees(this.entity.rotationYaw);
//        //#endif
//    }
//
//    /**
//     * Gets the entity's x motion.
//     * This is the amount the entity will move in the x direction next tick.
//     *
//     * @return the player's x motion
//     */
//    public double getMotionX() {
//        return this.entity.motionX;
//    }
//
//    /**
//     * Gets the entity's y motion.
//     * This is the amount the entity will move in the y direction next tick.
//     *
//     * @return the player's y motion
//     */
//    public double getMotionY() {
//        return this.entity.motionY;
//    }
//
//    /**
//     * Gets the entity's z motion.
//     * This is the amount the entity will move in the z direction next tick.
//     *
//     * @return the player's z motion
//     */
//    public double getMotionZ() {
//        return this.entity.motionZ;
//    }
//
//    /**
//     * Gets the entity's health, 0 if not a living entity
//     *
//     * @return the entity's health
//     */
//    public float getHP() {
//        return this.entity instanceof EntityLivingBase
//                ? ((EntityLivingBase) this.entity).getHealth()
//                : 0;
//    }
//
//    /**
//     * Gets the entity that this entity is riding.
//     *
//     * @return the entity being ridden, or null if there isn't one.
//     */
//    public Entity getRiding() {
//        //#if MC<=10809
//        return this.entity.ridingEntity == null
//                ? null
//                : new Entity(this.entity.ridingEntity);
//        //#else
//        //$$ return this.entity.getRidingEntity() == null
//        //$$         ? null
//        //$$         : new Entity(this.entity.getRidingEntity());
//        //#endif
//    }
//
//    /**
//     * Gets the entity that is riding this entity.
//     *
//     * @return the riding entity, or null if there isn't one.
//     */
//    public Entity getRider() {
//        //#if MC<=10809
//        return this.entity.riddenByEntity == null
//                ? null
//                : new Entity(this.entity.riddenByEntity);
//        //#else
//        //$$ return getRiders().isEmpty()
//        //$$         ? null
//        //$$         : getRiders().get(0);
//        //#endif
//    }
//
//    //#if MC>10809
//    //$$ /**
//    //$$  * Gets the entities that are riding this entity.
//    //$$  *
//    //$$  * @return the riding entities, or an empty list if there aren't any.
//    //$$  */
//    //$$ public List<Entity> getRiders() {
//    //$$     return this.entity.getPassengers()
//    //$$             .stream()
//    //$$             .map(Entity::new)
//    //$$             .collect(Collectors.toList());
//    //$$ }
//    //#endif
//
//    /**
//     * Checks whether or not the entity is dead.
//     * This is a fairly loose term, dead for a particle could mean it has faded,
//     * while dead for an entity means it has no health.
//     *
//     * @return whether or not an entity is dead
//     */
//    public boolean isDead() {
//        return this.entity.isDead;
//    }
//
//    /**
//     * Gets the entire width of the entity's hitbox
//     *
//     * @return the entity's width
//     */
//    public float getWidth() {
//        return this.entity.width;
//    }
//
//    /**
//     * Gets the entire height of the entity's hitbox
//     *
//     * @return the entity's height
//     */
//    public float getHeight() {
//        return this.entity.height;
//    }
//
//    /**
//     * Gets the height of the eyes on the entity,
//     * can be added to its Y coordinate to get the actual Y location of the eyes.
//     * This value defaults to 85% of an entity's height, however is different for some entities.
//     *
//     * @return the height of the entity's eyes
//     */
//    public float getEyeHeight() {
//        return this.entity.getEyeHeight();
//    }
//
//    /**
//     * Gets the name of the entity, could be "Villager",
//     * or, if the entity has a custom name, it returns that.
//     *
//     * @return the (custom) name of the entity
//     */
//    public String getName() {
//        return this.entity.getName();
//    }
//
//    /**
//     * Gets the Java class name of the entity, for example "EntityVillager"
//     *
//     * @return the entity's class name
//     */
//    public String getClassName() {
//        return this.entity.getClass().getSimpleName();
//    }
//
//    /**
//     * Gets the Java UUID object of this entity.
//     * Use of {@link UUID#toString()} in conjunction is recommended.
//     *
//     * @return the entity's uuid
//     */
//    public UUID getUUID() {
//        return this.entity.getUniqueID();
//    }
//
//    @Override
//    public String toString() {
//        String name = EntityList.getEntityString(this.entity);
//        return "Entity{"
//                + (name == null ? this.entity.getClass().getSimpleName() : name)
//                + ",x:" + getX()
//                + ",y:" + getY()
//                + ",z:" + getZ()
//                + "}";
//    }
//}
