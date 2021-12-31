package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(NbtCompound.class)
public interface NbtCompoundAccessor {
    @Accessor
    public Map<String, NbtElement> getEntries();
}
