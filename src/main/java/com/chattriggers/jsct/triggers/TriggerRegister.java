package com.chattriggers.jsct.triggers;

import com.chattriggers.jsct.commands.Command;
import net.minecraftforge.client.ClientCommandHandler;

import java.util.ArrayList;

public class TriggerRegister {

    /**
     * Register a new method that receives chat events based
     * on certain message criteria
     * @param methodName the name of the method to callback when the event is fired
     * @param chatCriteria the criteria for which the event should called
     */
    public static void registerChat(String methodName, String chatCriteria) {
        ChatTrigger trigger = new ChatTrigger(methodName, chatCriteria);
        TriggerTypes.CHAT.addTrigger(trigger);
    }

    /**
     * Register a new method that receives all chat events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerChat(String methodName) {
        registerChat(methodName, "");
    }

    /**
     * Register a new method that receives chat events based on certain criteria
     * and a parameter.
     * The parameter can be {@code <s> for start, <c> for contains, <e> for end}
     * @param methodName the name of the method to callback when the event is fired
     * @param chatCriteria the criteria for which the event should be called
     * @param parameter the parameter for the criteria to apply to
     */
    public static void registerChat(String methodName, String chatCriteria, String parameter) {
        ChatTrigger trigger = new ChatTrigger(methodName, chatCriteria, parameter);
        TriggerTypes.CHAT.addTrigger(trigger);
    }

    /**
     * Register a new method that receives world load events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerWorldLoad(String methodName) {
        WorldLoadTrigger trigger = new WorldLoadTrigger(methodName);
        TriggerTypes.WORLD_LOAD.addTrigger(trigger);
    }

    /**
     * Register a new method that receives sound play events if the
     * sound event's name is the same as the criteria
     * @param methodName the name of the method to callback when the event is fired
     * @param soundName the name of the sound criteria (null for all sounds)
     */
    public static void registerSoundPlay(String methodName, String soundName) {
        SoundPlayTrigger trigger = new SoundPlayTrigger(methodName, soundName);
        TriggerTypes.SOUND_PLAY.addTrigger(trigger);
    }

    /**
     * Register a new method that receives client tick events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnTick(String methodName) {
        OnTickTrigger trigger = new OnTickTrigger(methodName);
        TriggerTypes.TICK.addTrigger(trigger);
    }

    /**
     * Register a new method that receives render overlay events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnRenderOverlay(String methodName) {
        OnRenderOverlayTrigger trigger = new OnRenderOverlayTrigger(methodName);
        TriggerTypes.RENDER_OVERLAY.addTrigger(trigger);
    }

    /**
     * Register a new method that receives a command input
     * @param methodName the name of the method to callback when the event is fired
     * @param commandName the name of the command
     * @param commandUsage the usage for the command
     */
    public static void registerCommand(String methodName, String commandName, String commandUsage) {
        CommandTrigger trigger = new CommandTrigger(methodName);
        Command command = new Command(trigger, commandName, commandUsage);
        ClientCommandHandler.instance.registerCommand(command);
    }

    public enum TriggerTypes {
        CHAT, WORLD_LOAD, SOUND_PLAY, TICK, RENDER_OVERLAY;

        private ArrayList<Trigger> triggers = new ArrayList<>();

        public void clearTriggers() {
            triggers.clear();
        }

        public void addTrigger(Trigger trigger) {
            triggers.add(trigger);
        }

        public ArrayList<Trigger> getTriggers() {
            return triggers;
        }

        public static void triggerAllOfType(TriggerTypes triggerType, Object... args) {
            for (Trigger trigger : triggerType.getTriggers()) {
                trigger.trigger(args);
            }
        }

        public static void clearAllTriggers() {
            for (TriggerTypes triggerType : TriggerTypes.values()) {
                triggerType.clearTriggers();
            }
        }
    }
}