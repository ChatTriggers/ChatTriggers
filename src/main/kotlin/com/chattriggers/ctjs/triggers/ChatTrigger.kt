package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.EventLib
import com.chattriggers.ctjs.utils.kotlin.MCITextComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import org.mozilla.javascript.regexp.NativeRegExp

class ChatTrigger(method: Any, type: TriggerType, loader: ILoader) : Trigger(method, type, loader) {
    private lateinit var chatCriteria: Any
    private var formatted: Boolean = false
    private var caseInsensitive: Boolean = false
    private lateinit var criteriaPattern: Regex
    private val parameters = mutableListOf<Parameter?>()
    private var triggerIfCanceled: Boolean = true
    private var canReceiveClientMessages: Boolean = false

    /**
     * Sets if the chat trigger should run if the chat event has already been canceled.
     * True by default.
     * @param bool Boolean to set
     * @return the trigger object for method chaining
     */
    fun triggerIfCanceled(bool: Boolean) = apply { triggerIfCanceled = bool }

    /**
     * Sets the chat criteria for [matchesChatCriteria].
     * Arguments for the trigger's method can be passed in using ${variable}.
     * Example: `setChatCriteria("<${name}> ${message}");`
     * Use ${*} to match a chat message but ignore the pass through.
     * @param chatCriteria the chat criteria to set
     * @return the trigger object for method chaining
     */
    fun setChatCriteria(chatCriteria: Any) = apply {
        this.chatCriteria = chatCriteria
        val flags = mutableSetOf<RegexOption>()
        var source = ".+"

        when (chatCriteria) {
            is String -> {
                formatted = Regex("[&\u00a7]") in chatCriteria

                val replacedCriteria = Regex.escape(chatCriteria.replace("\n", "->newLine<-"))
                    .replace(Regex("\\\$\\{[^*]+?}"), "\\\\E(.+)\\\\Q")
                    .replace(Regex("\\$\\{\\*?}"), "\\\\E(?:.+)\\\\Q")

                if (caseInsensitive)
                    flags.add(RegexOption.IGNORE_CASE)

                if ("" != chatCriteria)
                    source = replacedCriteria
            }
            is NativeRegExp -> {
                if (chatCriteria["ignoreCase"] as Boolean || caseInsensitive)
                    flags.add(RegexOption.IGNORE_CASE)

                if (chatCriteria["multiline"] as Boolean)
                    flags.add(RegexOption.MULTILINE)

                source = (chatCriteria["source"] as String).let {
                    if ("" == it) ".+" else it
                }

                formatted = Regex("[&\u00a7]") in source
            }
            else -> throw IllegalArgumentException("Expected String or Regexp Object")
        }

        criteriaPattern = Regex(source, flags)
    }

    /**
     * Alias for [setChatCriteria].
     * @param chatCriteria the chat criteria to set
     * @return the trigger object for method chaining
     */
    fun setCriteria(chatCriteria: Any) = setChatCriteria(chatCriteria)

    /**
     * Sets the chat parameter for [Parameter].
     * Clears current parameter list.
     * @param parameter the chat parameter to set
     * @return the trigger object for method chaining
     */
    fun setParameter(parameter: String) = apply {
        parameters.clear()
        addParameter(parameter)
    }

    /**
     * Sets multiple chat parameters for [Parameter].
     * Clears current parameter list.
     * @param parameters the chat parameters to set
     * @return the trigger object for method chaining
     */
    fun setParameters(vararg parameters: String) = apply {
        this.parameters.clear()
        addParameters(*parameters)
    }

    /**
     * Adds chat parameter for [Parameter].
     * @param parameter the chat parameter to add
     * @return the trigger object for method chaining
     */
    fun addParameter(parameter: String) = apply {
        parameters.add(Parameter.getParameterByName(parameter))
    }

    /**
     * Adds multiple chat parameters for [Parameter].
     * @param parameters the chat parameters to add
     * @return the trigger object for method chaining
     */
    fun addParameters(vararg parameters: String) = apply {
        parameters.forEach(::addParameter)
    }

    /**
     * Adds the "start" parameter
     * @return the trigger object for method chaining
     */
    fun setStart() = apply {
        setParameter("start")
    }

    /**
     * Adds the "contains" parameter
     * @return the trigger object for method chaining
     */
    fun setContains() = apply {
        setParameter("contains")
    }

