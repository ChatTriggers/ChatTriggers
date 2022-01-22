package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @Inject(
        method = "play(Lnet/minecraft/client/sound/SoundInstance;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sound/SoundInstance;getSoundSet(Lnet/minecraft/client/sound/SoundManager;)Lnet/minecraft/client/sound/WeightedSoundSet;",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    public void injectPlay(SoundInstance sound, CallbackInfo ci) {
        if (sound.getSound() == null)
            return;

        TriggerType.SoundPlay.triggerAll(
            new Vec3f((float) sound.getX(), (float) sound.getY(), (float) sound.getZ()),
            sound.getId().toString(),
            sound.getVolume(),
            sound.getPitch(),
            sound.getCategory().getName(),
            ci
        );
    }
}
