package com.chattriggers.ctjs.launch.mixins.transformers.gui;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

//#if MC<=11202
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.gen.Accessor;
//#else
//$$ import net.minecraft.client.gui.components.ChatComponent;
//$$ import net.minecraft.client.GuiMessage;
//$$ import net.minecraft.network.chat.Component;
//$$ import net.minecraft.util.FormattedCharSequence;
//#endif

//#if MC<=11202
@Mixin(GuiNewChat.class)
public abstract class ChatGuiMixin {
    @Final
    @Shadow
    private List<ChatLine> chatLines;

    @Final
    @Shadow
    private List<ChatLine> drawnChatLines;

    @Shadow
    public abstract void clearChatMessages();

    @Shadow
    public abstract void deleteChatLine(int id);

    public ChatLib.ChatLineListIterator<ChatLine> getChatLines() {
        return new ChatLib.ChatLineListIterator<>(chatLines);
    }

    public ChatLib.ChatLineListIterator<ChatLine> getDrawnChatLines() {
        return new ChatLib.ChatLineListIterator<>(chatLines);
    }

    public void clearMessages() {
        clearChatMessages();
    }

    public void deleteMessage(int id) {
        deleteChatLine(id);
    }
}
//#else
//$$ @Mixin(ChatComponent.class)
//$$ public abstract class ChatGuiMixin {
//$$     @Final
//$$     @Shadow
//$$     private List<GuiMessage<Component>> allMessages;
//$$
//$$     @Final
//$$     @Shadow
//$$     private List<GuiMessage<FormattedCharSequence>> trimmedMessages;
//$$
//$$     @Shadow
//$$     public abstract void clearMessages(boolean clearRecent);
//$$
//$$     @Shadow
//$$     private void removeById(int id) {}
//$$
//$$     public ChatLib.ChatLineListIterator<Component> getChatLines() {
//$$         return new ChatLib.ChatLineListIterator<>(allMessages, false);
//$$     }
//$$
//$$     public ChatLib.ChatLineListIterator<FormattedCharSequence> getDrawnChatLines() {
//$$         return new ChatLib.ChatLineListIterator<>(trimmedMessages, true);
//$$     }
//$$
//$$     public void clearMessages() {
//$$         clearMessages(false);
//$$     }
//$$
//$$     public void deleteMessage(int id) {
//$$         removeById(id);
//$$     }
//$$ }
//#endif
