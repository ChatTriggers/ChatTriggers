package com.chattriggers.ctjs.minecraft.objects.display

import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.utils.kotlin.External
import java.util.concurrent.CopyOnWriteArrayList

@External
object DisplayHandler {
    private val displays = CopyOnWriteArrayList<Display>()

    fun registerDisplay(display: Display) = displays.add(display)

    fun clearDisplays() = displays.clear()

    fun renderDisplays() {
        Renderer.pushMatrix()
        displays.forEach(Display::render)
        Renderer.popMatrix()
    }

    enum class Background {
        NONE, FULL, PER_LINE
    }

    enum class Align {
        LEFT, CENTER, RIGHT
    }

    enum class Order {
        UP, DOWN
    }
}
