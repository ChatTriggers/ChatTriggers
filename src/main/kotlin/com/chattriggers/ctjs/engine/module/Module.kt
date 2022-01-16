package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import java.io.File

class Module(val name: String, var metadata: ModuleMetadata, val folder: File) {
    private var collapsed = true
    private var x = 0f
    private var y = 0f
    private var description = Text(metadata.description ?: "No description provided in the metadata")

    fun draw(x: Float, y: Float, width: Float): Float {
        this.x = x
        this.y = y

        Renderer.drawRect(0xaa000000, x, y, width, 13f)
        Renderer.drawStringWithShadow(metadata.name ?: name, x + 3, y + 3)

        return if (collapsed) {
            Renderer.translate(x + width - 5.0, y + 8.0)
            Renderer.rotate(180f)
            Renderer.drawString("^", 0f, 0f)
            15f
        } else {
            description.setWidth(width.toInt() - 5)

            Renderer.drawRect(0x50000000, x, y + 13, width, description.getHeight() + 12)
            Renderer.drawString("^", x + width - 10, y + 5)

            description.draw(x + 3, y + 15)

            if (metadata.version != null) {
                Renderer.drawStringWithShadow(
                    ChatLib.addColor("&8v" + (metadata.version)),
                    x + width - Renderer.getStringWidth(ChatLib.addColor("&8v" + metadata.version)),
                    y + description.getHeight() + 15
                )
            }

            Renderer.drawStringWithShadow(
                ChatLib.addColor(if (metadata.isRequired) "&8required" else "&4[delete]"),
                x + 3,
                y + description.getHeight() + 15,
            )

            description.getHeight() + 27
        }
    }

    fun click(x: Double, y: Double, width: Float) {
        if (x > this.x && x < this.x + width && y > this.y && y < this.y + 13) {
            collapsed = !collapsed
            return
        }

        if (collapsed)
            return

        if (x > this.x && x < this.x + 45
            && y > this.y + description.getHeight() + 15 && y < this.y + description.getHeight() + 25
        ) {
            ModuleManager.deleteModule(name)
        }
    }

    override fun toString() = "Module{name=$name,version=${metadata.version}}"
}
