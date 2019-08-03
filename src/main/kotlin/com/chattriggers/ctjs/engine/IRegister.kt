package com.chattriggers.ctjs.engine

import com.chattriggers.ctjs.engine.module.Module
import com.chattriggers.ctjs.minecraft.wrappers.objects.inventory.Item
import com.chattriggers.ctjs.triggers.*
import kotlin.reflect.full.memberFunctions

interface IRegister {
    companion object {
        var currentModule: Module? = null
    }

    /**
     * Helper method register a trigger. <br/>
     * Called by taking the original name of the method, i.e. `registerChat`,
     * removing the word register, and making the first letter lowercase.
     *
     * @param triggerType the type of trigger
     * @param method the name of the method or the actual method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun register(triggerType: String, method: Any): OnTrigger {
        val name = triggerType.toLowerCase()

        val func = this::class.memberFunctions.firstOrNull {
            it.name.toLowerCase() == "register$name"
        }

        //println("params for func ${func?.name}: ${func?.parameters?.toString()}")

        return func?.call(this, method) as OnTrigger? ?: throw NoSuchMethodException()
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
    fun registerChat(method: Any): OnChatTrigger {
        return OnChatTrigger(method, TriggerType.CHAT, currentModule, getImplementationLoader())
    }

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
    fun registerActionBar(method: Any): OnChatTrigger {
        return OnChatTrigger(method, TriggerType.ACTION_BAR, currentModule, getImplementationLoader())
    }

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
    fun registerWorldLoad(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.WORLD_LOAD, currentModule, getImplementationLoader())
    }

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
    fun registerWorldUnload(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.WORLD_UNLOAD, currentModule, getImplementationLoader())
    }

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
    fun registerClicked(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.CLICKED, currentModule, getImplementationLoader())
    }

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
    fun registerDragged(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.DRAGGED, currentModule, getImplementationLoader())
    }

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
    fun registerSoundPlay(method: Any): OnSoundPlayTrigger {
        return OnSoundPlayTrigger(method, currentModule, getImplementationLoader())
    }

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
    fun registerNoteBlockPlay(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.NOTE_BLOCK_PLAY, currentModule, getImplementationLoader())
    }

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
    fun registerNoteBlockChange(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.NOTE_BLOCK_CHANGE, currentModule, getImplementationLoader())
    }

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
    fun registerTick(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.TICK, currentModule, getImplementationLoader())
    }

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
    fun registerStep(method: Any): OnStepTrigger {
        return OnStepTrigger(method, currentModule, getImplementationLoader())
    }

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
    fun registerRenderWorld(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.RENDER_WORLD, currentModule, getImplementationLoader())
    }

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
    fun registerRenderOverlay(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_OVERLAY, currentModule, getImplementationLoader())
    }

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
    fun registerRenderPlayerList(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_PLAYER_LIST, currentModule, getImplementationLoader())
    }

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
    fun registerRenderCrosshair(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_CROSSHAIR, currentModule, getImplementationLoader())
    }

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
    fun registerRenderDebug(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_DEBUG, currentModule, getImplementationLoader())
    }

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
    fun registerRenderBossHealth(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_BOSS_HEALTH, currentModule, getImplementationLoader())
    }

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
    fun registerRenderHealth(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_HEALTH, currentModule, getImplementationLoader())
    }

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
    fun registerRenderFood(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_FOOD, currentModule, getImplementationLoader())
    }

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
    fun registerRenderMountHealth(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_MOUNT_HEALTH, currentModule, getImplementationLoader())
    }

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
    fun registerRenderExperience(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_EXPERIENCE, currentModule, getImplementationLoader())
    }

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
    fun registerRenderHotbar(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_HOTBAR, currentModule, getImplementationLoader())
    }

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
    fun registerRenderAir(method: Any): OnRenderTrigger {
        return OnRenderTrigger(method, TriggerType.RENDER_AIR, currentModule, getImplementationLoader())
    }

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
    fun registerDrawBlockHighlight(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.BLOCK_HIGHLIGHT, currentModule, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs after the game loads.<br></br>
     * This runs after the initial loading of the game directly after scripts are loaded and after "/ct loadExtra" happens.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerGameLoad(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GAME_LOAD, currentModule, getImplementationLoader())
    }

    /**
     * Registers a new trigger that runs before the game unloads.<br></br>
     * This runs before shutdown of the JVM and before "/ct loadExtra" happens.<br></br>
     *
     *
     * Available modifications:<br></br>
     * [OnTrigger.setPriority] Sets the priority
     *
     * @param method the name of the method to callback when the event is fired
     * @return the trigger for additional modification
     */
    fun registerGameUnload(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GAME_UNLOAD, currentModule, getImplementationLoader())
    }

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
    fun registerCommand(method: Any): OnCommandTrigger {
        return OnCommandTrigger(method, currentModule, getImplementationLoader())
    }

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
    fun registerGuiOpened(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GUI_OPENED, currentModule, getImplementationLoader())
    }

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
    fun registerPlayerJoined(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.PLAYER_JOIN, currentModule, getImplementationLoader())
    }

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
    fun registerPlayerLeft(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.PLAYER_LEAVE, currentModule, getImplementationLoader())
    }

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
    fun registerPickupItem(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.PICKUP_ITEM, currentModule, getImplementationLoader())
    }

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
    fun registerDropItem(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.DROP_ITEM, currentModule, getImplementationLoader())
    }

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
    fun registerScreenshotTaken(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.SCREENSHOT_TAKEN, currentModule, getImplementationLoader())
    }

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
    fun registerMessageSent(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.MESSAGE_SENT, currentModule, getImplementationLoader())
    }

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
    fun registerItemTooltip(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.TOOLTIP, currentModule, getImplementationLoader())
    }

    fun registerPlayerInteract(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.PLAYER_INTERACT, currentModule, getImplementationLoader())
    }

    fun registerBlockBreak(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.BLOCK_BREAK, currentModule, getImplementationLoader())
    }

    /**
     * Passes in mouseX, mouseY, and the open gui instance
     */
    fun registerGuiRender(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GUI_RENDER, currentModule, getImplementationLoader())
    }

    /**
     * Passes in the character typed, the keycode typed (see [org.lwjgl.input.Keyboard], the open gui instance,
     * and the event (which can be cancelled).
     */
    fun registerGuiKey(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GUI_KEY, currentModule, getImplementationLoader())
    }

    /**
     * Passes in the mouseX, mouseY, mouseButton, the open gui instance, and the event (which can be cancelled).
     */
    fun registerGuiMouseClick(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GUI_MOUSE_CLICK, currentModule, getImplementationLoader())
    }

    /**
     * Passes in the mouseX, mouseY, mouseButton, the open gui instance, and the event (which can be cancelled).
     */
    fun registerGuiMouseRelease(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GUI_MOUSE_RELEASE, currentModule, getImplementationLoader())
    }

    /**
     * Passes in the mouseX, mouseY, mouseButton, the open gui instance, and the event (which can be cancelled).
     */
    fun registerGuiMouseDrag(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.GUI_MOUSE_DRAG, currentModule, getImplementationLoader())
    }

    /**
     * Passes in the packet and the event (which can be cancelled).
     */
    fun registerPacketSent(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.PACKET_SENT, currentModule, getImplementationLoader())
    }

    /**
     * Passes in the chat component and the event (which can be cancelled).
     */
    fun registerChatComponentClicked(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.CHAT_COMPONENT_CLICKED, currentModule, getImplementationLoader())
    }

    /**
     * Passes in the chat component, the x and y coordinates, and the
     * event (which can be cancelled).
     */
    fun registerChatComponentHovered(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.CHAT_COMPONENT_HOVERED, currentModule, getImplementationLoader())
    }

    /**
     * Passes in the entity, the entity location as a Vector3d, the
     * partialTicks, and the event (which can be cancelled).
     */
    fun registerRenderEntity(method: Any): OnRegularTrigger {
        return OnRegularTrigger(method, TriggerType.RENDER_ENTITY, currentModule, getImplementationLoader())
    }

    // /**
    //  * Passes in the block, its BlockPos, and the event (which
    //  * can be cancelled).
    //  */
    // fun registerRenderBlock(method: Any): OnRegularTrigger {
    //     return OnRegularTrigger(method, TriggerType.RENDER_BLOCK, currentModule, getImplementationLoader())
    // }

    fun getImplementationLoader(): ILoader
}