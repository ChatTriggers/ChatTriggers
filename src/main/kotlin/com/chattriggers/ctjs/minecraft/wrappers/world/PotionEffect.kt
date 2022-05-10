package com.chattriggers.ctjs.minecraft.wrappers.world

import com.chattriggers.ctjs.utils.kotlin.MCPotionEffect
import com.chattriggers.ctjs.utils.kotlin.i18Format

//#if MC>=11701
//$$ import gg.essential.universal.wrappers.message.UTextComponent
//$$ import net.minecraft.world.effect.MobEffect
//#endif

class PotionEffect(val effect: MCPotionEffect) {
    /**
     * Returns the translation key of the potion.
     * Ex: "potion.poison"
     */
    fun getName(): String {
        //#if MC<=11202
        return effect.effectName
        //#else
        //$$ return UTextComponent(effect.effect.displayName).formattedText
        //#endif
    }

    /**
     * Returns the localized name of the potion that
     * is displayed in the player's inventory.
     * Ex: "Poison"
     */
    fun getLocalizedName(): String = getName().i18Format("%s")

    fun getAmplifier(): Int = effect.amplifier

    fun getDuration(): Int = effect.duration

    fun getID(): Int {
        //#if MC<=10809
        return effect.potionID
        //#else
        //$$ return MobEffect.getId(effect.effect)
        //#endif
    }

    fun isAmbient(): Boolean = effect.isAmbient

    fun isDurationMax(): Boolean {
        //#if MC<=11202
        return effect.isPotionDurationMax
        //#else
        //$$ // TODO(VERIFY)
        //$$ return effect.duration == Short.MAX_VALUE.toInt()
        //#endif
    }

    fun showsParticles(): Boolean {
        //#if MC<=10809
        return effect.isShowParticles
        //#else
        //$$ return effect.isVisible
        //#endif
    }

    override fun toString(): String = effect.toString()
}
