package com.chattriggers.ctjs.launch.mixins.transformers

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.printToConsole
import com.chattriggers.ctjs.triggers.TriggerType
import com.chattriggers.ctjs.utils.Config
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ClientPlayerEntity::class)
class ClientPlayerEntityMixin {
    @Inject(method = ["sendMessage"], at = [At("HEAD")], cancellable = true)
    fun injectSendMessage(entity: ClientPlayerEntity, text: Text, actionBar: Boolean, ci: CallbackInfo) {
        val message = Message(text)
        if (actionBar) {
            ClientListener.actionBarHistory += ChatLib.replaceFormatting(message.getFormattedText())
            if (ClientListener.actionBarHistory.size > 1000)
                ClientListener.actionBarHistory.removeFirst()
            TriggerType.ActionBar.triggerAll(Message(message).getUnformattedText(), ci)
        } else {
            ClientListener.chatHistory += ChatLib.replaceFormatting(message.getFormattedText())
            if (ClientListener.chatHistory.size > 1000)
                ClientListener.chatHistory.removeFirst()
            TriggerType.Chat.triggerAll(Message(message).getUnformattedText(), ci)

            if (Config.printChatToConsole)
                "[CHAT] ${ClientListener.chatHistory.last()}".printToConsole()
        }
    }
}
