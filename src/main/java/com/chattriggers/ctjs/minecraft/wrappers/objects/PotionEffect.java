//package com.chattriggers.ctjs.minecraft.wrappers.objects;
//
////#if MC>10809
////$$ import net.minecraft.potion.Potion;
////#endif
//
//public class PotionEffect {
//    private net.minecraft.potion.PotionEffect effect;
//
//    public PotionEffect(net.minecraft.potion.PotionEffect effect) {
//        this.effect = effect;
//    }
//
//    /**
//     * @return the name of the potion effect
//     */
//    public String getName() {
//        return this.effect.getEffectName();
//    }
//
//    /**
//     * @return the amplifier of the potion effect
//     */
//    public int getAmplifier() {
//        return this.effect.getAmplifier();
//    }
//
//    /**
//     * @return the duration of the potion effect
//     */
//    public int getDuration() {
//        return this.effect.getDuration();
//    }
//
//    /**
//     * @return the ID of the potion effect
//     */
//    public int getID() {
//        //#if MC<=10809
//        return this.effect.getPotionID();
//        //#else
//        //$$ return Potion.getIdFromPotion(this.effect.getPotion());
//        //#endif
//    }
//
//    /**
//     * @return True if the potion effect is ambient
//     */
//    public Boolean isAmbient() {
//        return this.effect.getIsAmbient();
//    }
//
//    /**
//     * @return True if the potion effect is the max duration
//     */
//    public Boolean isDurationMax() {
//        return this.effect.getIsPotionDurationMax();
//    }
//
//    /**
//     * @return True if the potion effect is showing particles
//     */
//    public Boolean showsParticles() {
//        //#if MC<=10809
//        return this.effect.getIsShowParticles();
//        //#else
//        //$$ return this.effect.doesShowParticles();
//        //#endif
//    }
//
//    public String toString() {
//        return this.effect.toString();
//    }
//}
