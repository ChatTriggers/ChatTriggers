package com.chattriggers.ctjs.launch.mixins.transformers.gui;

//#if MC<=11202
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiIngame.class)
public interface GuiIngameAccessor {
    @Accessor
    String getDisplayedTitle();

    @Accessor
    String getDisplayedSubTitle();
}
//#endif
