package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import com.chattriggers.ctjs.minecraft.listeners.MouseListener
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiScreen

//#if MC>=11701
//$$ import com.mojang.blaze3d.vertex.PoseStack
//$$ import gg.essential.universal.wrappers.message.UTextComponent
//#endif

//#if MC<=11202
object ModulesGui : GuiScreen() {
//#else
//$$ object ModulesGui : Screen(UTextComponent("")) {
//#endif
    private val window = object {
        var title = Text("Modules").setScale(2f).setShadow(true)
        var exit = Text(ChatLib.addColor("&cx")).setScale(2f)
        var height = 0f
        var scroll = 0f
    }

    init {
        MouseListener.registerScrollListener { x, y, delta ->
            window.scroll += delta / 10
        }
    }

    //#if MC<=11202
    override fun doesGuiPauseGame() = false
    //#else
    //$$ override fun isPauseScreen() = false
    //#endif

    //#if MC<=11202
    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
    //#else
    //$$ override fun render(matrixStack: PoseStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
    //$$     super.render(matrixStack, mouseX, mouseY, partialTicks)
    //$$     Renderer.withMatrixStack(matrixStack) {
    //#endif

        Renderer.pushMatrix()

        val middle = Renderer.screen.getWidth() / 2f
        var width = Renderer.screen.getWidth() - 100f
        if (width > 500) width = 500f

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

        //#if MC>=11701
        //$$ }
        //#endif
    }

    //#if MC<=11202
    override fun mouseClicked(mouseX: Int, mouseY: Int, button: Int) {
        super.mouseClicked(mouseX, mouseY, button)
    //#else
    //$$ override fun mouseClicked(mouseX_: Double, mouseY_: Double, button: Int): Boolean {
    //$$     if (!super.mouseClicked(mouseX_, mouseY_, button))
    //$$         return false
    //$$     val mouseX = mouseX_.toInt()
    //$$     val mouseY = mouseY_.toInt()
    //#endif

        var width = Renderer.screen.getWidth() - 100f
        if (width > 500) width = 500f

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

        //#if MC>=11701
        //$$ return true
        //#endif
    }
}
