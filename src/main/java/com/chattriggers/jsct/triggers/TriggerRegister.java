package com.chattriggers.jsct.triggers;

import java.util.ArrayList;

/**
 * Copyright (c) FalseHonesty 2017
 */
public class TriggerRegister {

    public static void registerChat(String methodName, String chatCriteria) {
        ChatTrigger trigger = new ChatTrigger(methodName, chatCriteria);
        TriggerTypes.CHAT.addTrigger(trigger);
    }

    public enum TriggerTypes {
        CHAT;

        private ArrayList<Trigger> triggers;

        public void addTrigger(Trigger trigger) {
            triggers.add(trigger);
        }

        public ArrayList<Trigger> getTriggers() {
            return triggers;
        }
    }
}