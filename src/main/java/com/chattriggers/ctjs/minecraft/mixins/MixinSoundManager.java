package com.chattriggers.ctjs.minecraft.mixins;

import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import paulscode.sound.SoundSystem;

@Mixin(SoundManager.class)
public interface MixinSoundManager {
    @Accessor(value = "sndSystem")
    SoundSystem getSndSystem();
}
