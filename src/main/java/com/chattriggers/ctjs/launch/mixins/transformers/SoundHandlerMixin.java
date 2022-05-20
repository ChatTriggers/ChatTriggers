package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC<=11202
@Mixin(SoundHandler.class)
public interface SoundHandlerMixin {
    @Accessor
    SoundManager getSndManager();
}
//#endif
