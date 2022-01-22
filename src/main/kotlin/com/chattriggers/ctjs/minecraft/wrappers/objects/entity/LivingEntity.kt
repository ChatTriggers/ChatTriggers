package com.chattriggers.ctjs.minecraft.wrappers.objects.entity

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.objects.PotionEffect
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCEntity
import com.chattriggers.ctjs.utils.kotlin.MCLivingEntity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.util.registry.Registry

// TODO(BREAKING): Rename from EntityLivingBase
@External
open class LivingEntity(entity: MCLivingEntity?) : Entity(entity) {
    private val livingEntity: MCLivingEntity?
        inline get() = super.entity as MCLivingEntity?

    fun addPotionEffect(effect: PotionEffect) {
        livingEntity?.addStatusEffect(effect.effect)
    }

    fun clearPotionEffects() {
        livingEntity?.clearStatusEffects()
    }

    fun getActivePotionEffects() = livingEntity?.statusEffects?.map(::PotionEffect) ?: emptyList()

    fun canSeeEntity(other: Entity): Boolean = other.entity?.let(::canSeeEntity) ?: false

    fun canSeeEntity(other: MCEntity): Boolean = livingEntity?.canSee(other) ?: false

    /**
     * Gets the item currently in the entity's specified inventory slot.
     * 0 is main hand. Armor is 1-4. Off-hand is 5, if the version supports
     * it.
     *
     * @param slot the slot to access
     * @return the item in said slot
     */
    fun getItemInSlot(slot: Int): Item? {
        // 1 is off-hand, but we move it until after armor so armor values are
        // consistent across versions
        val adjustedSlot = when (slot) {
            0 -> 0
            1 -> 5
            in 2..5 -> slot - 1
            else -> throw IllegalStateException()
        }

        return livingEntity?.getEquippedStack(EquipmentSlot.values()[adjustedSlot])?.let(::Item)
    }

    fun getHP() = livingEntity?.health ?: 0

    fun setHP(health: Float) = apply {
        livingEntity?.health = health
    }

    fun getMaxHP() = livingEntity?.maxHealth ?: 0

    fun getAbsorption() = livingEntity?.absorptionAmount ?: 0

    fun setAbsorption(absorption: Float) = apply {
        livingEntity?.absorptionAmount = absorption
    }

    fun getAge() = livingEntity?.age ?: 0

    fun getArmorValue() = livingEntity?.armor ?: 0

    fun isPotionActive(id: Int) = livingEntity?.hasStatusEffect(Registry.STATUS_EFFECT[id]) ?: false

    fun isPotionActive(potion: PotionEffect) = livingEntity?.hasStatusEffect(potion.effect.effectType) ?: false

    override fun toString(): String {
        return "EntityLivingBase{name=${getName()}, entity=${super.toString()}}"
    }
}
