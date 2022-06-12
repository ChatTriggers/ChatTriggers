package com.chattriggers.ctjs.engine

import com.chattriggers.ctjs.minecraft.listeners.ClientListener
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Item
import com.chattriggers.ctjs.minecraft.wrappers.inventory.Slot
import com.chattriggers.ctjs.triggers.*
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions

@Suppress("unused")
interface IRegister {
    companion object {
        private val methodMap = mutableMapOf<String, KFunction<*>>()
    }

    /**
     * Helper method register a trigger.
     *
     * Called by taking the original name of the method, i.e. `registerChat`,
     * removing the word register, and comparing it case-insensitively with
     * the methods below.
     *
     * @param triggerType the type of trigger
     * @param method The name of the method or the actual method to callback when the event is fired
     * @return The trigger for additional modification
     */
    fun register(triggerType: String, method: Any): Trigger {
        val name = triggerType.lowercase()

        var func = methodMap[name]

        if (func == null) {
            func = this::class.memberFunctions.firstOrNull {
                it.name.lowercase() == "register$name"
            } ?: throw NoSuchMethodException("No trigger type named '$triggerType'")
            methodMap[name] = func
        }

        return func.call(this, method) as Trigger
    }

    /**
     * Registers a new trigger that runs before a chat message is received.
     *
     * Passes through multiple arguments:
     * - Any number of chat criteria variables
     * - The chat event, which can be cancelled
     *
     * Available modifications:
     * - [ChatTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [ChatTrigger.setChatCriteria] Sets the chat criteria
     * - [ChatTrigger.setParameter] Sets the chat parameter
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerChat(method: Any): ChatTrigger {
        return ChatTrigger(method, TriggerType.Chat, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before an action bar message is received.
     *
     * Passes through multiple arguments:
     * - Any number of chat criteria variables
     * - The chat event, which can be cancelled
     *
     * Available modifications:
     * - [ChatTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [ChatTrigger.setChatCriteria] Sets the chat criteria
     * - [ChatTrigger.setParameter] Sets the chat parameter
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerActionBar(method: Any): ChatTrigger {
        return ChatTrigger(method, TriggerType.ActionBar, getImplementationLoader())
    }

    /**
     * Registers a trigger that runs before the world loads.
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerWorldLoad(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.WorldLoad, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the world unloads.
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerWorldUnload(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.WorldUnload, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a mouse button is being pressed or released.
     *
     * Passes through four arguments:
     * - The mouse x position
     * - The mouse y position
     * - The mouse button
     * - The mouse button state (true if button is pressed, false otherwise)
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerClicked(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.Clicked, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the mouse is scrolled.
     *
     * Passes through three arguments:
     * - The mouse x position
     * - The mouse y position
     * - The scroll direction
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerScrolled(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.Scrolled, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs while a mouse button is being held down.
     *
     * Passes through five arguments:
     * - The mouse delta x position (relative to last frame)
     * - The mouse delta y position (relative to last frame)
     * - The mouse x position
     * - The mouse y position
     * - The mouse button
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerDragged(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.Dragged, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a sound is played.
     *
     * Passes through six arguments:
     * - The sound event's position
     * - The sound event's name
     * - The sound event's volume
     * - The sound event's pitch
     * - The sound event's category's name
     * - The sound event, which can be cancelled
     *
     * Available modifications:
     * - [SoundPlayTrigger.setCriteria] Sets the sound name criteria
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerSoundPlay(method: Any): SoundPlayTrigger {
        return SoundPlayTrigger(method, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a noteblock is played.
     *
     * Passes through four arguments:
     * - The note block play event's Vector3f position
     * - The note block play event's note's name
     * - The note block play event's octave
     * - The note block play event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerNoteBlockPlay(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.NoteBlockPlay, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a noteblock is changed.
     *
     * Passes through four arguments:
     * - The note block change event's Vector3f position
     * - The note block change event's note's name
     * - The note block change event's octave
     * - The note block change event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerNoteBlockChange(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.NoteBlockChange, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before every game tick.
     *
     * Passes through one argument:
     * - Ticks elapsed
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerTick(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.Tick, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs in predictable intervals. (60 per second by default)
     *
     * Passes through one argument:
     * - Steps elapsed
     *
     * Available modifications:
     * - [StepTrigger.setFps] Sets the fps, i.e. how many times this trigger will fire
     *      per second
     * - [StepTrigger.setDelay] Sets the delay in seconds, i.e. how many seconds it takes
     *      to fire. Overrides [StepTrigger.setFps].
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerStep(method: Any): StepTrigger {
        return StepTrigger(method, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the world is drawn.
     *
     * Passes through one argument:
     * - Partial ticks elapsed
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderWorld(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.RenderWorld, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the overlay is drawn.
     *
     * Passes through one argument:
     * - The render event, which cannot be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderOverlay(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderOverlay, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player list is being drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderPlayerList(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderPlayerList, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the crosshair is being drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderCrosshair(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderCrosshair, getImplementationLoader())
    }

    /**
     * Registers a trigger that runs before the debug screen is being drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderDebug(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderDebug, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the boss health bar is being drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderBossHealth(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderBossHealth, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's health is being drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderHealth(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderHealth, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's armor bar is drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderArmor(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderArmor, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's food is being drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderFood(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderFood, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's mount's health is being drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderMountHealth(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderMountHealth, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's experience is being drawn.
     *
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderExperience(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderExperience, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's hotbar is drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderHotbar(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderHotbar, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's air level is drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderAir(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderAir, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the portal effect is drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderPortal(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderPortal, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the jump bar is drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderJumpBar(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderJumpBar, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the chat is drawn.
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderChat(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderChat, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's helmet overlay is drawn.
     * This triggers when a pumpkin is on the player's head
     *
     * Passes through one argument:
     * - The render event, which can be cancelled
     *
     * Available modifications:
     * - [EventTrigger.triggerIfCanceled] Sets if triggered if event is already cancelled
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderHelmet(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderHelmet, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player's hand is drawn.
     *
     * Passes through one argument:
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderHand(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderHand, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the scoreboard is drawn.
     *
     * Passes through one argument:
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderScoreboard(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderScoreboard, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the title and subtitle are drawn.
     *
     * Passes through three arguments:
     * - The title
     * - The subtitle
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderTitle(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderTitle, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the block highlight box is drawn.
     *
     * Passes through two arguments:
     * - The draw block highlight event's position
     * - The draw block highlight event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerDrawBlockHighlight(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.BlockHighlight, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs after the game loads.
     *
     * This runs after the initial loading of the game directly after scripts are
     * loaded and after "/ct load" happens.
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGameLoad(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.GameLoad, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the game unloads.
     *
     * This runs before shutdown of the JVM and before "/ct load" happens.
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGameUnload(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.GameUnload, getImplementationLoader())
    }

    /**
     * Registers a new command that will run the method provided.
     *
     * Passes through multiple arguments:
     * - The arguments supplied to the command by the user
     *
     * Available modifications:
     * - [CommandTrigger.setCommandName] Sets the command name
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerCommand(method: Any): CommandTrigger {
        return CommandTrigger(method, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs when a new gui is first opened.
     *
     * Passes through one argument:
     * - The gui opened event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGuiOpened(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.GuiOpened, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs when a gui is closed.
     *
     * Passes through one argument:
     * - The gui that was closed
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGuiClosed(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.GuiClosed, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs when a player joins the world.
     *
     * Maximum is one per tick. Any extras will queue and run in later ticks.
     * This trigger is asynchronous.
     *
     * Passes through one argument:
     * - The [com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP] object
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPlayerJoined(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.PlayerJoin, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs when a player leaves the world.
     *
     * Maximum is one per tick. Any extras will queue and run in later ticks.
     * This trigger is asynchronous.
     *
     * Passes through one argument:
     * - The name of the player that left
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPlayerLeft(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.PlayerLeave, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before an item is picked up.
     *
     * Passes through five arguments:
     * - The [Item] that is picked up
     * - The [com.chattriggers.ctjs.minecraft.wrappers.entity.PlayerMP] that picked up the item
     * - The item's position vector
     * - The item's motion vector
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPickupItem(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.PickupItem, getImplementationLoader())
    }

    // TODO(BREAKING) Removed position, motion, player to make the event trigger for singleplayer too,
    // should still work like forge's but a little sooner.
    /**
     * Registers a new trigger that runs before an item is dropped.
     *
     * Passes through five arguments:
     * - The [Item] that is dropped up
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerDropItem(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.DropItem, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a screenshot is taken.
     *
     * Passes through two arguments:
     * - The name of the screenshot
     * - The screenshot event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerScreenshotTaken(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.ScreenshotTaken, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a message is sent in chat.
     *
     * Passes through two arguments:
     * - The message
     * - The message event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerMessageSent(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.MessageSent, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs when a tooltip is being rendered.
     * This allows for the user to modify what text is in the tooltip, and even the
     * ability to cancel rendering completely.
     *
     * Passes through three arguments:
     * - The list of lore to modify.
     * - The [Item] that this lore is attached to.
     * - The cancellable event.
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerItemTooltip(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.Tooltip, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player interacts.
     *
     * In 1.8.9, the following events will activate this trigger:
     * - Right clicking a block
     * - Right clicking the air
     *
     * In 1.12.2, the following events will activate this trigger:
     * - Left clicking a block
     * - Left clicking air
     * - Right clicking an entity
     * - Right clicking a block
     * - Right clicking an item
     * - Right clicking air
     *
     * Passes through three arguments:
     * - The [ClientListener.PlayerInteractAction]
     * - The position of the target as a Vector3f
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPlayerInteract(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.PlayerInteract, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the player breaks a block
     *
     * Passes through one argument:
     * - The block
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerBlockBreak(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.BlockBreak, getImplementationLoader())
    }

    // TODO(BREAKING): Remove this (it's just a worse attackentity)
    /**
     * Registers a new trigger that runs before an entity is damaged
     *
     * Passes through two arguments:
     * - The target Entity that is damaged
     * - The PlayerMP attacker
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerEntityDamage(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.EntityDamage, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before an entity dies
     *
     * Passes through one argument:
     * - The Entity that died
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerEntityDeath(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.EntityDeath, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the gui background is drawn
     * This is useful for drawing custom backgrounds.
     *
     * Passes through one argument:
     * - The [net.minecraft.client.gui.GuiScreen] that is being drawn
     *
     */
    fun registerGuiDrawBackground(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.GuiDrawBackground, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs as a gui is rendered
     *
     * Passes through three arguments:
     * - The mouse x position
     * - The mouse y position
     * - The gui
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGuiRender(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.GuiRender, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever a key is typed with a gui open
     *
     * Passes through four arguments:
     * - The character pressed (e.g. 'd')
     * - The key code pressed (e.g. 41)
     * - The gui
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGuiKey(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.GuiKey, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever the mouse is clicked with a
     * gui open
     *
     * Passes through five arguments:
     * - The mouse x position
     * - The mouse y position
     * - The mouse button
     * - The gui
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGuiMouseClick(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.GuiMouseClick, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever a mouse button is released
     * with a gui open
     *
     * Passes through five arguments:
     * - The mouse x position
     * - The mouse y position
     * - The mouse button
     * - The gui
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGuiMouseRelease(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.GuiMouseRelease, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever a mouse button held and dragged
     * with a gui open
     *
     * Passes through five arguments:
     * - The mouse x position
     * - The mouse y position
     * - The mouse button
     * - The gui
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerGuiMouseDrag(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.GuiMouseDrag, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever a packet is sent from the client to the server
     *
     * Passes through two arguments:
     * - The packet
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPacketSent(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.PacketSent, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever a packet is sent to the client from the server
     *
     * Passes through two arguments:
     * - The packet
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPacketReceived(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.PacketReceived, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever the player connects to a server
     *
     * Passes through one argument:
     * - The event, which cannot be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerServerConnect(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.ServerConnect, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever the player disconnects from a server
     *
     * Passes through two arguments:
     * - The event, which cannot be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerServerDisconnect(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.ServerDisconnect, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever the user clicks on a clickable
     * chat component
     *
     * Passes through two arguments:
     * - The [gg.essential.universal.wrappers.message.UTextComponent]
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerChatComponentClicked(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.ChatComponentClicked, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever the user hovers over a
     * hoverable chat component
     *
     * Passes through two arguments:
     * - The [gg.essential.universal.wrappers.message.UTextComponent]
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerChatComponentHovered(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.ChatComponentHovered, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever an entity is rendered
     *
     * Passes through four arguments:
     * - The [com.chattriggers.ctjs.minecraft.wrappers.entity.Entity]
     * - The position as a Vector3f
     * - The partial ticks
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderEntity(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderEntity, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs after an entity is rendered
     *
     * Passes through three arguments:
     * - The [com.chattriggers.ctjs.minecraft.wrappers.entity.Entity]
     * - The position as a Vector3f
     * - The partial ticks
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPostRenderEntity(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.PostRenderEntity, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever a tile entity is rendered
     *
     * Passes through four arguments:
     * - The TileEntity
     * - The position as a Vector3f
     * - The partial ticks
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderTileEntity(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderTileEntity, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs after a tile entity is rendered
     *
     * Passes through three arguments:
     * - The TileEntity
     * - The position as a Vector3f
     * - The partial ticks
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPostRenderTileEntity(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.PostRenderTileEntity, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs after the current screen is rendered
     *
     * Passes through three arguments:
     * - The mouseX
     * - The mouseY
     * - The GuiScreen
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPostGuiRender(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.PostGuiRender, getImplementationLoader())
    }

    // TODO(BREAKING): Remove in favor of slot triggers?
    /**
     * Registers a new trigger that runs before the items in the gui are drawn
     *
     * Passes through five arguments:
     * - The mouseX position
     * - The mouseY position
     * - The MC Slot
     * - The GuiContainer
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerPreItemRender(method: Any): RegularTrigger {
        return RegularTrigger(method, TriggerType.PreItemRender, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before a slot is drawn in a container
     * This is useful for hiding "background" items in containers used as GUIs.
     *
     * Passes through three arguments:
     * - The [Slot] being drawn
     * - The MC GUIScreen that is being drawn
     * - The event, which can be cancelled
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderSlot(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderSlot, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before each item is drawn into a GUI.
     *
     * Passes through four arguments:
     * - The [Item]
     * - The x position
     * - The y position
     * - The event, which can be cancelled.
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderItemIntoGui(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderItemIntoGui, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before each item overlay (stack size and damage bar) is drawn.
     *
     * Passes through four arguments:
     * - The [Item]
     * - The x position
     * - The y position
     * - The event, which can be cancelled.
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderItemOverlayIntoGui(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderItemOverlayIntoGui, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the hovered slot square is drawn.
     *
     * Passes through six arguments:
     * - The mouseX position
     * - The mouseY position
     * - The MC Slot
     * - The GuiContainer
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerRenderSlotHighlight(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.RenderSlotHighlight, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs whenever a particle is spawned
     *
     * Passes through three arguments:
     * - The [com.chattriggers.ctjs.minecraft.wrappers.entity.Particle]
     * - The [net.minecraft.util.EnumParticleTypes]
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerSpawnParticle(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.SpawnParticle, getImplementationLoader())
    }

    // TODO(BREAKING): Collapse into left/right click triggers
    /**
     * Registers a new trigger that runs whenever the player has left clicked on an entity
     *
     * Passes through three arguments:
     * - The [com.chattriggers.ctjs.minecraft.wrappers.entity.Entity] that is being hit
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerAttackEntity(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.AttackEntity, getImplementationLoader())
    }

    // TODO(BREAKING): Added Hand argument
    /**
     * Registers a new trigger that runs whenever a block is left clicked
     *
     * Note: this is not continuously called while the block is being broken, only once
     * when first left clicked. The hand argument will always be LEFT in 1.8.9.
     *
     * Passes through two arguments:
     * - The [com.chattriggers.ctjs.minecraft.wrappers.world.block.Block] being hit
     * - The [com.chattriggers.ctjs.minecraft.wrappers.Player.Hand] used to hit the block
     * - The event, which can be cancelled
     *
     * Available modifications:
     * - [Trigger.setPriority] Sets the priority
     *
     * @param method The method to call when the event is fired
     * @return The trigger for additional modification
     */
    fun registerHitBlock(method: Any): EventTrigger {
        return EventTrigger(method, TriggerType.HitBlock, getImplementationLoader())
    }

    fun getImplementationLoader(): ILoader
}
