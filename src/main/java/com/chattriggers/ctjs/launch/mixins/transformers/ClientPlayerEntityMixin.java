package com.chattriggers.ctjs.launch.mixins.transformers;

import com.chattriggers.ctjs.ReferenceKt;
import com.chattriggers.ctjs.engine.module.ModuleManager;
import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.listeners.ClientListener;
import com.chattriggers.ctjs.minecraft.objects.message.Message;
import com.chattriggers.ctjs.triggers.TriggerType;
import com.chattriggers.ctjs.utils.Config;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(
        method = "sendMessage",
        at = @At("HEAD"),
        cancellable = true
    )
    public void injectSendMessage(Text text, boolean actionBar, CallbackInfo ci) {
        Message message = new Message(text);
        if (actionBar) {
            ClientListener.getActionBarHistory().add(ChatLib.replaceFormatting(message.getFormattedText()));
            if (ClientListener.getActionBarHistory().size() > 1000)
                ClientListener.getActionBarHistory().remove(0);
            TriggerType.ActionBar.triggerAll(message, ci);
        } else {
            String newMessage = ChatLib.replaceFormatting(message.getFormattedText());
            ClientListener.getChatHistory().add(newMessage);
            if (ClientListener.getChatHistory().size() > 1000)
                ClientListener.getChatHistory().remove(0);
            TriggerType.Chat.triggerAll(message, ci);

            if (Config.INSTANCE.getPrintChatToConsole())
                ReferenceKt.printToConsole("[CHAT] " + newMessage, ModuleManager.getGeneralConsole());
        }
    }
}
