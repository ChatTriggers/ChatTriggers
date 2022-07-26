package com.chattriggers.ctjs.mixins;

//#if MC>=11701
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.Mutable;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//$$ import net.minecraft.client.resources.sounds.AbstractSoundInstance;
//$$ import net.minecraft.client.resources.sounds.SoundInstance;
//$$ import net.minecraft.sounds.SoundSource;
//$$
//$$ @Mixin(AbstractSoundInstance.class)
//$$ public interface AbstractSoundInstanceAccessor {
//$$     @Final
//$$     @Mutable
//$$     @Accessor
//$$     void setSource(SoundSource source);
//$$
//$$     @Accessor
//$$     void setVolume(float volume);
//$$
//$$     @Accessor
//$$     void setPitch(float pitch);
//$$
//$$     @Accessor
//$$     void setX(double x);
//$$
//$$     @Accessor
//$$     void setY(double y);
//$$
//$$     @Accessor
//$$     void setZ(double z);
//$$
//$$     @Accessor
//$$     void setAttenuation(SoundInstance.Attenuation attenuation);
//$$ }
//#endif
