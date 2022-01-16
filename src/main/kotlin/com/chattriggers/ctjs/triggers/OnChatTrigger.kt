package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.engine.ILoader
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.listeners.CancellableEvent
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.utils.kotlin.External
import org.mozilla.javascript.regexp.NativeRegExp

@External
class OnChatTrigger(method: Any, type: TriggerType, loader: ILoader) : OnTrigger(method, type, loader) {
    private lateinit var chatCriteria: Any
    private var formatted: Boolean = false
    private var caseInsensitive: Boolean = false
    private lateinit var criteriaPattern: Regex
    private var parameters = mutableListOf<Parameter?>()
    private var triggerIfCanceled: Boolean = true

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
     * Example: `OnChatTrigger.setChatCriteria("<${name}> ${message}");`
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
                formatted = "&" in chatCriteria

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

                formatted = "&" in source
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
        parameters = mutableListOf(Parameter.getParameterByName(parameter))
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
     * Argument 1 (String) The chat message received
     * Argument 2 (ClientChatReceivedEvent) the chat event fired
     * @param args list of arguments as described
     */
    // TODO(BREAKING): Pass a Message object instead of an event
    override fun trigger(args: Array<out Any?>) {
        val message = args[0] as? Message ?: throw IllegalArgumentException("Expected a Message as the first argument")
        val event = args[1] as? CancellableEvent ?: throw IllegalArgumentException("Expected a CancellableEvent as the second argument")

        if (!triggerIfCanceled && event.isCanceled()) return

        val chatMessage = if (formatted) {
            ChatLib.replaceFormatting(message.getFormattedText())
        } else message.getUnformattedText()

        val variables = getVariables(chatMessage) ?: return
        variables.add(message)

        callMethod(variables.toTypedArray())
    }

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
}
