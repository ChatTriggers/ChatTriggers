package com.chattriggers.ctjs.mixins;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(NBTTagList.class)
public interface NBTTagListAccessor {
    //#if MC<=11202
    @Accessor
    //#elseif MC>=11701
    //$$ @Accessor("list")
    //#endif
    List<NBTBase> getTagList();
}
