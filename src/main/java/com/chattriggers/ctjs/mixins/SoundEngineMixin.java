package com.chattriggers.ctjs.mixins;

//#if MC>=11701
//$$ import com.chattriggers.ctjs.minecraft.listeners.events.SoundEvent;
//$$ import com.chattriggers.ctjs.triggers.TriggerType;
//$$ import com.mojang.math.Vector3f;
//$$ import net.minecraft.client.resources.sounds.SoundInstance;
//$$ import net.minecraft.client.sounds.SoundEngine;
//$$ import net.minecraft.client.sounds.WeighedSoundEvents;
//$$ import net.minecraft.resources.ResourceLocation;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$ @Mixin(SoundEngine.class)
//$$ public class SoundEngineMixin {
//$$     @Inject(
//$$             method = "play",
//$$             at = @At(
//$$                     value = "INVOKE",
//$$                     target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getLocation()Lnet/minecraft/resources/ResourceLocation;"
//$$             ),
//$$             cancellable = true
//$$     )
//$$     private void chattriggers_playSoundTrigger(SoundInstance sound, CallbackInfo ci) {
//$$         Vector3f pos = new Vector3f((float) sound.getX(), (float) sound.getY(), (float) sound.getZ());
//$$         ResourceLocation location = sound.getLocation();
//$$         String name = location.getPath();
//$$         String category = sound.getSource().getName();
//$$
//$$         SoundEvent event = new SoundEvent(sound);
//$$
//$$         TriggerType.SoundPlay.triggerAll(
//$$                 pos,
//$$                 name,
//$$                 sound.getVolume(),
//$$                 sound.getPitch(),
//$$                 category,
//$$                 event
//$$         );
//$$
//$$         if (event.isCanceled()) {
//$$             ci.cancel();
//$$         }
//$$     }
//$$ }
//#endif