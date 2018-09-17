package com.chattriggers.ctjs.minecraft.wrappers.objects

import com.chattriggers.ctjs.utils.kotlin.External
import com.chattriggers.ctjs.utils.kotlin.MCPotionEffect

//#if MC>=11202
//$$ import net.minecraft.potion.Potion
//#endif

@External
class PotionEffect(private val effect: MCPotionEffect) {
    fun getName(): String = this.effect.effectName

    fun getAmplifier(): Int = this.effect.amplifier

    fun getDuration(): Int = this.effect.duration

    fun getID(): Int {
        //#if MC<=10809
        return this.effect.potionID
        //#else
        //$$ return Potion.getIdFromPotion(this.effect.potion)
        //#endif
    }

    fun isAmbient(): Boolean = this.effect.isAmbient

    fun isDurationMax(): Boolean = this.effect.isPotionDurationMax

    fun showsParticles(): Boolean {
        //#if MC<=10809
        return this.effect.isShowParticles
        //#else
        //$$ return this.effect.doesShowParticles();
        //#endif
    }

    override fun toString(): String = this.effect.toString()
}