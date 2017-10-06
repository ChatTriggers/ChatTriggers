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
        OnChatTrigger trigger = new OnChatTrigger(methodName, chatCriteria);
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
     * Register a new method that receives chat events based on certain criteria
     * and a parameter.
     * The parameter can be {@code <s> for start, <c> for contains, <e> for end}
     * @param methodName the name of the method to callback when the event is fired
     * @param chatCriteria the criteria for which the event should be called
     * @param parameter the parameter for the criteria to apply to
     */
    public static void registerChat(String methodName, String chatCriteria, String parameter) {
        OnChatTrigger trigger = new OnChatTrigger(methodName, chatCriteria, parameter);
        TriggerType.CHAT.addTrigger(trigger);
    }

    /**
     * Register a new method that receives world load events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerWorldLoad(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.WORLD_LOAD);
        TriggerType.WORLD_LOAD.addTrigger(trigger);
    }

    /**
     * Register a new method that receives sound play events if the
     * sound event's name is the same as the criteria
     * @param methodName the name of the method to callback when the event is fired
     * @param soundName the name of the sound criteria (null for all sounds)
     */
    public static void registerSoundPlay(String methodName, String soundName) {
        OnSoundPlayTrigger trigger = new OnSoundPlayTrigger(methodName, soundName);
        TriggerType.SOUND_PLAY.addTrigger(trigger);
    }

    /**
     * Register a new method that receives client tick events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnTick(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.TICK);
        TriggerType.TICK.addTrigger(trigger);
    }

    /**
     * An overloaded method for {@link #registerOnTick(String)}
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerClientTick(String methodName) {
        registerOnTick(methodName);
    }

    /**
     * Register a new method that receives step events
     * @param methodName the name of the method to callback when the event is fired
     * @param fps how many fps this trigger should be limited to
     */
    public static void registerOnStep(String methodName, long fps) {
        OnStepTrigger trigger = new OnStepTrigger(methodName, fps);
        TriggerType.STEP.addTrigger(trigger);
    }

    /**
     * Register a new method that receives step events (fired at 60fps)
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnStep(String methodName) {
        registerOnStep(methodName, 60L);
    }

    /**
     * Register a new method that receives render overlay events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnRenderOverlay(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.RENDER_OVERLAY);
        TriggerType.RENDER_OVERLAY.addTrigger(trigger);
    }

    /**
     * Register a new method that receives render image overlay events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerOnImageOverlay(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.RENDER_IMAGE);
        TriggerType.RENDER_IMAGE.addTrigger(trigger);
    }

    /**
     * Register a new method that receives world unload events
     * @param methodName the name of the method to callback when the event is fired
     */
    public static void registerWorldUnload(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.WORLD_UNLOAD);
        TriggerType.WORLD_UNLOAD.addTrigger(trigger);
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