package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.listeners.MouseListener
import com.chattriggers.ctjs.minecraft.wrappers.Client
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UScreen

object ModulesGui : UScreen() {
    private val window = object {
        var title = Text("Modules").setScale(2f).setShadow(true)
        var exit = Text(ChatLib.addColor("&cx")).setScale(2f)
        var height = 0f
        var scroll = 0f
    }

    init {
        MouseListener.registerScrollListener { _, _, delta ->
            window.scroll += delta / 10
        }
    }

    //#if MC<=11202
    override fun doesGuiPauseGame() = false
    //#else
    //$$ override fun isPauseScreen() = false
    //#endif

    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)
        Renderer.pushMatrix()

        val middle = Renderer.screen.getWidth() / 2f
        val width = (Renderer.screen.getWidth() - 100f).coerceAtMost(500f)

        Renderer.drawRect(
            0f,
            0f,
            Renderer.screen.getWidth().toFloat(),
            Renderer.screen.getHeight().toFloat(),
            0x50000000
        )

        if (-window.scroll > window.height - Renderer.screen.getHeight() + 20)
            window.scroll = -window.height + Renderer.screen.getHeight() - 20
        if (-window.scroll < 0) window.scroll = 0f

        if (-window.scroll > 0) {
            Renderer.drawRect(Renderer.screen.getWidth() - 20f, Renderer.screen.getHeight() - 20f, 20f, 20f, 0xaa000000)
            Renderer.drawString("^", Renderer.screen.getWidth() - 12f, Renderer.screen.getHeight() - 12f)
        }

        Renderer.drawRect(middle - width / 2f, window.scroll + 95f, width, window.height - 90, 0x50000000)

        Renderer.drawRect(middle - width / 2f, window.scroll + 95f, width, 25f, 0xaa000000)
        window.title.draw(middle - width / 2f + 5, window.scroll + 100f)
        window.exit.setString(ChatLib.addColor("&cx")).draw(middle + width / 2f - 17, window.scroll + 99f)

        window.height = 125f
        ModuleManager.cachedModules.forEach {
            window.height += it.draw(middle - width / 2f, window.scroll + window.height, width)
        }

        Renderer.popMatrix()
    }

    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        super.onMouseClicked(mouseX, mouseY, mouseButton)

        val width = (Renderer.screen.getWidth() - 100f).coerceAtMost(500f)

        if (mouseX > Renderer.screen.getWidth() - 20 && mouseY > Renderer.screen.getHeight() - 20) {
            window.scroll = 0f
        } else if (mouseX > Renderer.screen.getWidth() / 2f + width / 2f - 25 && mouseX < Renderer.screen.getWidth() / 2f + width / 2f
            && mouseY > window.scroll + 95 && mouseY < window.scroll + 120
        ) {
            Client.currentGui.close()
        } else {
            ModuleManager.cachedModules.toList().forEach {
                it.click(mouseX, mouseY, width)
            }
        }
    }
}
