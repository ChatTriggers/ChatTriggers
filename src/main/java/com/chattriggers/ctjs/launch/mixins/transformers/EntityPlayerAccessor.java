package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC>=11701
//$$ import net.minecraft.network.chat.Component;
//#endif

@Mixin(EntityPlayer.class)
public interface EntityPlayerAccessor {
    //#if MC<=11202
    @Accessor("displayname")
    String getDisplayName();

    @Accessor("displayname")
    void setDisplayName(String name);
    //#else
    //$$ @Accessor("displayname")
    //$$ Component getDisplayName();
    //$$
    //$$ @Accessor("displayname")
    //$$ void setDisplayName(Component name);
    //#endif
}
