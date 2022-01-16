package com.chattriggers.ctjs.utils

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.objects.message.Message
import com.chattriggers.ctjs.minecraft.objects.message.TextComponent
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.utils.kotlin.toVersion
import com.google.gson.reflect.TypeToken
import net.minecraft.client.util.math.MatrixStack

object UpdateChecker {
    private var worldLoaded = false
    private var updateAvailable = false
    private var warned = false

    init {
        try {
            getUpdate()
        } catch (exception: Exception) {
            exception.printTraceToConsole()
        }
        warned = !Config.showUpdatesInChat
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

    fun check() {
        if (!updateAvailable || warned)
            return

        World.playSound("note.bass", 1000f, 1f)
        Message(
            "&c&m" + ChatLib.getChatBreak("-"),
            "\n",
            "&cChatTriggers requires an update to work properly!",
            "\n",
            TextComponent("&a[Download]").setClick("open_url", "${CTJS.WEBSITE_ROOT}/#download"),
            " ",
            TextComponent("&e[Changelog]").setClick("open_url", "https://github.com/ChatTriggers/ChatTriggers/releases"),
            "\n",
            "&c&m" + ChatLib.getChatBreak("-")
        ).chat()

        warned = true
    }

    fun drawUpdateMessage(matrixStack: MatrixStack) {
        if (!updateAvailable)
            return

        matrixStack.push()

        Renderer.getFontRenderer().draw(
            matrixStack,
            ChatLib.addColor("&cChatTriggers requires an update to work properly!"),
            2f,
            2f,
            -0x1,
        )

        matrixStack.pop()
    }
}
