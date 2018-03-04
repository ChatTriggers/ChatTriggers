package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.utils.console.Console;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TriggerRegister {
    public static Module currentModule = null;

    /**
     * Helper method to make registering a trigger more like JavaScript.<br>
     * Used from provided libraries as <code>register("trigger type", "function name");</code><br>
     * Example: <code>register("chat","triggerOnChat");</code>
     *
     * @param triggerType the type of trigger
     * @param methodName  the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnTrigger register(String triggerType, String methodName) {
        String capitalizedName = triggerType.substring(0, 1).toUpperCase() + triggerType.substring(1);
        Method method;

        try {
            method = TriggerRegister.class.getDeclaredMethod(
                    "register" + capitalizedName,
                    String.class
            );
        } catch (NoSuchMethodException e) {
            Console.getConsole().printStackTrace(e);
            return null;
        }

        try {
            Object returned = method.invoke(null, methodName);
            return (OnTrigger) returned;
        } catch (IllegalAccessException | InvocationTargetException e) {
            Console.getConsole().printStackTrace(e);
            return null;
        }
    }

    /**
     * Registers a new chat trigger.<br>
     * Runs on arrival of a chat message before it gets drawn in chat.<br>
     * Passes through multiple arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;any number of chat criteria variables<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the chat event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnChatTrigger#setChatCriteria(String)} Sets the chat criteria<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnChatTrigger#setParameter(String)} Sets the chat parameter<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnChatTrigger registerChat(String methodName) {
        return new OnChatTrigger(methodName);
    }

    /**
     * Registers a new world load trigger.<br>
     * Runs every time a world loads.<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerWorldLoad(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.WORLD_LOAD);
    }

    /**
     * Registers a new world unload trigger.<br>
     * Runs every time a world unloads.<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerWorldUnload(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.WORLD_UNLOAD);
    }

    /**
     * Registers a new clicked trigger.<br>
     * Runs on both down and up action on the mouse for mouse buttons 0 through 5.<br>
     * Passes through 4 arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse x<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse y<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse button<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse button state<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerClicked(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.CLICKED);
    }

    /**
     * Registers a new dragged trigger.<br>
     * Runs while a mouse button is being held down.<br>
     * Passes through 5 arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse delta x<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse delta y<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse x<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse y<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;mouse button<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerDragged(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.DRAGGED);
    }

    /**
     * Registers a new sound play trigger.<br>
     * Runs every time a sound is played.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the sound event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnSoundPlayTrigger#setSoundNameCriteria(String)} Sets the sound name criteria<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnSoundPlayTrigger registerSoundPlay(String methodName) {
        return new OnSoundPlayTrigger(methodName);
    }

    /**
     * Registers a new tick trigger.<br>
     * Runs every game tick.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ticks elapsed<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerTick(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.TICK);
    }

    /**
     * Registers a new step trigger.<br>
     * Runs in predictable intervals, 60 times per second by default.<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnStepTrigger#setFps(long)} Sets the fps<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnStepTrigger#setDelay(long)} Sets the delay in seconds<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnStepTrigger registerStep(String methodName) {
        return new OnStepTrigger(methodName);
    }

    /**
     * Registers a new render overlay trigger.<br>
     * Runs when the overlay is getting drawn every frame.<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderOverlay(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_OVERLAY);
    }

    /**
     * Registers a new render player list trigger.<br>
     * Runs when the player list is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderPlayerList(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_PLAYER_LIST);
    }

    /**
     * Registers a new render crosshair trigger.<br>
     * Runs when the crosshair is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderCrosshair(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_CROSSHAIR);
    }

    /**
     * Registers a new render debug trigger.<br>
     * Runs when the debug screen (f3) is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderDebug(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_DEBUG);
    }

    /**
     * Registers a new render boss health trigger.<br>
     * Runs when the boss health bar is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderBossHealth(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_BOSS_HEALTH);
    }

    /**
     * Registers a new render health trigger.<br>
     * Runs when the player's health is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderHealth(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_HEALTH);
    }

    /**
     * Registers a new render food trigger.<br>
     * Runs when the player's food (hunger) is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderFood(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_FOOD);
    }

    /**
     * Registers a new render mount health trigger.<br>
     * Runs when the mount's health (Example: horse or pig) is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderMountHealth(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_MOUNT_HEALTH);
    }

    /**
     * Registers a new render experience trigger.<br>
     * Runs when the player's experience bar is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderExperience(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_EXPERIENCE);
    }

    /**
     * Registers a new render hotbar trigger.<br>
     * Runs when the player's hotbar is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderHotbar(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_HOTBAR);
    }

    /**
     * Registers a new render air trigger.<br>
     * Runs when the player's air level (underwater) is being drawn.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The render event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnRenderTrigger#triggerIfCanceled(Boolean)} Sets if triggered if event is already cancelled<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderAir(String methodName) {
        return new OnRenderTrigger(methodName, TriggerType.RENDER_AIR);
    }

    /**
     * Registers a new game load trigger.<br>
     * Runs directly after the game loads.<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerGameLoad(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.GAME_LOAD);
    }

    /**
     * Registers a new game unload trigger.<br>
     * Runs directly before the game unloads.<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerGameUnload(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.GAME_UNLOAD);
    }

    /**
     * Registers a new method that receives a command input.<br>
     * Runs when the command with matching name is run.<br>
     * Passes through multiple arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The arguments supplied to the command by the user<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnCommandTrigger#setCommandName(String)} Sets the command name<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnCommandTrigger registerCommand(String methodName) {
        return new OnCommandTrigger(methodName);
    }

    /**
     * Registers a new method that gets run when a new gui is opened.<br>
     * Runs when a gui is opened.<br>
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the gui opened event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger (useless)
     */
    public static OnRegularTrigger registerGuiOpened(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.GUI_OPENED);
    }
}