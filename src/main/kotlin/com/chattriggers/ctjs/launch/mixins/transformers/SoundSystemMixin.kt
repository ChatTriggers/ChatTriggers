package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.triggers.TriggerType
import net.minecraft.client.sound.SoundInstance
import net.minecraft.client.sound.SoundSystem
import net.minecraft.util.math.Vec3f
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.LocalCapture

@Mixin(SoundSystem::class)
class SoundSystemMixin {
    @Inject(
        method = ["play"],
        at = [At(
            value = "INVOKE",
            target = "net/minecraft/client/sound/SoundInstance.getSoundSet(Lnet/minecraft/client/sound/SoundManager;)Lnet/minecraft/client/sound/WeightedSoundSet;",
            shift = At.Shift.BEFORE
        )],
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD,
    )
    fun injectPlay(soundSystem: SoundSystem, sound: SoundInstance, ci: CallbackInfo) {
        TriggerType.SoundPlay.triggerAll(
            Vec3f(sound.x.toFloat(), sound.y.toFloat(), sound.z.toFloat()),
            sound.id.toString(),
            sound.volume,
            sound.pitch,
            sound.category.getName(),
            ci,
        )
    }
}
