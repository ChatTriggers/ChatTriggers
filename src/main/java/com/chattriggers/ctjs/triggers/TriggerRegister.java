package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.commands.Command;
import net.minecraftforge.client.ClientCommandHandler;

public class TriggerRegister {
    /**
     * Registers a new chat trigger.<br>
     * Available modifications:<br>
     * {@link OnChatTrigger#setChatCriteria(String)} Sets the chat criteria<br>
     * {@link OnChatTrigger#setParameter(String)} Sets the chat parameter<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnChatTrigger registerChat(String methodName) {
        return new OnChatTrigger(methodName);
    }

    /**
     * Registers a new world load trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerWorldLoad(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.WORLD_LOAD);
    }

    /**
     * Registers a new world unload trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerWorldUnload(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.WORLD_UNLOAD);
    }

    /**
     * Registers a new clicked trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerClicked(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.CLICKED);
    }

    /**
     * Registers a new sound play trigger.<br>
     * Available modifications:<br>
     * {@link OnSoundPlayTrigger#setSoundNameCriteria(String)} Sets the sound name criteria<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnSoundPlayTrigger registerSoundPlay(String methodName) {
        return new OnSoundPlayTrigger(methodName);
    }

    /**
     * Registers a new tick trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerTick(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.TICK);
    }

    /**
     * Registers a new step trigger.<br>
     * Available modifications:<br>
     * {@link OnStepTrigger#setFps(long)} Sets the fps<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnStepTrigger registerStep(String methodName) {
        return new OnStepTrigger(methodName);
    }

    /**
     * Registers a new render overlay trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerRenderOverlay(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.RENDER_OVERLAY);
    }

    /**
     * Registers a new game load trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerGameLoad(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.GAME_LOAD);
    }

    /**
     * Registers a new game unload trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerGameUnload(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.GAME_UNLOAD);
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