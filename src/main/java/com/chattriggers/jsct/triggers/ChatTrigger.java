package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.JSCT;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ChatTrigger extends Trigger {
    private String chatCriteria;
    private Pattern criteriaPattern;

    public ChatTrigger(String methodName, String chatCriteria) {
        super(methodName);
        this.chatCriteria = chatCriteria;

        String replacedCriteria = Pattern.quote(chatCriteria).replaceAll("\\$\\{.+?}", "\\\\E(.+)\\\\Q");

        criteriaPattern = Pattern.compile(replacedCriteria);
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

        List<String> variables = matchesChatCriteria(chatMessage);

        if (variables != null) {
            try {
                Object isNotCanceled = JSCT.getInstance().getInvocableEngine().invokeFunction(methodName, variables.toArray(new Object[variables.size()]));

                if (isNotCanceled != null && isNotCanceled.equals(false)) ((ClientChatReceivedEvent) args[1]).setCanceled(true);
            } catch (ScriptException | NoSuchMethodException e) {
                e.printStackTrace();
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
    public List<String> matchesChatCriteria(String chat) {
        Matcher matcher = criteriaPattern.matcher(chat);

        if (!matcher.matches()) return null;

        ArrayList<String> variables = new ArrayList<>();

        for (int i = 1; i <= matcher.groupCount(); i++) {
            variables.add(matcher.group(i));
        }

        return variables;
    }
}
