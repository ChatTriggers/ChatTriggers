package com.chattriggers.ctjs.minecraft.mixins;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiPlayerTabOverlay.class)
public interface MixinGuiTabList {
    @Accessor(value = "header")
    IChatComponent getHeader();

    @Accessor(value = "footer")
    IChatComponent getFooter();
}
