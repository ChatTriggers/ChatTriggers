package com.chattriggers.ctjs.minecraft.objects.display

import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.CopyOnWriteArrayList

object DisplayHandler {
    private val displays = CopyOnWriteArrayList<Display>()

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun registerDisplay(display: Display) = displays.add(display)

    fun clearDisplays() = displays.clear()

    @SubscribeEvent
    fun renderDisplayOverlay(event: RenderGameOverlayEvent.Text) {
        GlStateManager.pushMatrix()
        displays.forEach {
            if (it.registerType == RegisterType.RENDER_OVERLAY) {
                it.render()
            }
        }
        GlStateManager.popMatrix()
    }

    @SubscribeEvent
    fun renderDisplayGui(event: GuiScreenEvent.DrawScreenEvent.Post) {
        GlStateManager.pushMatrix()
        displays.forEach {
            if (it.registerType == RegisterType.POST_GUI_RENDER) {
                it.render()
            }
        }
        GlStateManager.popMatrix()
    }

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
