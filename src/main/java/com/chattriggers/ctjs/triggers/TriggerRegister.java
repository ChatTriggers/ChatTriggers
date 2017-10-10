package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.commands.Command;
import net.minecraftforge.client.ClientCommandHandler;

public class TriggerRegister {

    /**
     * Register a new method that receives chat events based
     * on certain message criteria
     * @param methodName the name of the method to callback when the event is fired
     * @param chatCriteria the criteria for which the event should called
     */
    public static void registerChat(String methodName, String chatCriteria) {
        registerChat(methodName, chatCriteria, Priority.NORMAL);
    }

    /**
     * The same as {@Link #registerChat(String, String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param chatCriteria the criteria for which the event should called
     * @param priority the priority of this event
     */
    public static void registerChat(String methodName, String chatCriteria, Priority priority) {
        OnChatTrigger trigger = new OnChatTrigger(methodName, chatCriteria);
        trigger.priority = priority;
        TriggerType.CHAT.addTrigger(trigger);
    }

    /**
     * Register a new method that receives all chat events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerChat(String methodName) {
        registerChat(methodName, "");
    }

    /**
     * The same as {@Link #registerChat(String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param priority the priority of this event
     */
    public static void registerChat(String methodName, Priority priority) {
        registerChat(methodName, "", priority);
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
        registerChat(methodName, chatCriteria, parameter, Priority.NORMAL);
    }

    /**
     * The same as {@Link #registerChat(String, String, String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param chatCriteria the criteria for which the event should be called
     * @param parameter the parameter for the criteria to apply to
     * @param priority the priority of this event
     */
    public static void registerChat(String methodName, String chatCriteria, String parameter, Priority priority) {
        OnChatTrigger trigger = new OnChatTrigger(methodName, chatCriteria, parameter);
        trigger.priority = priority;
        TriggerType.CHAT.addTrigger(trigger);
    }

    /**
     * Register a new method that receives world load events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerWorldLoad(String methodName) {
        registerWorldLoad(methodName, Priority.NORMAL);
    }

    /**
     * Same as {@link #registerWorldLoad(String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param priority the priority of this event
     */
    public static void registerWorldLoad(String methodName, Priority priority) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.WORLD_LOAD);
        trigger.priority = priority;
        TriggerType.WORLD_LOAD.addTrigger(trigger);
    }

    /**
     * Register a new method that receives world unload events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerWorldUnload(String methodName) {
        registerWorldUnload(methodName, Priority.NORMAL);
    }

    /**
     * Same as {@link #registerWorldUnload(String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param priority the priority of this event
     */
    public static void registerWorldUnload(String methodName, Priority priority) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.WORLD_UNLOAD);
        trigger.priority = priority;
        TriggerType.WORLD_UNLOAD.addTrigger(trigger);
    }

    /**
     * Register a new method that receives sound play events if the
     * sound event's name is the same as the criteria
     * @param methodName the name of the method to callback when the event is fired
     * @param soundName the name of the sound criteria (null for all sounds)
     */
    public static void registerSoundPlay(String methodName, String soundName) {
        registerSoundPlay(methodName, soundName, Priority.NORMAL);
    }

    /**
     * Same as {@Link #registerSoundPlay(String, String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param soundName the name of the sound criteria (null for all sounds)
     * @param priority the priority of this event
     */
    public static void registerSoundPlay(String methodName, String soundName, Priority priority) {
        OnSoundPlayTrigger trigger = new OnSoundPlayTrigger(methodName, soundName);
        trigger.priority = priority;
        TriggerType.SOUND_PLAY.addTrigger(trigger);
    }

    /**
     * Register a new method that receives client tick events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnTick(String methodName) {
        registerOnTick(methodName, Priority.NORMAL);
    }

    /**
     * Same as {@link #registerOnTick(String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param priority the priority of this event
     */
    public static void registerOnTick(String methodName, Priority priority) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.TICK);
        trigger.priority = priority;
        TriggerType.TICK.addTrigger(trigger);
    }

    /**
     * Register a new method that receives step events
     * @param methodName the name of the method to callback when the event is fired
     * @param fps how many fps this trigger should be limited to
     */
    public static void registerOnStep(String methodName, long fps) {
        registerOnStep(methodName, fps, Priority.NORMAL);
    }

    /**
     * Register a new method that receives step events (fired at 60fps)
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnStep(String methodName) {
        registerOnStep(methodName, 60L, Priority.NORMAL);
    }

    /**
     * Same as {@Link #registerOnStep(String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param priority the priority of this event
     */
    public static void registerOnStep(String methodName, Priority priority) {
        registerOnStep(methodName, 60L, priority);
    }

    /**
     * Same as {@Link #registerOnStep(String, long)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param fps how many fps this trigger should be limited to
     * @param priority the priority of this event
     */
    public static void registerOnStep(String methodName, long fps, Priority priority) {
        OnStepTrigger trigger = new OnStepTrigger(methodName, fps);
        trigger.priority = priority;
        TriggerType.STEP.addTrigger(trigger);
    }

    /**
     * Register a new method that receives render overlay events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnRenderOverlay(String methodName) {
        registerOnRenderOverlay(methodName, Priority.NORMAL);
    }

    /**
     * Same as {@link #registerOnRenderOverlay(String)} except it takes a priority
     * @param methodName the name of the method to callback when the event is fired
     * @param priority the priority of this event
     */
    public static void registerOnRenderOverlay(String methodName, Priority priority) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.RENDER_OVERLAY);
        trigger.priority = priority;
        TriggerType.RENDER_OVERLAY.addTrigger(trigger);
    }

    /**
     * Register a new method that receives a command input
     * @param methodName the name of the method to callback when the event is fired
     * @param commandName the name of the command
     * @param commandUsage the usage for the command
     */
    public static void registerCommand(String methodName, String commandName, String commandUsage) {
        OnCommandTrigger trigger = new OnCommandTrigger(methodName);
        Command command = new Command(trigger, commandName, commandUsage);
        ClientCommandHandler.instance.registerCommand(command);
    }
}