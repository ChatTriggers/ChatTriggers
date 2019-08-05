//#if MC>10809
//$$package com.chattriggers.ctjs.minecraft.mixins;
//$$
//$$import net.minecraft.client.particle.Particle;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$import org.spongepowered.asm.mixin.gen.Accessor;
//$$
//$$@Mixin(Particle.class)
//$$public interface MixinParticle {
//$$    @Accessor
//$$    double getPosX();
//$$
//$$    @Accessor
//$$    double getPosY();
//$$
//$$    @Accessor
//$$    double getPosZ();
//$$}
//#endif