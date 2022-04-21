package com.chattriggers.ctjs.minecraft.wrappers.world

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCPotionEffect
import net.minecraft.client.resources.I18n

//#if MC>=11202
//$$ import net.minecraft.potion.Potion
//#endif

@External
class PotionEffect(val effect: MCPotionEffect) {
    /**
     * Returns the translation key of the potion.
     * Ex: "potion.poison"
     */
    fun getName(): String = effect.effectName

    /**
     * Returns the localized name of the potion that
     * is displayed in the player's inventory.
     * Ex: "Poison"
     */
    fun getLocalizedName(): String = I18n.format(getName(), "%s")

    fun getAmplifier(): Int = effect.amplifier

    fun getDuration(): Int = effect.duration

    fun getID(): Int {
        //#if MC<=10809
        return effect.potionID
        //#else
        //$$ return Potion.getIdFromPotion(effect.potion)
        //#endif
    }

    fun isAmbient(): Boolean = effect.isAmbient

    fun isDurationMax(): Boolean = effect.isPotionDurationMax

    fun showsParticles(): Boolean {
        //#if MC<=10809
        return effect.isShowParticles
        //#else
        //$$ return effect.doesShowParticles()
        //#endif
    }

    override fun toString(): String = effect.toString()
}
