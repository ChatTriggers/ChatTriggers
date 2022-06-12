package com.chattriggers.ctjs.minecraft.objects.display

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.triggers.EventType
import java.util.concurrent.CopyOnWriteArrayList

object DisplayHandler {
    private val displays = CopyOnWriteArrayList<Display>()

    init {
        CTJS.addEventListener(EventType.RenderOverlay) {
            Renderer.pushMatrix()
            displays.forEach {
                if (it.registerType == RegisterType.RENDER_OVERLAY) {
                    it.render()
                }
            }
            Renderer.popMatrix()
        }

        CTJS.addEventListener(EventType.PostGuiRender) {
            Renderer.pushMatrix()
            displays.forEach {
                if (it.registerType == RegisterType.POST_GUI_RENDER) {
                    it.render()
                }
            }
            Renderer.popMatrix()
        }
    }

    fun registerDisplay(display: Display) = displays.add(display)

    fun clearDisplays() = displays.clear()

    enum class RegisterType {
        RENDER_OVERLAY, POST_GUI_RENDER
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
