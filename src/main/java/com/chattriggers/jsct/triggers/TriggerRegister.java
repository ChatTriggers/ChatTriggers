package com.chattriggers.jsct.triggers;

import java.util.ArrayList;

public class TriggerRegister {

    /**
     * Register a new method that receives chat events based
     * on certain message criteria
     * @param methodName the name of the method to callback when the event is called
     * @param chatCriteria the criteria for which the event should called
     */
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