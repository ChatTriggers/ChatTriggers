package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.commands.Command;
import net.minecraftforge.client.ClientCommandHandler;

public class TriggerRegister {
    /**
     * Registers a new chat trigger.<br>
     * Available modifications:<br>
     * {@link OnChatTrigger#setChatCriteria(String)} Sets the chat criteria<br>
     * {@link OnChatTrigger#setParameter(String)} Sets the chat parameter<br>
     * {@link OnTrigger#setPriority(Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnChatTrigger registerChat(String methodName) {
        OnChatTrigger trigger = new OnChatTrigger(methodName);
        TriggerType.CHAT.addTrigger(trigger);

        return trigger;
    }

    /**
     * Registers a new world load trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerWorldLoad(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.WORLD_LOAD);
        TriggerType.WORLD_LOAD.addTrigger(trigger);

        return trigger;
    }

    /**
     * Registers a new world unload trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerWorldUnload(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.WORLD_UNLOAD);
        TriggerType.WORLD_UNLOAD.addTrigger(trigger);

        return trigger;
    }

    /**
     * Registers a new sound play trigger.<br>
     * Available modifications:<br>
     * {@link OnSoundPlayTrigger#setSoundNameCriteria(String)} Sets the sound name criteria<br>
     * {@link OnTrigger#setPriority(Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnSoundPlayTrigger registerSoundPlay(String methodName) {
        OnSoundPlayTrigger trigger = new OnSoundPlayTrigger(methodName);
        TriggerType.SOUND_PLAY.addTrigger(trigger);

        return trigger;
    }

    /**
     * Registers a new tick trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerTick(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.TICK);
        TriggerType.TICK.addTrigger(trigger);

        return trigger;
    }

    /**
     * Registers a new step trigger.<br>
     * Available modifications:<br>
     * {@link OnStepTrigger#setFps(long)} Sets the fps<br>
     * {@link OnTrigger#setPriority(Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnStepTrigger registerStep(String methodName) {
        OnStepTrigger trigger = new OnStepTrigger(methodName);
        TriggerType.STEP.addTrigger(trigger);

        return trigger;
    }

    /**
     * Registers a new render overlay trigger.<br>
     * Available modifications:<br>
     * {@link OnTrigger#setPriority(Priority)} Sets the priority<br>
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerRenderOverlay(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.RENDER_OVERLAY);
        TriggerType.RENDER_OVERLAY.addTrigger(trigger);

        return trigger;
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