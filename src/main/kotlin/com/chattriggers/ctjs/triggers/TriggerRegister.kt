package com.chattriggers.ctjs.triggers

import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.modules.Module
import com.chattriggers.ctjs.utils.console.Console

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 *
 * This class is used to register new triggers to run function on minecraft events
 * <br></br>
 *
 * "while" means repeating until the action is stopped
 *
 * "before" means directly before within the same game tick
 *
 * "after" means directly after within the same game tick
 * <br></br>
 *
 * Most triggers are synchronous meaning they will happen at the same time as the event.<br></br>
 * Triggers that are asynchronous will be labeled as such
 */
object TriggerRegister {
    @JvmStatic
    var currentModule: Module? = null

    /**
     * Helper method to make registering a trigger more like JavaScript.<br></br>
     * Used from provided libraries as `register("trigger type", "function name");`<br></br>
     * Example: `register("chat","triggerOnChat");`
     *
     * @param triggerType the type of trigger
     * @param method the name of the method or the actual method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun register(triggerType: String, method: Any): OnTrigger? {
        val registerMethod: Method

        try {
            registerMethod = TriggerRegister.javaClass.getDeclaredMethod(
                "register${triggerType.capitalize()}",
                Any::class.java
            )
        } catch (e: NoSuchMethodException) {
            Console.getInstance().printStackTrace(e)
            return null
        }

        return try {
            val returned = registerMethod.invoke(null, method)
            returned as OnTrigger
        } catch (e: IllegalAccessException) {
            Console.getInstance().printStackTrace(e)
            null
        } catch (e: InvocationTargetException) {
            Console.getInstance().printStackTrace(e)
            null
        }

    }

    /**
     * Registers a new trigger that runs before a chat message is received.<br></br>
     *
     *
     * Passes through multiple arguments:<br></br>
     * any number of chat criteria variables<br></br>
     * the chat event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnChatTrigger.setChatCriteria] Sets the chat criteria<br></br>
     * [OnChatTrigger.setParameter] Sets the chat parameter<br></br>
     * [OnTrigger.setPriority] Sets the priority<br></br>
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerChat(method: Any) = OnChatTrigger(method, TriggerType.CHAT)

    /**
     * Registers a new trigger that runs before an action bar message is received.<br></br>
     *
     *
     * Passes through multiple arguments:<br></br>
     * any number of chat criteria variables<br></br>
     * the chat event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnChatTrigger.setChatCriteria] Sets the chat criteria<br></br>
     * [OnChatTrigger.setParameter] Sets the chat parameter<br></br>
     * [OnTrigger.setPriority] Sets the priority<br></br>
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerActionBar(method: Any) = OnChatTrigger(method, TriggerType.ACTION_BAR)

    /**
     * Registers a trigger that runs before the world loads.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerWorldLoad(method: Any) = OnRegularTrigger(method, TriggerType.WORLD_LOAD)

    /**
     * Registers a new trigger that runs before the world unloads.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerWorldUnload(method: Any) = OnRegularTrigger(method, TriggerType.WORLD_UNLOAD)

    /**
     * Registers a new trigger that runs before a mouse button is being pressed or released.<br></br>
     *
     *
     * Passes through 4 arguments:<br></br>
     * mouse x<br></br>
     * mouse y<br></br>
     * mouse button<br></br>
     * mouse button state<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerClicked(method: Any) = OnRegularTrigger(method, TriggerType.CLICKED)

    /**
     * Registers a new trigger that runs while a mouse button is being held down.<br></br>
     *
     *
     * Passes through 5 arguments:<br></br>
     * mouse delta x<br></br>
     * mouse delta y<br></br>
     * mouse x<br></br>
     * mouse y<br></br>
     * mouse button<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerDragged(method: Any) = OnRegularTrigger(method, TriggerType.DRAGGED)

    /**
     * Registers a new trigger that runs before a sound is played.<br></br>
     *
     *
     * Passes through 6 arguments:<br></br>
     * the sound event<br></br>
     * the sound event's position<br></br>
     * the sound event's name<br></br>
     * the sound event's volume<br></br>
     * the sound event's pitch<br></br>
     * the sound event's category's name<br></br>
     * Available modifications:<br></br>
     * [OnSoundPlayTrigger.setSoundNameCriteria] Sets the sound name criteria<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerSoundPlay(method: Any) = OnSoundPlayTrigger(method)

    /**
     * Registers a new trigger that runs before a noteblock is played.<br></br>
     *
     *
     * Passes through 4 arguments:<br></br>
     * the note block play event<br></br>
     * the note block play event's Vector3d position<br></br>
     * the note block play event's note's name<br></br>
     * the note block play event's octave<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerNoteBlockPlay(method: Any) = OnRegularTrigger(method, TriggerType.NOTE_BLOCK_PLAY)

    /**
     * Registers a new trigger that runs before a noteblock is changed.<br></br>
     *
     *
     * Passes through 4 arguments:<br></br>
     * the note block change event<br></br>
     * the note block change event's Vector3d position<br></br>
     * the note block change event's note's name<br></br>
     * the note block change event's octave<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerNoteBlockChange(method: Any) = OnRegularTrigger(method, TriggerType.NOTE_BLOCK_CHANGE)

    /**
     * Registers a new trigger that runs before every game tick.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * ticks elapsed<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerTick(method: Any) = OnRegularTrigger(method, TriggerType.TICK)

    /**
     * Registers a new trigger that runs in predictable intervals. (60 per second by default)<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * steps elapsed<br></br>
     * Available modifications:<br></br>
     * [OnStepTrigger.setFps] Sets the fps<br></br>
     * [OnStepTrigger.setDelay] Sets the delay in seconds<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerStep(method: Any) = OnStepTrigger(method)

    /**
     * Registers a new trigger that runs before the world is drawn.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderWorld(method: Any) = OnRegularTrigger(method, TriggerType.RENDER_WORLD)

    /**
     * Registers a new trigger that runs before the overlay is drawn.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderOverlay(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_OVERLAY)

    /**
     * Registers a new trigger that runs before the player list is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderPlayerList(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_PLAYER_LIST)

    /**
     * Registers a new trigger that runs before the crosshair is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderCrosshair(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_CROSSHAIR)

    /**
     * Registers a trigger that runs before the debug screen is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderDebug(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_DEBUG)

    /**
     * Registers a new trigger that runs before the boss health bar is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderBossHealth(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_BOSS_HEALTH)

    /**
     * Registers a new trigger that runs before the player's health is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderHealth(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_HEALTH)

    /**
     * Registers a new trigger that runs before the player's food is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderFood(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_FOOD)

    /**
     * Registers a new trigger that runs before the player's mount's health is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderMountHealth(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_MOUNT_HEALTH)

    /**
     * Registers a new trigger that runs before the player's experience is being drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderExperience(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_EXPERIENCE)

    /**
     * Registers a new trigger that runs before the player's hotbar is drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderHotbar(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_HOTBAR)

    /**
     * Registers a new trigger that runs before the player's air level is drawn.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * The render event<br></br>
     * Available modifications:<br></br>
     * [OnRenderTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerRenderAir(method: Any) = OnRenderTrigger(method, TriggerType.RENDER_AIR)

    /**
     * Registers a new trigger that runs before the block highlight box is drawn.<br></br>
     *
     *
     * Passes through 2 arguments:<br></br>
     * The draw block highlight event<br></br>
     * The draw block highlight event's position<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerDrawBlockHighlight(method: Any) = OnRegularTrigger(method, TriggerType.BLOCK_HIGHLIGHT)

    /**
     * Registers a new trigger that runs after the game loads.<br></br>
     * This runs after the initial loading of the game directly after scripts are loaded and after "/ct load" happens.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerGameLoad(method: Any) = OnRegularTrigger(method, TriggerType.GAME_LOAD)

    /**
     * Registers a new trigger that runs before the game unloads.<br></br>
     * This runs before shutdown of the JVM and before "/ct load" happens.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerGameUnload(method: Any) = OnRegularTrigger(method, TriggerType.GAME_UNLOAD)

    /**
     * Registers a new command that will run the method provided.<br></br>
     *
     *
     * Passes through multiple arguments:<br></br>
     * The arguments supplied to the command by the user<br></br>
     * Available modifications:<br></br>
     * [OnCommandTrigger.setCommandName] Sets the command name<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerCommand(method: Any) = OnCommandTrigger(method)

    /**
     * Registers a new trigger that runs when a new gui is first opened.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * the gui opened event<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerGuiOpened(method: Any) = OnRegularTrigger(method, TriggerType.GUI_OPENED)

    /**
     * Registers a new trigger that runs when a player joins the world.<br></br>
     * Maximum is one per tick. Any extras will queue and run in later ticks.<br></br>
     * This trigger is asynchronous.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * the [com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP] object<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerPlayerJoined(method: Any) = OnRegularTrigger(method, TriggerType.PLAYER_JOIN)

    /**
     * Registers a new trigger that runs when a player leaves the world.<br></br>
     * Maximum is one per tick. Any extras will queue and run in later ticks.<br></br>
     * This trigger is asynchronous.<br></br>
     *
     *
     * Passes through 1 argument:<br></br>
     * the name of the player that left<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerPlayerLeft(method: Any) = OnRegularTrigger(method, TriggerType.PLAYER_LEAVE)

    /**
     * Registers a new trigger that runs before an item is picked up.<br></br>
     *
     *
     * Passes through 3 arguments:<br></br>
     * the [Item] that is picked up<br></br>
     * the [com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP] that picked up the item<br></br>
     * the item's position vector<br></br>
     * the item's motion vector<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerPickupItem(method: Any) = OnRegularTrigger(method, TriggerType.PICKUP_ITEM)

    /**
     * Registers a new trigger that runs before an item is dropped.<br></br>
     *
     *
     * Passes through 3 arguments:<br></br>
     * the [Item] that is dropped up<br></br>
     * the [com.chattriggers.ctjs.minecraft.wrappers.objects.PlayerMP] that dropped the item<br></br>
     * the item's position vector<br></br>
     * the item's motion vector<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerDropItem(method: Any) = OnRegularTrigger(method, TriggerType.DROP_ITEM)

    /**
     * Registers a new trigger that runs before a screenshot is taken.<br></br>
     *
     *
     * Passes through 2 arguments:<br></br>
     * the name of the screenshot<br></br>
     * the screenshot event<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerScreenshotTaken(method: Any) = OnRegularTrigger(method, TriggerType.SCREENSHOT_TAKEN)

    /**
     * Registers a new trigger that runs before a message is sent in chat.<br></br>
     *
     *
     * Passes through 2 arguments:<br></br>
     * the message event<br></br>
     * the message<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerMessageSent(method: Any) = OnRegularTrigger(method, TriggerType.MESSAGE_SENT)

    /**
     * Registers a new trigger that runs before a message is sent in chat.<br></br>
     *
     *
     * Passes through 2 arguments:<br></br>
     * the list of lore to modify<br></br>
     * the [Item] that this lore is attached to.<br></br>
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    @JvmStatic
	fun registerItemTooltip(method: Any) = OnRegularTrigger(method, TriggerType.TOOLTIP)
}