package com.chattriggers.ctjs.minecraft.wrappers.objects.entity

import com.chattriggers.ctjs.minecraft.wrappers.objects.PotionEffect
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MCEntityLivingBase
import net.minecraft.potion.Potion

//#if MC>10809
//$$ import net.minecraft.inventory.EntityEquipmentSlot
//#endif

@External
open class EntityLivingBase(val entityLivingBase: MCEntityLivingBase) : Entity(entityLivingBase) {
    fun addPotionEffect(effect: PotionEffect) {
        entityLivingBase.addPotionEffect(effect.effect)
    }

    fun clearPotionEffects() {
        entityLivingBase.clearActivePotions()
    }

    fun getActivePotionEffects(): List<PotionEffect> {
        return entityLivingBase.activePotionEffects.map(::PotionEffect)
    }

    fun canSeeEntity(other: MCEntity) = entityLivingBase.canEntityBeSeen(other)

    fun canSeeEntity(other: Entity) = canSeeEntity(other.entity)

    /**
     * Gets the item currently in the entity's specified inventory slot.
     * 0 for main hand, 1-4 for armor
     * (2 for offhand in 1.12.2, and everything else shifted over).
     *
     * @param slot the slot to access
     * @return the item in said slot
     */
    fun getItemInSlot(slot: Int): Item? {
        //#if MC<=10809
        return entityLivingBase.getEquipmentInSlot(slot)?.let(::Item)
        //#else
        //$$ return entityLivingBase.getItemStackFromSlot(EntityEquipmentSlot.values()[slot])?.let(::Item)
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

    fun getAge() = entityLivingBase.age

    fun getArmorValue() = entityLivingBase.totalArmorValue

    fun isPotionActive(id: Int) = entityLivingBase.isPotionActive(id)

    fun isPotionActive(potion: Potion) = isPotionActive(potion.id)

    fun isPotionActive(potionEffect: PotionEffect) = isPotionActive(potionEffect.effect.potionID)

    override fun toString(): String {
        return "EntityLivingBase{name=" + getName() +
                ", entity=" + super.toString() +
                "}"
    }
}
