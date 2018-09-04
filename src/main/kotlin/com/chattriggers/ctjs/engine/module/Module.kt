package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.libs.renderer.Text
import java.io.File

class Module(val name: String, val metadata: ModuleMetadata, val folder: File) {
    private val gui = object {
        var collapsed = true
        var x = 0f
        var y = 0f
    }

    fun getFilesWithExtension(type: String): List<File> {
        return this.folder.walkTopDown().filter {
            it.name.endsWith(type)
        }.filter {
            if (this.metadata.ignored == null) return@filter true

            for (ignore: String in this.metadata.ignored) {
                if (ignore in it.name) return@filter false
            }

            return@filter true
        }.filter {
            it.isFile
        }.toList()
    }

    fun draw(x: Float, y: Float): Float {
        gui.x = x
        gui.y = y

        Renderer.drawRect(
                0x75000000,
                x, y, 500f, 13f)
        Renderer.drawStringWithShadow(
                metadata.name ?: name,
                x + 3, y + 3)

        return if (gui.collapsed) {
            Renderer.translate(x + 495, y + 8)
            Renderer.rotate(180f)
            Renderer.drawString("^", 0f, 0f)

            15f
        } else {
            val description = Text(metadata.description ?: "No description provided in the metadata").setWidth(500)

            Renderer.drawRect(0x50000000, x, y + 13, 500f, description.getHeight() + 12)
            Renderer.drawString("^", x + 490, y + 5)

            description.draw(x + 3, y + 15)

            if (metadata.version != null) {
                Renderer.drawStringWithShadow(
                        ChatLib.addColor("&8v" + (metadata.version)),
                        x + 500f - Renderer.getStringWidth(ChatLib.addColor("&8v" + metadata.version)), y + description.getHeight() + 15)
            }

            description.getHeight() + 27
        }
    }

    fun click(x: Int, y: Int) {
        if (x > gui.x && x < gui.x + 500
        && y > gui.y && y < gui.y + 13) {
            gui.collapsed = !gui.collapsed
        }
    }

    override fun toString() = "Module{name=$name,folder=$folder,metadata=$metadata}"
}