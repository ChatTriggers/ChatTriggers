package com.chattriggers.ctjs.triggers;

import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item;
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
 * <p>Most triggers are synchronous meaning they will happen at the same time as the event.<br>
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
     * @param method the name of the method or the actual method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnTrigger register(String triggerType, Object method) {
        String capitalizedName = triggerType.substring(0, 1).toUpperCase() + triggerType.substring(1);
        Method registerMethod;

        try {
            registerMethod = TriggerRegister.class.getDeclaredMethod(
                    "register" + capitalizedName,
                    Object.class
            );
        } catch (NoSuchMethodException e) {
            Console.getInstance().printStackTrace(e);
            return null;
        }

        try {
            Object returned = registerMethod.invoke(null, method);
            return (OnTrigger) returned;
        } catch (IllegalAccessException | InvocationTargetException e) {
            Console.getInstance().printStackTrace(e);
            return null;
        }
    }

    /**
     * Registers a new trigger that runs before a chat message is received.<br>
     * <p>
     *     Passes through multiple arguments:<br>
     *         &emsp;any number of chat criteria variables<br>
     *         &emsp;the chat event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnChatTrigger#setChatCriteria(String)} Sets the chat criteria<br>
     *         &emsp;{@link OnChatTrigger#setParameter(String)} Sets the chat parameter<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority<br>
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnChatTrigger registerChat(Object method) {
        return new OnChatTrigger(method);
    }

    /**
     * Registers a trigger that runs before the world loads.<br>
     * <p>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerWorldLoad(Object method) {
        return new OnRegularTrigger(method, TriggerType.WORLD_LOAD);
    }

    /**
     * Registers a new trigger that runs before the world unloads.<br>
     * <p>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerWorldUnload(Object method) {
        return new OnRegularTrigger(method, TriggerType.WORLD_UNLOAD);
    }

    /**
     * Registers a new trigger that runs before a mouse button is being pressed or released.<br>
     * <p>
     *     Passes through 4 arguments:<br>
     *         &emsp;mouse x<br>
     *         &emsp;mouse y<br>
     *         &emsp;mouse button<br>
     *         &emsp;mouse button state<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerClicked(Object method) {
        return new OnRegularTrigger(method, TriggerType.CLICKED);
    }

    /**
     * Registers a new trigger that runs while a mouse button is being held down.<br>
     * <p>
     *     Passes through 5 arguments:<br>
     *         &emsp;mouse delta x<br>
     *         &emsp;mouse delta y<br>
     *         &emsp;mouse x<br>
     *         &emsp;mouse y<br>
     *         &emsp;mouse button<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerDragged(Object method) {
        return new OnRegularTrigger(method, TriggerType.DRAGGED);
    }

    /**
     * Registers a new trigger that runs before a sound is played.<br>
     * <p>
     *     Passes through 6 arguments:<br>
     *         &emsp;the sound event<br>
     *         &emsp;the sound event's position<br>
     *         &emsp;the sound event's name<br>
     *         &emsp;the sound event's volume<br>
     *         &emsp;the sound event's pitch<br>
     *         &emsp;the sound event's category's name<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnSoundPlayTrigger#setSoundNameCriteria(String)} Sets the sound name criteria<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnSoundPlayTrigger registerSoundPlay(Object method) {
        return new OnSoundPlayTrigger(method);
    }

    /**
     * Registers a new trigger that runs before a noteblock is played.<br>
     * <p>
     *     Passes through 4 arguments:<br>
     *         &emsp;the note block play event<br>
     *         &emsp;the note block play event's Vector3d position<br>
     *         &emsp;the note block play event's note's name<br>
     *         &emsp;the note block play event's octave<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerNoteBlockPlay(Object method) {
        return new OnRegularTrigger(method, TriggerType.NOTE_BLOCK_PLAY);
    }

    /**
     * Registers a new trigger that runs before a noteblock is changed.<br>
     * <p>
     *     Passes through 4 arguments:<br>
     *         &emsp;the note block change event<br>
     *         &emsp;the note block change event's Vector3d position<br>
     *         &emsp;the note block change event's note's name<br>
     *         &emsp;the note block change event's octave<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerNoteBlockChange(Object method) {
        return new OnRegularTrigger(method, TriggerType.NOTE_BLOCK_CHANGE);
    }

    /**
     * Registers a new trigger that runs before every game tick.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;ticks elapsed<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerTick(Object method) {
        return new OnRegularTrigger(method, TriggerType.TICK);
    }

    /**
     * Registers a new trigger that runs in predictable intervals. (60 per second by default)<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;steps elapsed<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnStepTrigger#setFps(long)} Sets the fps<br>
     *         &emsp;{@link OnStepTrigger#setDelay(long)} Sets the delay in seconds<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnStepTrigger registerStep(Object method) {
        return new OnStepTrigger(method);
    }

    /**
     * Registers a new trigger that runs before the world is drawn.<br>
     * <p>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerRenderWorld(Object method) {
        return new OnRegularTrigger(method, TriggerType.RENDER_WORLD);
    }

    /**
     * Registers a new trigger that runs before the overlay is drawn.<br>
     * <p>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderOverlay(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_OVERLAY);
    }

    /**
     * Registers a new trigger that runs before the player list is being drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderPlayerList(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_PLAYER_LIST);
    }

    /**
     * Registers a new trigger that runs before the crosshair is being drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderCrosshair(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_CROSSHAIR);
    }

    /**
     * Registers a trigger that runs before the debug screen is being drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderDebug(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_DEBUG);
    }

    /**
     * Registers a new trigger that runs before the boss health bar is being drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderBossHealth(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_BOSS_HEALTH);
    }

    /**
     * Registers a new trigger that runs before the player's health is being drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderHealth(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_HEALTH);
    }

    /**
     * Registers a new trigger that runs before the player's food is being drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderFood(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_FOOD);
    }

    /**
     * Registers a new trigger that runs before the player's mount's health is being drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderMountHealth(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_MOUNT_HEALTH);
    }

    /**
     * Registers a new trigger that runs before the player's experience is being drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderExperience(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_EXPERIENCE);
    }

    /**
     * Registers a new trigger that runs before the player's hotbar is drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderHotbar(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_HOTBAR);
    }

    /**
     * Registers a new trigger that runs before the player's air level is drawn.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;The render event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnRenderTrigger#triggerIfCanceled(boolean)} Sets if triggered if event is already cancelled<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRenderTrigger registerRenderAir(Object method) {
        return new OnRenderTrigger(method, TriggerType.RENDER_AIR);
    }

    /**
     * Registers a new trigger that runs before the block highlight box is drawn.<br>
     * <p>
     *     Passes through 2 arguments:<br>
     *         &emsp;The draw block highlight event<br>
     *         &emsp;The draw block highlight event's position<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerDrawBlockHighlight(Object method) {
        return new OnRegularTrigger(method, TriggerType.BLOCK_HIGHLIGHT);
    }

    /**
     * Registers a new trigger that runs after the game loads.<br>
     * This runs after the initial loading of the game directly after scripts are loaded and after "/ct load" happens.<br>
     * <p>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerGameLoad(Object method) {
        return new OnRegularTrigger(method, TriggerType.GAME_LOAD);
    }

    /**
     * Registers a new trigger that runs before the game unloads.<br>
     * This runs before shutdown of the JVM and before "/ct load" happens.<br>
     * <p>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerGameUnload(Object method) {
        return new OnRegularTrigger(method, TriggerType.GAME_UNLOAD);
    }

    /**
     * Registers a new command that will run the method provided.<br>
     * <p>
     *     Passes through multiple arguments:<br>
     *         &emsp;The arguments supplied to the command by the user<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnCommandTrigger#setCommandName(String)} Sets the command name<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnCommandTrigger registerCommand(Object method) {
        return new OnCommandTrigger(method);
    }

    /**
     * Registers a new trigger that runs when a new gui is first opened.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;the gui opened event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerGuiOpened(Object method) {
        return new OnRegularTrigger(method, TriggerType.GUI_OPENED);
    }

    /**
     * Registers a new trigger that runs when a player joins the world.<br>
     * Maximum is one per tick. Any extras will queue and run in later ticks.<br>
     * This trigger is asynchronous.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;the {@link com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP} object<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerPlayerJoined(Object method) {
        return new OnRegularTrigger(method, TriggerType.PLAYER_JOIN);
    }

    /**
     * Registers a new trigger that runs when a player leaves the world.<br>
     * Maximum is one per tick. Any extras will queue and run in later ticks.<br>
     * This trigger is asynchronous.<br>
     * <p>
     *     Passes through 1 argument:<br>
     *         &emsp;the name of the player that left<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerPlayerLeft(Object method) {
        return new OnRegularTrigger(method, TriggerType.PLAYER_LEAVE);
    }

    /**
     * Registers a new trigger that runs before an item is picked up.<br>
     * <p>
     *     Passes through 3 arguments:<br>
     *         &emsp;the {@link Item} that is picked up<br>
     *         &emsp;the {@link com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP} that picked up the item<br>
     *         &emsp;the item's position vector<br>
     *         &emsp;the item's motion vector<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerPickupItem(Object method) {
        return new OnRegularTrigger(method, TriggerType.PICKUP_ITEM);
    }

    /**
     * Registers a new trigger that runs before an item is dropped.<br>
     * <p>
     *     Passes through 3 arguments:<br>
     *         &emsp;the {@link Item} that is dropped up<br>
     *         &emsp;the {@link com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP} that dropped the item<br>
     *         &emsp;the item's position vector<br>
     *         &emsp;the item's motion vector<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerDropItem(Object method) {
        return new OnRegularTrigger(method, TriggerType.DROP_ITEM);
    }

    /**
     * Registers a new trigger that runs before a screenshot is taken.<br>
     * <p>
     *     Passes through 2 arguments:<br>
     *         &emsp;the name of the screenshot<br>
     *         &emsp;the screenshot event<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerScreenshotTaken(Object method) {
        return new OnRegularTrigger(method, TriggerType.SCREENSHOT_TAKEN);
    }

    /**
     * Registers a new trigger that runs before a message is sent in chat.<br>
     * <p>
     *     Passes through 2 arguments:<br>
     *         &emsp;the message event<br>
     *         &emsp;the message<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerMessageSent(Object method) {
        return new OnRegularTrigger(method, TriggerType.MESSAGE_SENT);
    }

    /**
     * Registers a new trigger that runs before a message is sent in chat.<br>
     * <p>
     *     Passes through 2 arguments:<br>
     *         &emsp;the list of lore to modify<br>
     *         &emsp;the {@link Item} that this lore is attached to.<br>
     *     Available modifications:<br>
     *         &emsp;{@link OnTrigger#setPriority(OnTrigger.Priority)} Sets the priority
     * </p>
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    public static OnRegularTrigger registerItemTooltip(Object method) {
        return new OnRegularTrigger(method, TriggerType.TOOLTIP);
    }
}