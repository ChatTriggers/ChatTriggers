package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.MCPotionEffect

class PotionEffect(private val effect: MCPotionEffect) {
    /**
     * @return the name of the potion effect
     */
    fun getName(): String = this.effect.effectName

    /**
     * @return the amplifier of the potion effect
     */
    fun getAmplifier() = this.effect.amplifier

    /**
     * @return the duration of the potion effect
     */
    fun getDuration() = this.effect.duration

    /**
     * @return the ID of the potion effect
     */
    fun getID(): Int {
        //#if MC<=10809
        return this.effect.potionID
        //#else
        //$$ return Potion.getIdFromPotion(this.effect.potion);
        //#endif
    }

    /**
     * @return True if the potion effect is ambient
     */
    fun isAmbient() = this.effect.isAmbient

    /**
     * @return True if the potion effect is the max duration
     */
    fun isDurationMax() = this.effect.isPotionDurationMax

    /**
     * @return True if the potion effect is showing particles
     */
    fun showsParticles(): Boolean {
        //#if MC<=10809
        return this.effect.isShowParticles
        //#else
        //$$ return this.effect.doesShowParticles();
        //#endif
    }

    override fun toString() = this.effect.toString()
}