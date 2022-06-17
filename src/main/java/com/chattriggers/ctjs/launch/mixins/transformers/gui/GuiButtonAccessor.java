package com.chattriggers.ctjs.launch.mixins.transformers.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC<=11202
import net.minecraft.client.gui.GuiButton;
//#elseif MC>=11701
//$$ import net.minecraft.client.gui.components.AbstractWidget;
//#endif

//#if MC<=11202
@Mixin(GuiButton.class)
//#elseif MC>=11701
//$$ @Mixin(AbstractWidget.class)
//#endif
public interface GuiButtonAccessor {
    @Accessor
    void setHeight(int height);
}
