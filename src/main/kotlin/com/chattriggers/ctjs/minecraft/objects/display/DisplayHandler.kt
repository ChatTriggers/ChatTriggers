package com.chattriggers.ctjs.minecraft.objects.display

import com.chattriggers.ctjs.utils.kotlin.External
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.CopyOnWriteArrayList

@External
object DisplayHandler {
    private var displays = CopyOnWriteArrayList<Display>()

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun registerDisplay(display: Display) = this.displays.add(display)

    fun clearDisplays() = this.displays.clear()

    @SubscribeEvent
    fun renderDisplays(event: RenderGameOverlayEvent.Text) {
        GlStateManager.pushMatrix()
        this.displays.forEach(Display::render)
        GlStateManager.popMatrix()
    }

    enum class Background {
        NONE, FULL, PER_LINE
    }

    enum class Align {
        NONE, LEFT, CENTER, RIGHT
    }

    enum class Order {
        UP, DOWN
    }
}
