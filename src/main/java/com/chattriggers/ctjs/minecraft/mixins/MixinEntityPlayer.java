package com.chattriggers.ctjs.minecraft.mixins;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayer.class)
public interface MixinEntityPlayer {
    @Accessor
    String getDisplayName();

    @Accessor
    void setDisplayName(String displayName);
}
