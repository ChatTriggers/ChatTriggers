package com.chattriggers.ctjs.utils

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.triggers.EventType
import com.chattriggers.ctjs.utils.kotlin.toVersion
import com.google.gson.reflect.TypeToken
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.event.ClickEvent

object UpdateChecker {
    private var updateAvailable = false
    private var warned = false

    init {
        try {
            getUpdate()
        } catch (exception: Exception) {
            exception.printTraceToConsole()
        }
        warned = !Config.showUpdatesInChat

        CTJS.addEventListener(EventType.WorldLoad) {
            if (!updateAvailable || warned) return@addEventListener

            //#if MC<=11202
            World.playSound("note.bass", 1000f, 1f)
            //#elseif MC>=11701
            //$$ World.playSound("block.note_block.bass", 1000f, 1f)
            //#endif
            UMessage(
                "&c&m" + ChatLib.getChatBreak("-"),
                "\n",
                "&cChatTriggers requires an update to work properly!",
                "\n",
                UTextComponent("&a[Download]").setClick(ClickEvent.Action.OPEN_URL, "${CTJS.WEBSITE_ROOT}/#download"),
                " ",
                UTextComponent("&e[Changelog]").setClick(ClickEvent.Action.OPEN_URL, "https://github.com/ChatTriggers/ChatTriggers/releases"),
                "\n",
                "&c&m" + ChatLib.getChatBreak("-")
            ).chat()

            warned = true
        }
    }

    private fun getUpdate() {
        val versions = CTJS.gson.fromJson<Map<String, List<String>>>(
            FileLib.getUrlContent("${CTJS.WEBSITE_ROOT}/api/versions"),
            object : TypeToken<Map<String, List<String>>>() {}.type
        )

        val latestVersion = versions.flatMap { entry ->
            entry.value.map { "${entry.key}.$it".toVersion() }
        }.maxOrNull() ?: return

        updateAvailable = latestVersion > Reference.MODVERSION.toVersion()
    }

    fun drawUpdateMessage() {
        if (!updateAvailable) return

        Renderer.pushMatrix()

        Renderer.drawString(
            ChatLib.addColor("&cChatTriggers requires an update to work properly!"),
            2f,
            2f,
        )

        Renderer.popMatrix()
    }
}
