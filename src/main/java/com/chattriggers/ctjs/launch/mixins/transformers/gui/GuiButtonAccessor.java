package com.chattriggers.ctjs.launch.mixins.transformers.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(targets = {
        "net.minecraft.client.gui.GuiButton",
        "net.minecraft.client.gui.components.AbstractWidget",
        "net.minecraft.client.gui.widget.ClickableWidget"
})
public interface GuiButtonAccessor {
    @Accessor
    void setHeight(int height);
}
