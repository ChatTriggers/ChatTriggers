package com.chattriggers.ctjs.minecraft.libs

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.mixins.MixinGuiNewChat
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.Player
import jdk.nashorn.api.scripting.ScriptObjectMirror
import net.minecraft.client.gui.ChatLine
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.function.Function
import java.util.regex.Pattern

@SideOnly(Side.CLIENT)
object ChatLib {
    fun chat(text: Any) {
        when (text) {
            is String -> Message(text).chat()
            is Message -> text.chat()
            is TextComponent -> text.chat()
        }
    }

    fun actionBar(text: Any) {
        when (text) {
            is String -> Message(text).actionBar()
            is Message -> text.chat()
            is TextComponent -> text.chat()
        }
    }

    fun simulateChat(text: Any) {
        when (text) {
            is String -> Message(text).setRecursive(true).chat()
            is Message -> text.setRecursive(true).chat()
            is TextComponent -> Message(text).setRecursive(true).chat()
        }
    }

    fun say(text: String) = Player.getPlayer()?.sendChatMessage(text)
    fun command(text: String) = say("/$text")

    fun clearChat() {
        //#if MC<=10809
        Client.getChatGUI().clearChatMessages()
        //#else
        //$$ Client.getChatGUI().clearChatMessages(false);
        //#endif
    }

    fun clearChat(vararg chatLineIDs: Int) {
        for (chatLineID in chatLineIDs)
            Client.getChatGUI().deleteChatLine(chatLineID)
    }

    @JvmOverloads
    fun getChatBreak(separator: String = "-"): String {
        val stringBuilder = StringBuilder()

        while (Renderer.getStringWidth(stringBuilder.toString()) < Client.getChatGUI().chatWidth) {
            stringBuilder.append(separator)
        }

        return stringBuilder.deleteCharAt(stringBuilder.length - 1).toString()
    }

    fun removeFormatting(text: String) = text.replace("[\\u00a7&][0-9a-fklmnor]", "")
    fun replaceFormatting(text: String) = text.replace("\\u00a7(?![^0-9a-fklmnor]|$)", "&")

    fun getCenteredText(text: String): String {
        var left = true
        val stringBuilder = StringBuilder(removeFormatting(text))

        if (Renderer.getStringWidth(stringBuilder.toString()) > Client.getChatGUI().chatWidth) {
            return stringBuilder.toString()
        }

        while (Renderer.getStringWidth(stringBuilder.toString()) < Client.getChatGUI().chatWidth) {
            left = if (left) {
                stringBuilder.insert(0, " ")
                false
            } else {
                stringBuilder.append(" ")
                true
            }
        }

        return stringBuilder.deleteCharAt(
                if (left) 0 else stringBuilder.length - 1).toString().replace(removeFormatting(text),
                text
        )
    }

    fun editChat(regexp: Any, vararg replacements: Message) {
        if (regexp !is ScriptObjectMirror) {
            throw IllegalArgumentException("Regex object not correct")
        }

        val global = regexp["global"] as Boolean
        val ignoreCase = regexp["ignoreCase"] as Boolean
        val multiline = regexp["multiline"] as Boolean

        val flags = (if (ignoreCase) Pattern.CASE_INSENSITIVE else 0) or if (multiline) Pattern.MULTILINE else 0
        val pattern = Pattern.compile(regexp["source"] as String, flags)

        editChat(
                { message ->
                    val matcher = pattern.matcher(message.getChatMessage().getUnformattedText())
                    if (global) matcher.find() else matcher.matches()
                },
                *replacements
        )
    }

    fun editChat(toReplace: String, vararg replacements: Message) {
        editChat(
                { message -> removeFormatting(message.getChatMessage().getUnformattedText()) == toReplace },
                *replacements
        )
    }

    fun editChat(toReplace: Message, vararg replacements: Message) {
        editChat(
                { message ->
                    println("tr: " + toReplace.getChatMessage().formattedText)
                    println("m: " + message.getChatMessage().getFormattedText().replaceFirst("\\u00a7r".toRegex(), ""))
                    toReplace.getChatMessage().formattedText == message.getChatMessage().getFormattedText().replaceFirst("\\u00a7r".toRegex(), "")
                },
                *replacements
        )
    }

    fun editChat(chatLineId: Int, vararg replacements: Message) {
        editChat(
                { message -> message.getChatLineId() == chatLineId },
                *replacements
        )
    }

    fun editChat(toReplace: Function<Message, Boolean>, vararg replacements: Message) {
        val drawnChatLines = (Client.getChatGUI() as MixinGuiNewChat).drawnChatLines
        val chatLines = (Client.getChatGUI() as MixinGuiNewChat).chatLines

        editChatLineList(chatLines, toReplace, *replacements)
        editChatLineList(drawnChatLines, toReplace, *replacements)
    }

    private fun editChatLineList(lineList: MutableList<ChatLine>, toReplace: Function<Message, Boolean>, vararg replacements: Message) {
        val chatLineIterator = lineList.listIterator()

        while (chatLineIterator.hasNext()) {
            val chatLine = chatLineIterator.next()

            val result = toReplace.apply(
                    Message(chatLine.chatComponent).setChatLineId(chatLine.chatLineID)
            )

            if (!result) {
                continue
            }

            chatLineIterator.remove()

            for (message in replacements) {
                val lineId = if (message.getChatLineId() == -1) 0 else message.getChatLineId()

                val newChatLine = ChatLine(chatLine.updatedCounter, message.getChatMessage(), lineId)
                chatLineIterator.add(newChatLine)
            }
        }
    }
}