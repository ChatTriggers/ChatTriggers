package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.text.TranslatableText
import net.minecraft.util.registry.Registry

@External
class PotionEffect(val effect: StatusEffectInstance) {
    /**
     * Returns the translation key of the potion.
     * Ex: "potion.poison"
     */
    fun getName(): String = effect.translationKey

    /**
     * Returns the localized name of the potion that
     * is displayed in the player's inventory.
     * Ex: "Poison"
     */
    fun getLocalizedName(): String = TextComponent(TranslatableText(getName())).getFormattedText()

    fun getAmplifier(): Int = effect.amplifier

    fun getDuration(): Int = effect.duration

    fun getID() = Registry.STATUS_EFFECT.getRawId(effect.effectType)

    fun isAmbient(): Boolean = effect.isAmbient

    // TODO("fabric")
    // fun isDurationMax(): Boolean = effect.isPotionDurationMax

    fun showsParticles() = effect.shouldShowParticles()

    override fun toString(): String = effect.toString()
}
