package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OnChatTrigger extends OnTrigger {
    private String chatCriteria = "";
    private Pattern criteriaPattern;
    private Parameter parameter;

    public OnChatTrigger(String methodName) {
        super(methodName);
    }

    public OnChatTrigger setChatCriteria(String chatCriteria) {
        this.chatCriteria = chatCriteria;

        String replacedCriteria = chatCriteria.replace("\n", "->newLine<-");
        replacedCriteria = Pattern.quote(replacedCriteria).replaceAll("\\$\\{.+?}", "\\\\E(.+)\\\\Q");

        criteriaPattern = Pattern.compile(chatCriteria.equals("") ? ".+" : replacedCriteria);

        return this;
    }

    public OnChatTrigger setParameter(String parameter) {
        this.parameter = Parameter.getParameterByName(parameter);

        return this;
    }

    /**
     * Argument 1 (String) The chat message received
     * Argument 2 (ClientChatReceivedEvent) the chat event fired
     * @param args list of arguments as described
     */
    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof String) || !(args[1] instanceof ClientChatReceivedEvent)) {
            throw new IllegalArgumentException("Argument 1 must be a String, Argument 2 must be a ClientChatReceivedEvent");
        }

        String chatMessage = (String) args[0];

        if (chatCriteria.contains("&")) {
            chatMessage = ((ClientChatReceivedEvent) args[1]).message.getFormattedText().replace("\u00a7", "&");
        }

        List<Object> variables = matchesChatCriteria(chatMessage.replace("\n", "->newLine<-"));

        if (variables != null) {
            try {
                variables.add(args[1]);
                CTJS.getInstance().getInvocableEngine().invokeFunction(methodName, variables.toArray(new Object[variables.size()]));
            } catch (ScriptException | NoSuchMethodException e) {
                Console.getConsole().printStackTrace(e);
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

        if (parameter == Parameter.CONTAINS) {
            if (!matcher.find()) return null;
        } else if (parameter == Parameter.START) {
            if (!matcher.find() || matcher.start() != 0) return null;
        } else if (parameter == Parameter.END) {
            int endMatch = -1;

            while (matcher.find()) {
                endMatch = matcher.end();
            }

            if (endMatch != chat.length()) return null;
        } else if (parameter == null) {
            if (!matcher.matches()) return null;
        }

        ArrayList<Object> variables = new ArrayList<>();

        for (int i = 1; i <= matcher.groupCount(); i++) {
            variables.add(matcher.group(i));
        }

        return variables;
    }

    private enum Parameter {
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
