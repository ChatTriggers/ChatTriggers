package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.utils.console.Console;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>This class is used to register new triggers to run function on minecraft events</p>
 * <br>
 * <p>"while" means repeating until the action is stopped</p>
 * <p>"before" means directly before within the same game tick</p>
 * <p>"after" means directly after within the same game tick</p>
 * <br>
 * <p>Most triggers are synchronous meaning they will happen at the same time as the event.</br>
 * Triggers that are asynchronous will be labeled as such</p>
 */
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
     * Registers a new trigger that runs before a chat message is received.<br>
     * 
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
     * Registers a trigger that runs before the world loads.<br>
     * 
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
     * Registers a new trigger that runs before the world unloads.<br>
     * 
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
     * Registers a new trigger that runs before a mouse button is being pressed or released.<br>
     * 
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
     * Registers a new trigger that runs while a mouse button is being held down.<br>
     * 
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
     * Registers a new trigger that runs before a sound is played.<br>
     * 
     * Passes through 6 arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the sound event<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the sound event's position<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the sound event's name<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the sound event's volume<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the sound event's pitch<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the sound event's category's name<br>
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
     * Registers a new trigger that runs before a noteblock is played.<br>
     * 
     * Passes through 4 arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the note block play event<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the note block play event's Vector3d position<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the note block play event's note's name<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the note block play event's octave<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerNoteBlockPlay(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.NOTE_BLOCK_PLAY);
    }

    /**
     * Registers a new trigger that runs before a noteblock is changed.<br>
     * 
     * Passes through 4 arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the note block change event<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the note block change event's Vector3d position<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the note block change event's note's name<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the note block change event's octave<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerNoteBlockChange(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.NOTE_BLOCK_CHANGE);
    }

    /**
     * Registers a new trigger that runs before every game tick.<br>
     * 
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
     * Registers a new trigger that runs in predictable intervals. (60 per second by default)<br>
     * 
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
     * Registers a new trigger that runs before the world is drawn.<br>
     * 
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerRenderWorld(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.RENDER_WORLD);
    }

    /**
     * Registers a new trigger that runs before the overlay is drawn.<br>
     * 
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
     * Registers a new trigger that runs before the player list is being drawn.<br>
     * 
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
     * Registers a new trigger that runs before the crosshair is being drawn.<br>
     * 
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
     * Registers a trigger that runs before the debug screen is being drawn.<br>
     * 
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
     * Registers a new trigger that runs before the boss health bar is being drawn.<br>
     * 
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
     * Registers a new trigger that runs before the player's health is being drawn.<br>
     * 
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
     * Registers a new trigger that runs before the player's food is being drawn.<br>
     * 
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
     * Registers a new trigger that runs before the player's mount's health is being drawn.<br>
     * 
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
     * Registers a new trigger that runs before the player's experience is being drawn.<br>
     * 
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
     * Registers a new trigger that runs before the player's hotbar is drawn.<br>
     * 
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
     * Registers a new trigger that runs before the player's air level is drawn.<br>
     * 
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
     * Registers a new trigger that runs before the block highlight box is drawn.<br>
     * 
     * Passes through 2 arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The draw block highlight event<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The draw block highlight event's position<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerDrawBlockHighlight(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.BLOCK_HIGHLIGHT);
    }

    /**
     * Registers a new trigger that runs after the game loads.<br>
     * This runs after the initial loading of the game directly after scripts are loaded and after "/ct load" happens.<br>
     * 
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
     * Registers a new trigger that runs before the game unloads.<br>
     * This runs before shutdown of the JVM and before "/ct load" happens.<br>
     * 
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
     * Registers a new command that will run the method provided.<br>
     *
     * Passes through multiple arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The arguments supplied to the command by the user<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnCommandTrigger#setCommandName(String)} Sets the command name<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnCommandTrigger registerCommand(String methodName) {
        return new OnCommandTrigger(methodName);
    }

    /**
     * Registers a new trigger that runs when a new gui is first opened.<br>
     * 
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the gui opened event<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerGuiOpened(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.GUI_OPENED);
    }

    /**
     * Registers a new trigger that runs when a player joins the world.<br>
     * Maximum is one per tick. Any extras will queue and run in later ticks.<br>
     * This trigger is asynchronous.<br>
     * 
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the {@link com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP} object<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnPlayerTrigger registerPlayerJoined(String methodName) {
        return new OnPlayerTrigger(methodName);
    }

    /**
     * Registers a new trigger that runs when a player leaves the world.<br>
     * Maximum is one per tick. Any extras will queue and run in later ticks.<br>
     * This trigger is asynchronous.<br>
     * 
     * Passes through 1 argument:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the name of the player that left<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerPlayerLeft(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.PLAYER_LEAVE);
    }

    /**
     * Registers a new trigger that runs before an item is picked up.<br>
     *
     * Passes through 3 arguments:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the {@link com.chattriggers.ctjs.minecraft.wrappers.objects.Item} that is picked up<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the item's position vector<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the item's motion vector<br>
     * Available modifications:<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     *
     * @param methodName the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerPickupItem(String methodName) {
        return new OnRegularTrigger(methodName, TriggerType.PICKUP_ITEM);
    }
}