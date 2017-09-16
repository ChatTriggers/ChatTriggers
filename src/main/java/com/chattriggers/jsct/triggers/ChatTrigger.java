package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.JSCT;

import javax.script.ScriptException;
import java.util.List;


public class ChatTrigger extends Trigger {
    private String chatCriteria;

    public ChatTrigger(String methodName, String chatCriteria) {
        super(methodName);
        this.chatCriteria = chatCriteria;
    }

    /**
     * Argument 1 (String) The chat message received
     * @param args list of arguments as described
     */
    @Override
    public void trigger(Object... args) {
        if (!(args[0] instanceof String)) throw new IllegalArgumentException("Argument 1 must be a string");

        String chatMessage = (String) args[0];

        List<String> variables = matchesChatCriteria(chatMessage);

        if (variables != null) {
            try {
                JSCT.getInstance().getScriptEngine().invokeFunction(methodName, variables);
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
        //TODO
        return null;
    }
}
