package com.chattriggers.ctjs.minecraft.wrappers.entity

import com.chattriggers.ctjs.minecraft.wrappers.world.PotionEffect
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MCEntityLivingBase
import net.minecraft.potion.Potion

//#if MC>=11701
//$$ import net.minecraft.world.entity.EquipmentSlot as MCEquipmentSlot
//#endif

open class EntityLivingBase(val entityLivingBase: MCEntityLivingBase) : Entity(entityLivingBase) {
    fun addPotionEffect(effect: PotionEffect) {
        //#if MC<=11202
        entityLivingBase.addPotionEffect(effect.effect)
        //#else
        //$$ entityLivingBase.addEffect(effect.effect)
        //#endif
    }

    fun clearPotionEffects() {
        //#if MC<=11202
        entityLivingBase.clearActivePotions()
        //#else
        //$$ entityLivingBase.removeAllEffects()
        //#endif
    }

    fun getActivePotionEffects(): List<PotionEffect> {
        //#if MC<=11202
        return entityLivingBase.activePotionEffects.map(::PotionEffect)
        //#else
        //$$ return entityLivingBase.activeEffects.map(::PotionEffect)
        //#endif
    }

    fun canSeeEntity(other: MCEntity): Boolean {
        //#if MC<=11202
        return entityLivingBase.canEntityBeSeen(other)
        //#else
        //$$ return entityLivingBase.hasLineOfSight(other)
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
        return entityLivingBase.getEquipmentInSlot(slot.mcValue)?.let(::Item)
        //#else
        //$$ return entityLivingBase.getItemBySlot(slot.mcValue)?.let(::Item)
        //#endif
    }

    fun getHP() = entityLivingBase.health

    fun setHP(health: Float) = apply {
        entityLivingBase.health = health
    }

    fun getMaxHP() = entityLivingBase.maxHealth

    fun getAbsorption() = entityLivingBase.absorptionAmount

    fun setAbsorption(absorption: Float) = apply {
        entityLivingBase.absorptionAmount = absorption
    }

    // TODO(BREAKING): Remove this (doesn't exist in new versions, should just use getTicksExisted())
    // fun getAge() = entityLivingBase.age

    fun getArmorValue(): Int {
        //#if MC<=11202
        return entityLivingBase.totalArmorValue
        //#else
        //$$ return entityLivingBase.armorValue
        //#endif
    }

    fun isPotionActive(id: Int): Boolean {
        //#if MC<=11202
        return entityLivingBase.isPotionActive(id)
        //#else
        //$$ return MobEffect.byId(id)?.let(::isPotionActive) ?: false
        //#endif
    }

    fun isPotionActive(potion: Potion): Boolean {
        //#if MC<=11202
        return isPotionActive(potion.id)
        //#else
        //$$ return entityLivingBase.hasEffect(potion)
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
    //$$ enum class EquipmentSlot(val mcValue: MCEquipmentSlot) {
    //$$     MainHand(MCEquipmentSlot.MAINHAND),
    //$$     OffHand(MCEquipmentSlot.OFFHAND),
    //$$     Feet(MCEquipmentSlot.FEET),
    //$$     Legs(MCEquipmentSlot.LEGS),
    //$$     Chest(MCEquipmentSlot.CHEST),
    //$$     Head(MCEquipmentSlot.HEAD),
    //$$ }
    //#endif
}
