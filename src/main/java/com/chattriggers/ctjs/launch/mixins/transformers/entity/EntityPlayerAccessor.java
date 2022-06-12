package com.chattriggers.ctjs.launch.mixins.transformers.entity;

//#if FORGE
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC>=11701
//$$ import net.minecraft.network.chat.Component;
//#endif

@Mixin(EntityPlayer.class)
public interface EntityPlayerAccessor {
    //#if MC<=11202
    @Accessor(value = "displayname", remap = false)
    String getDisplayName();

    @Accessor(value = "displayname", remap = false)
    void setDisplayName(String name);
    //#else
    //$$ @Accessor(value = "displayname", remap = false)
    //$$ Component getDisplayName();
    //$$
    //$$ @Accessor(value = "displayname", remap = false)
    //$$ void setDisplayName(Component name);
    //#endif
}
//#endif
