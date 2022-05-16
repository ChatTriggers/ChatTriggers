package com.chattriggers.ctjs.launch.mixins.transformers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC<=11202
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.IChatComponent;

@Mixin(GuiPlayerTabOverlay.class)
public interface PlayerTabOverlayAccessor {
    @Accessor
    IChatComponent getHeader();

    @Accessor
    IChatComponent getFooter();

    @Shadow
    void setHeader(IChatComponent header);

    @Shadow
    void setFooter(IChatComponent footer);
}
//#else
//$$ import net.minecraft.client.gui.components.PlayerTabOverlay;
//$$ import net.minecraft.network.chat.Component;
//$$
//$$ @Mixin(PlayerTabOverlay.class)
//$$ public interface PlayerTabOverlayAccessor {
//$$     @Accessor
//$$     Component getHeader();
//$$
//$$     @Accessor
//$$     Component getFooter();
//$$
//$$     @Shadow
//$$     void setHeader(Component header);
//$$
//$$     @Shadow
//$$     void setFooter(Component footer);
//$$ }
//#endif
