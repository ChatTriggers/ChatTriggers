package com.chattriggers.ctjs.minecraft.listeners

import com.chattriggers.ctjs.triggers.TriggerType

//#if MC<=11202
import com.chattriggers.ctjs.minecraft.wrappers.Client
import com.chattriggers.ctjs.minecraft.wrappers.World
import org.lwjgl.input.Mouse
//#endif

object MouseListener : Initializer {
    private val scrollListeners = mutableListOf<(x: Double, y: Double, delta: Int) -> Unit>()
    private val clickListeners = mutableListOf<(x: Double, y: Double, button: Int, pressed: Boolean) -> Unit>()
    private val draggedListeners = mutableListOf<(deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) -> Unit>()

    //#if MC<=11202
    private val mouseState = mutableMapOf<Int, Boolean>()
    private val draggedState = mutableMapOf<Int, State>()

    class State(val x: Double, val y: Double)
    //#endif

    override fun init() {
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

    internal fun scrolled(x: Double, y: Double, delta: Int) {
        scrollListeners.forEach { it(x, y, delta) }
    }

    internal fun clicked(x: Double, y: Double, button: Int, pressed: Boolean) {
        clickListeners.forEach { it(x, y, button, pressed) }
    }

    internal fun dragged(deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) {
        draggedListeners.forEach { it(deltaX, deltaY, x, y, button) }
    }

    fun clearListeners() {
        scrollListeners.clear()
        clickListeners.clear()
        draggedListeners.clear()
    }

    fun registerTriggerListeners() {
        registerScrollListener(TriggerType.Scrolled::triggerAll)
        registerClickListener(TriggerType.Clicked::triggerAll)
        registerDraggedListener(TriggerType.Dragged::triggerAll)
    }

    //#if MC<=11202
    internal fun process(button: Int, dWheel: Int) {
        if (dWheel != 0) {
            scrolled(
                Client.getMouseX(),
                Client.getMouseY(),
                if (dWheel < 0) -1 else 1,
            )
        }

        if (button == -1)
            return

        // normal clicked
        if (Mouse.isButtonDown(button) == mouseState[button])
            return

        val x = Client.getMouseX()
        val y = Client.getMouseY()

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

    internal fun onGuiMouseInput() {
        if (!World.isLoaded()) {
            mouseState.clear()
            draggedState.clear()
            return
        }

        val button = Mouse.getEventButton()
        val dWheel = Mouse.getEventDWheel()
        process(button, dWheel)
    }

    internal fun handleDragged() {
        for (button in 0..4) {
            if (button !in draggedState)
                continue

            val x = Client.getMouseX()
            val y = Client.getMouseY()

            if (x == draggedState[button]?.x && y == draggedState[button]?.y)
                continue

            dragged(
                x - (draggedState[button]?.x ?: 0.0),
                y - (draggedState[button]?.y ?: 0.0),
                x,
                y,
                button,
            )

            // update dragged
            draggedState[button] = State(x, y)
        }
    }
    //#endif
}
