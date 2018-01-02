package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.libs.EventLib;
import com.chattriggers.ctjs.utils.console.Console;
import io.sentry.Sentry;
import io.sentry.event.Breadcrumb;
import io.sentry.event.BreadcrumbBuilder;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OnChatTrigger extends OnTrigger {
    private String chatCriteria;
    private Pattern criteriaPattern;
    private List<Parameter> parameters;
    private List<String> ignored;
    private Boolean triggerIfCanceled;

    public OnChatTrigger(String methodName) {
        super(methodName, TriggerType.CHAT);

        this.chatCriteria = "";
        this.parameters = new ArrayList<>();
        this.ignored = new ArrayList<>();
        this.triggerIfCanceled = true;
    }

    /**
     * Sets if the chat trigger should run if the chat event has already been canceled.
     * True by default.
     * @param bool Boolean to set
     * @return the trigger object for method chaining
     */
    public OnChatTrigger triggerIfCanceled(Boolean bool) {
        this.triggerIfCanceled = bool;
        return this;
    }

    /**
     * Sets the chat criteria for {@link #matchesChatCriteria(String)}.
     * @param chatCriteria the chat criteria to set
     * @return the trigger object for method chaining
     */
    public OnChatTrigger setChatCriteria(String chatCriteria) {
        this.chatCriteria = chatCriteria;

        String replacedCriteria = chatCriteria.replace("\n", "->newLine<-");
        replacedCriteria = Pattern.quote(replacedCriteria)
                .replaceAll("\\$\\{[^*]+?}", "\\\\E(.+)\\\\Q")
                .replaceAll("\\$\\{\\*?}", "\\\\E(?:.+)\\\\Q");

        this.criteriaPattern = Pattern.compile("".equals(chatCriteria) ? ".+" : replacedCriteria);

        return this;
    }

    /**
     * Adds substrings to be ignored when deciding if a chat event triggers this.
     * @param ignores substrings to ignore
     * @return the trigger object for method chaining
     */
    public OnChatTrigger shouldIgnore(String... ignores) {
        this.ignored.addAll(Arrays.asList(ignores));
        
        return this;
    }

    /**
     * Clears the ignored substring list.
     * @return the trigger object for method chaining
     */
    public OnChatTrigger clearIgnoreList() {
        this.ignored.clear();
        
        return this;
    }

    /**
     * Sets the chat parameter for {@link Parameter}.
     * Clears current parameter list.
     * @param parameter the chat parameter to set
     * @return the trigger object for method chaining
     */
    public OnChatTrigger setParameter(String parameter) {
        this.parameters = Collections.singletonList(Parameter.getParameterByName(parameter));
        return this;
    }

    /**
     * Sets multiple chat parameters for {@link Parameter}.
     * Clears current parameter list.
     * @param parameters the chat parameters to set
     * @return the trigger object for method chaining
     */
    public OnChatTrigger setParameters(String... parameters) {
        this.parameters.clear();
        for (String parameter : parameters)
            this.parameters.add(Parameter.getParameterByName(parameter));
        return this;
    }

    /**
     * Adds chat parameter for {@link Parameter}.
     * @param parameter the chat parameter to add
     * @return the trigger object for method chaining
     */
    public OnChatTrigger addParameter(String parameter) {
        this.parameters.add(Parameter.getParameterByName(parameter));
        return this;
    }

    /**
     * Adds multiple chat parameters for {@link Parameter}.
     * @param parameters the chat parameters to add
     * @return the trigger object for method chaining
     */
    public OnChatTrigger addParameters(String... parameters) {
        for (String parameter : parameters)
            this.parameters.add(Parameter.getParameterByName(parameter));
        return this;
    }

    /**
     * Argument 1 (String) The chat message received
     * Argument 2 (ClientChatReceivedEvent) the chat event fired
     * @param args list of arguments as described
     */
    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof String) || !(args[1] instanceof ClientChatReceivedEvent))
            throw new IllegalArgumentException("Argument 1 must be a String, Argument 2 must be a ClientChatReceivedEvent");

        ClientChatReceivedEvent chatEvent = (ClientChatReceivedEvent) args[1];
        if (!this.triggerIfCanceled && chatEvent.isCanceled()) return;

        String chatMessage = (String) args[0];

        if (chatCriteria.contains("&"))
            chatMessage = EventLib.getMessage((ClientChatReceivedEvent) args[1]).getFormattedText().replace("\u00a7", "&");

        for (String ignore : this.ignored)
            chatMessage = chatMessage.replace(ignore, "");

        List<Object> variables = new ArrayList<>();
        if (!"".equals(chatCriteria))
            variables = matchesChatCriteria(chatMessage.replace("\n", "->newLine<-"));

        if (variables != null) {

            Sentry.getContext().recordBreadcrumb(
                new BreadcrumbBuilder()
                    .setCategory("generic")
                    .setLevel(Breadcrumb.Level.INFO)
                    .setTimestamp(new Date())
                    .setType(Breadcrumb.Type.DEFAULT)
                    .setMessage("Chat message: " + chatMessage)
                    .build()
            );

            try {
                variables.add(chatEvent);
                CTJS.getInstance().getModuleManager().invokeFunction(methodName, variables.toArray(new Object[variables.size()]));
            } catch (ScriptException | NoSuchMethodException e) {
                Console.getConsole().printStackTrace(e, this);
                TriggerType.CHAT.removeTrigger(this);
            }
        }
    }

    /**
     * A method to check whether or not a received chat message
     * matches this trigger's definition criteria.
     * Ex. "FalseHonesty joined Cops vs Crims" would match {@code ${playername} joined ${gamejoined}}
     * @param chat the chat message to compare against
     * @return a list of the variables, in order or null if it doesn't match
     */
    public List<Object> matchesChatCriteria(String chat) {
        Matcher matcher = criteriaPattern.matcher(chat);

        if (parameters.isEmpty()) {
            if (!matcher.matches()) return null;
        } else {
            for (Parameter parameter : this.parameters) {
                if (parameter == Parameter.CONTAINS) {
                    if (!matcher.find()) return null;
                } else if (parameter == Parameter.START) {
                    if (!matcher.find() || matcher.start() != 0) return null;
                } else if (parameter == Parameter.END) {
                    int endMatch = -1;
                    while (matcher.find())
                        endMatch = matcher.end();
                    if (endMatch != chat.length()) return null;
                } else if (parameter == null && !matcher.matches()) {
                    return null;
                }
            }
        }

        ArrayList<Object> variables = new ArrayList<>();

        for (int i = 1; i <= matcher.groupCount(); i++) {
            variables.add(matcher.group(i));
        }

        return variables;
    }

    /**
     * The parameter to match chat criteria to.<br>
     * Location parameters<br>
     *     <strong>&emsp;contains</strong><br>
     *     <strong>&emsp;start</strong><br>
     *     <strong>&emsp;end</strong><br>
     */
     public enum Parameter {
        CONTAINS("<c>", "<contains>", "c", "contains"),
        START("<s>", "<start>", "s", "start"),
        END("<e>", "<end>", "e", "end");

        public List<String> names;

        Parameter(String... names) {
            this.names = Arrays.asList(names);
        }

        public static Parameter getParameterByName(String name) {
            for (Parameter parameter : Parameter.values()) {
                for (String paramName : parameter.names) {
                    if (paramName.equalsIgnoreCase(name)) return parameter;
                }
            }

            return null;
        }
    }
}
