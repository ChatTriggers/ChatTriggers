package com.chattriggers.ctjs.mixins.gui;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChat.class)
public interface GuiChatAccessor {
    //#if MC<=11202
    @Accessor
    //#elseif MC>=11701
    //$$ @Accessor("input")
    //#endif
    GuiTextField getInputField();
}
