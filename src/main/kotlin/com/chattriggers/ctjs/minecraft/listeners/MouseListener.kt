package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.triggers.TriggerType

//#if MC==11602
//$$ import net.minecraftforge.client.event.GuiScreenEvent
//$$ import net.minecraftforge.eventbus.api.SubscribeEvent
//#else
import org.lwjgl.input.Mouse
//#endif

object MouseListener {
    private val scrollListeners = mutableListOf<(x: Double, y: Double, delta: Int) -> Unit>()
    private val clickListeners = mutableListOf<(x: Double, y: Double, button: Int, pressed: Boolean) -> Unit>()
    private val draggedListeners = mutableListOf<(deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) -> Unit>()

    //#if MC==10809
    private val mouseState = mutableMapOf<Int, Boolean>()
    private val draggedState = mutableMapOf<Int, State>()

    class State(val x: Double, val y: Double)
    //#endif

    init {
        registerTriggerListeners()
    }

    fun registerScrollListener(listener: (x: Double, y: Double, delta: Int) -> Unit) {
        scrollListeners.add(listener)
    }

    fun registerClickListener(listener: (x: Double, y: Double, button: Int, pressed: Boolean) -> Unit) {
        clickListeners.add(listener)
    }

    fun registerDraggedListener(listener: (deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) -> Unit) {
        draggedListeners.add(listener)
    }

    private fun scrolled(x: Double, y: Double, delta: Int) {
        scrollListeners.forEach { it(x, y, delta) }
    }

    private fun clicked(x: Double, y: Double, button: Int, pressed: Boolean) {
        clickListeners.forEach { it(x, y, button, pressed) }
    }

    private fun dragged(deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) {
        draggedListeners.forEach { it(deltaX, deltaY, x, y, button) }
    }

    fun clearListeners() {
        scrollListeners.clear()
        clickListeners.clear()
        draggedListeners.clear()
    }

    fun registerTriggerListeners() {
        registerScrollListener(TriggerType.SCROLLED::triggerAll)
        registerClickListener(TriggerType.CLICKED::triggerAll)
        registerDraggedListener(TriggerType.DRAGGED::triggerAll)
    }

    //#if MC==11602
    //$$ @SubscribeEvent
    //$$ fun onMouseScroll(event: GuiScreenEvent.MouseScrollEvent) {
    //$$     if (event is GuiScreenEvent.MouseScrollEvent.Pre)
    //$$         scrolled(event.mouseX, event.mouseY, if (event.scrollDelta < 0) -1 else 1)
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ fun onMouseClicked(event: GuiScreenEvent.MouseClickedEvent) {
    //$$     if (event is GuiScreenEvent.MouseClickedEvent.Pre)
    //$$         clicked(event.mouseX, event.mouseY, event.button, true)
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ fun onMouseRelease(event: GuiScreenEvent.MouseReleasedEvent) {
    //$$     if (event is GuiScreenEvent.MouseReleasedEvent.Pre)
    //$$         clicked(event.mouseX, event.mouseY, event.button, false)
    //$$ }
    //$$
    //$$ @SubscribeEvent
    //$$ fun onMouseDragged(event: GuiScreenEvent.MouseDragEvent) {
    //$$     if (event is GuiScreenEvent.MouseDragEvent.Pre)
    //$$         dragged(event.dragX, event.dragY, event.mouseX, event.mouseY, event.mouseButton)
    //$$ }
    //$$
    //$$ internal fun process() {}
    //#else
    internal fun process() {
        if (!Mouse.isCreated())
            return

        val scroll = Mouse.getEventDWheel()
        if (scroll != 0) {
            scrolled(
                Client.getMouseX().toDouble(),
                Client.getMouseY().toDouble(),
                if (scroll < 0) -1 else 1,
            )
        }

        for (button in 0..4) {
            handleDragged(button)

            // normal clicked
            if (Mouse.isButtonDown(button) == mouseState[button])
                continue

            val x = Client.getMouseX().toDouble()
            val y = Client.getMouseY().toDouble()

            clicked(
                x,
                y,
                button,
                Mouse.isButtonDown(button),
            )

            mouseState[button] = Mouse.isButtonDown(button)

            // add new dragged
            if (Mouse.isButtonDown(button)) {
                draggedState[button] = State(x, y)
            } else if (draggedState.containsKey(button)) {
                draggedState.remove(button)
            }
        }
    }

    private fun handleDragged(button: Int) {
        if (button !in draggedState)
            return

        val x = Client.getMouseX().toDouble()
        val y = Client.getMouseY().toDouble()

        dragged(
            x - (draggedState[button]?.x ?: 0.0),
            y - (draggedState[button]?.y ?: 0.0),
            x,
            y,
            button
        )

        // update dragged
        draggedState[button] = State(x, y)
    }
    //#endif
}