package com.chattriggers.ctjs.launch.mixins.transformers.gui;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.List;

import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;

//#if MC>=11701
//$$ import net.minecraft.network.chat.Component;
//$$ import net.minecraft.util.FormattedCharSequence;
//#endif

@Mixin(GuiNewChat.class)
public interface GuiNewChatAccessor {
    //#if MC<=11202
    @Final
    @Accessor
    List<ChatLine> getChatLines();

    @Final
    @Accessor
    List<ChatLine> getDrawnChatLines();

    @Invoker
    void invokeClearChatMessages();

    @Invoker
    void invokeDeleteChatLine(int id);
    //#elseif MC>=11701
    //$$ @Final
    //$$ @Accessor("allMessages")
    //$$ List<GuiMessage<Component>> getChatLines();
    //$$
    //$$ @Final
    //$$ @Accessor("trimmedMessages")
    //$$ List<GuiMessage<FormattedCharSequence>> getDrawnChatLines();
    //$$
    //$$ @Invoker("clearMessages")
    //$$ void invokeClearChatMessages(boolean bl);
    //$$
    //$$ @Invoker("removeById")
    //$$ void invokeDeleteChatLine(int id);
    //#endif
}