    /**
     * Adds the "end" parameter
     * @return the trigger object for method chaining
     */
    fun setEnd() = apply {
        setParameter("end")
    }

    /**
     * Makes the trigger match the entire chat message
     * @return the trigger object for method chaining
     */
    fun setExact() = apply {
        parameters.clear()
    }

    /**
     * Makes the chat criteria case insensitive
     * @return the trigger object for method chaining
     */
    fun setCaseInsensitive() = apply {
        caseInsensitive = true

        // Reparse criteria if setCriteria has already been called
        if (::chatCriteria.isInitialized)
            setCriteria(chatCriteria)
    }

    /**
     * Allows this to trigger on client side messages, e.g. messages coming from other mods,
     * ChatLib.chat(), etc
     * @return the trigger object for method chaining
     */
    fun setCanReceiveClientMessages() = apply {
        canReceiveClientMessages = true
    }

    /**
     * Argument 1 (String) The chat message received
     * Argument 2 (ClientChatReceivedEvent) the chat event fired
     * @param args list of arguments as described
     */
    override fun trigger(args: Array<out Any?>) {
        require(args[0] is String && args[1] is ClientChatReceivedEvent && args[2] is Boolean) {
            "Argument 1 must be a String, Argument 2 must be a ClientChatReceivedEvent," +
                    "Argument 3 must be a Boolean"
        }

        val chatEvent = args[1] as ClientChatReceivedEvent
        val isClientSideTriggered = args[2] as Boolean

        if ((!triggerIfCanceled && chatEvent.isCanceled) || (!canReceiveClientMessages && isClientSideTriggered)) {
            return
        }

        // If the user has a chat trigger with canReceiveClientMessages, it will trigger at both the ASM point
        // and in ClientListener for "normal" chats (not client-only). So, to fix it firing twice, we store
        // the previous chat component to check if we have already seen it.
        if (prevChatComponent === chatEvent.message) {
            prevChatComponent = null
            return
        }

        prevChatComponent = chatEvent.message

        val chatMessage = getChatMessage(chatEvent, args[0] as String)

        val variables = getVariables(chatMessage) ?: return
        variables.add(chatEvent)

        callMethod(variables.toTypedArray())
    }

    // helper method to get the proper chat message based on the presence of color codes
    private fun getChatMessage(chatEvent: ClientChatReceivedEvent, chatMessage: String) =
        if (formatted)
            EventLib.getMessage(chatEvent).formattedText.replace("\u00a7", "&")
        else ChatLib.removeFormatting(chatMessage)

    // helper method to get the variables to pass through
    private fun getVariables(chatMessage: String) =
        if (::criteriaPattern.isInitialized)
            matchesChatCriteria(chatMessage.replace("\n", "->newLine<-"))
        else ArrayList()

    /**
     * A method to check whether a received chat message
     * matches this trigger's definition criteria.
     * Ex. "FalseHonesty joined Cops vs Crims" would match `${playername} joined ${gamejoined}`
     * @param chat the chat message to compare against
     * @return a list of the variables, in order or null if it doesn't match
     */
    private fun matchesChatCriteria(chat: String): MutableList<Any>? {
        val regex = criteriaPattern

        if (parameters.isEmpty()) {
            if (!(regex matches chat)) return null
        } else {
            parameters.forEach { parameter ->
                val first = try {
                    regex.find(chat)?.groups?.get(0)
                } catch (e: IndexOutOfBoundsException) {
                    return null
                }

                when (parameter) {
                    Parameter.CONTAINS -> if (first == null) return null
                    Parameter.START -> if (first == null || first.range.first != 0) return null
                    Parameter.END -> if (first?.range?.last != chat.length) return null
                    null -> if (!(regex matches chat)) return null
                }
            }
        }

        return regex.find(chat)?.groupValues?.drop(1)?.toMutableList()
    }

    /**
     * The parameter to match chat criteria to.
     * Location parameters
     * - contains
     * - start
     * - end
     */
    enum class Parameter constructor(vararg names: String) {
        CONTAINS("<c>", "<contains>", "c", "contains"),
        START("<s>", "<start>", "s", "start"),
        END("<e>", "<end>", "e", "end");

        var names: List<String> = names.asList()

        companion object {
            fun getParameterByName(name: String) =
                values().find { param ->
                    param.names.any { it.lowercase() == name }
                }
        }
    }

    companion object {
        private var prevChatComponent: MCITextComponent? = null
    }
}
