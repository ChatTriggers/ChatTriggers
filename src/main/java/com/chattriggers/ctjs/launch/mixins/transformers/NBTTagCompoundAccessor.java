package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(NBTTagCompound.class)
public interface NBTTagCompoundAccessor {
    //#if MC<=11202
    @Accessor
    //#elseif MC>=11701
    //$$ @Accessor("tags")
    //#endif
    Map<String, NBTBase> getTagMap();
}
