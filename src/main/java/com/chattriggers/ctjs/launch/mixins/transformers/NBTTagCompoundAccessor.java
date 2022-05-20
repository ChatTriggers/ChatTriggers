package com.chattriggers.ctjs.launch.mixins.transformers;

//#if MC<=11202
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(NBTTagCompound.class)
public interface NBTTagCompoundAccessor {
    @Accessor
    Map<String, NBTBase> getTagMap();
}
//#endif
