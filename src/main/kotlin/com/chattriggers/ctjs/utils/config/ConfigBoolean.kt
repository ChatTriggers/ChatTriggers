package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.Client
import net.minecraft.client.gui.GuiButton

class ConfigBoolean internal constructor(previous: ConfigBoolean? = null, name: String = "", private val defaultValue: Boolean = false, x: Int = 0, y: Int = 0) : ConfigOption(ConfigOption.Type.BOOLEAN) {
    var value: Boolean = previous?.value ?: this.defaultValue

    @Transient
    private var button: GuiButton = GuiButton(0,
            Renderer.screen.getWidth() / 2 - 100 + this.x, this.y + 15,
            stringValue)

    private val stringValue: String
        get() = if (this.value) ChatLib.addColor("&aTrue") else ChatLib.addColor("&cFalse")

    init {
        this.name = name

        this.x = x
        this.y = y
    }

    override fun draw(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (this.hidden) return

        val middle = Renderer.screen.getWidth() / 2

        Renderer.rectangle(-0x80000000, (middle - 105 + this.x).toFloat(), (this.y - 5).toFloat(), 210f, 45f)
                .setShadow(-0x30000000, 3f, 3f)
                .draw()

        Renderer.text(this.name!!, (middle - 100 + this.x).toFloat(), this.y.toFloat()).draw()

        //#if MC<=10809
        this.button.xPosition = middle - 100 + this.x
        this.button.drawButton(Client.getMinecraft(), mouseX, mouseY)
        //#else
        //$$ this.button.x = middle - 100 + this.x;
        //$$ this.button.drawButton(Client.getMinecraft(), mouseX, mouseY, partialTicks);
        //#endif

        super.draw(mouseX, mouseY, partialTicks)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int) {
        if (this.hidden) return

        if (this.button.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = (!this.value)
            this.button.playPressSound(Client.getMinecraft().soundHandler)
        }

        if (this.resetButton.mousePressed(Client.getMinecraft(), mouseX, mouseY)) {
            this.value = this.defaultValue
            this.resetButton.playPressSound(Client.getMinecraft().soundHandler)
        }

        this.button.displayString = stringValue
    }
}