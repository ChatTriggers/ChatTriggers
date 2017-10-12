package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.commands.Command;
import net.minecraftforge.client.ClientCommandHandler;

public class TriggerRegister {
    public static OnChatTrigger registerChat(String methodName) {
        OnChatTrigger trigger = new OnChatTrigger(methodName);
        TriggerType.CHAT.addTrigger(trigger);

        return trigger;
    }

    public static OnRegularTrigger registerWorldLoad(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.WORLD_LOAD);
        TriggerType.WORLD_LOAD.addTrigger(trigger);

        return trigger;
    }

    public static OnRegularTrigger registerWorldUnload(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.WORLD_UNLOAD);
        TriggerType.WORLD_UNLOAD.addTrigger(trigger);

        return trigger;
    }

    public static OnSoundPlayTrigger registerSoundPlay(String methodName) {
        OnSoundPlayTrigger trigger = new OnSoundPlayTrigger(methodName);
        TriggerType.SOUND_PLAY.addTrigger(trigger);

        return trigger;
    }

    public static OnRegularTrigger registerTick(String methodName) {
        OnRegularTrigger trigger = new OnRegularTrigger(methodName, TriggerType.TICK);
        TriggerType.TICK.addTrigger(trigger);

        return trigger;
    }

    public static OnStepTrigger registerStep(String methodName) {
        OnStepTrigger trigger = new OnStepTrigger(methodName);
        TriggerType.STEP.addTrigger(trigger);

        return trigger;
    }

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