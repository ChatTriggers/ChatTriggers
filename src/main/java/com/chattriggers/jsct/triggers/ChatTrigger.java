package com.chattriggers.jsct.triggers;

import java.util.List;

/**
 * Copyright (c) FalseHonesty 2017
 */
public class ChatTrigger extends Trigger {
    private String methodName;
    private String chatCriteria;

    public ChatTrigger(String methodName, String chatCriteria) {
        this.methodName = methodName;
        this.chatCriteria = chatCriteria;
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
