package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameOptions.class)
public interface GameOptionsAccessor {
    @Accessor
    KeyBinding[] getKeysAll();

    @Accessor
    void setKeysAll(KeyBinding[] bindings);
}
