package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.wrappers.world.PotionEffect
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import net.minecraft.potion.Potion

open class EntityLivingBase(override val entity: net.minecraft.entity.EntityLivingBase) : Entity(entity) {
    fun isEating(): Boolean {
        //#if MC<=11202
        return entity.isEating
        //#elseif MC>=11701
        //$$ return entity.isUsingItem && entity.getItemInHand(entity.usedItemHand).isEdible
        //#endif
    }

    fun getActivePotionEffects(): List<PotionEffect> {
        //#if MC<=11202
        return entity.activePotionEffects.map(::PotionEffect)
        //#else
        //$$ return entity.activeEffects.map(::PotionEffect)
        //#endif
    }

    fun canSeeEntity(other: net.minecraft.entity.Entity): Boolean {
        //#if MC<=11202
        return entity.canEntityBeSeen(other)
        //#else
        //$$ return entity.hasLineOfSight(other)
        //#endif
    }

    fun canSeeEntity(other: Entity) = canSeeEntity(other.entity)

    /**
     * Gets the item currently in the entity's specified inventory slot.
     * 0 for main hand, 1-4 for armor
     * (2 for offhand in 1.12.2, and everything else shifted over).
     *
     * @param slot the slot to access
     * @return the item in said slot
     */
    // TODO(BREAKING): Changed argument to enum
    fun getItemInSlot(slot: EquipmentSlot): Item? {
        //#if MC<=11202
        if (slot == EquipmentSlot.OffHand)
            return null
        return entity.getEquipmentInSlot(slot.mcValue)?.let(::Item)
        //#else
        //$$ return entity.getItemBySlot(slot.mcValue)?.let(::Item)
        //#endif
    }

    fun getHP() = entity.health

    fun getMaxHP() = entity.maxHealth

    fun getAbsorption() = entity.absorptionAmount

    // TODO(BREAKING): Remove this (doesn't exist in new versions, should just use getTicksExisted())
    // fun getAge() = entity.age

    fun getArmorValue(): Int {
        //#if MC<=11202
        return entity.totalArmorValue
        //#else
        //$$ return entity.armorValue
        //#endif
    }

    fun isPotionActive(id: Int): Boolean {
        //#if MC<=11202
        return entity.isPotionActive(id)
        //#else
        //$$ return MobEffect.byId(id)?.let(::isPotionActive) ?: false
        //#endif
    }

    fun isPotionActive(potion: Potion): Boolean {
        //#if MC<=11202
        return isPotionActive(potion.id)
        //#else
        //$$ return entity.hasEffect(potion)
        //#endif
    }

    fun isPotionActive(potionEffect: PotionEffect): Boolean {
        //#if MC<=11202
        return isPotionActive(potionEffect.effect.potionID)
        //#else
        //$$ return isPotionActive(potionEffect.effect.effect)
        //#endif
    }

    override fun toString(): String {
        return "EntityLivingBase{name=" + getName() +
                ", entity=" + super.toString() +
                "}"
    }

    //#if MC<=11202
    enum class EquipmentSlot(val mcValue: Int) {
        MainHand(0),
        OffHand(-1),
        Feet(1),
        Legs(2),
        Chest(3),
        Head(4),
    }
    //#else
    //$$ enum class EquipmentSlot(val mcValue: net.minecraft.world.entity.EquipmentSlot) {
    //$$     MainHand(net.minecraft.world.entity.EquipmentSlot.MAINHAND),
    //$$     OffHand(net.minecraft.world.entity.EquipmentSlot.OFFHAND),
    //$$     Feet(net.minecraft.world.entity.EquipmentSlot.FEET),
    //$$     Legs(net.minecraft.world.entity.EquipmentSlot.LEGS),
    //$$     Chest(net.minecraft.world.entity.EquipmentSlot.CHEST),
    //$$     Head(net.minecraft.world.entity.EquipmentSlot.HEAD),
    //$$ }
    //#endif
}
