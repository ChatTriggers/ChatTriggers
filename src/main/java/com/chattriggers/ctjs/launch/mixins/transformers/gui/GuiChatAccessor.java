package com.chattriggers.ctjs.launch.mixins.transformers.gui;

//#if MC<=11202
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChat.class)
public interface GuiChatAccessor {
    @Accessor
    GuiTextField getInputField();
}
//#endif
