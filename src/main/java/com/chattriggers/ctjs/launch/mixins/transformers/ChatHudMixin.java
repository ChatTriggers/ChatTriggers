package com.chattriggers.ctjs.launch.mixins.transformers;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ChatHud.class)
public interface ChatHudMixin {
    @Accessor
    List<String> getMessageHistory();

    @Accessor
    List<ChatHudLine<Text>> getMessages();

    @Accessor
    List<ChatHudLine<OrderedText>> getVisibleMessages();

    @Invoker
    void invokeAddMessage(Text message, int messageId);

    @Invoker
    void invokeRemoveMessage(int messageId);
}
