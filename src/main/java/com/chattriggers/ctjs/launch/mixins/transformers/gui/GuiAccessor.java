package com.chattriggers.ctjs.launch.mixins.transformers.gui;

//#if MC<=11202
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Gui.class)
public interface GuiAccessor {
    @Invoker
    void invokeDrawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor);
}
//#endif
