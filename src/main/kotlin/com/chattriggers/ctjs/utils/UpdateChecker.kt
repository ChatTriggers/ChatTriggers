package com.chattriggers.ctjs.utils

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.Reference
import com.chattriggers.ctjs.minecraft.libs.ChatLib
import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.minecraft.libs.renderer.Renderer
import com.chattriggers.ctjs.minecraft.wrappers.World
import com.chattriggers.ctjs.printTraceToConsole
import com.chattriggers.ctjs.utils.kotlin.MCClickEventAction
import com.chattriggers.ctjs.utils.kotlin.toVersion
import com.google.gson.reflect.TypeToken
import gg.essential.universal.wrappers.message.UMessage
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

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

    @SubscribeEvent
    fun worldLoad(event: WorldEvent.Load) {
        worldLoaded = true
    }

    @SubscribeEvent
    fun renderOverlay(event: RenderGameOverlayEvent.Pre) {
        if (!worldLoaded) return
        worldLoaded = false

        if (!updateAvailable || warned) return

        World.playSound("note.bass", 1000f, 1f)
        UMessage(
            "&c&m" + ChatLib.getChatBreak("-"),
            "\n",
            "&cChatTriggers requires an update to work properly!",
            "\n",
            UTextComponent("&a[Download]").setClick(MCClickEventAction.OPEN_URL, "${CTJS.WEBSITE_ROOT}/#download"),
            " ",
            UTextComponent("&e[Changelog]").setClick(MCClickEventAction.OPEN_URL, "https://github.com/ChatTriggers/ChatTriggers/releases"),
            "\n",
            "&c&m" + ChatLib.getChatBreak("-")
        ).chat()

        warned = true
    }

    fun drawUpdateMessage() {
        if (!updateAvailable) return

        GlStateManager.pushMatrix()

        Renderer.getFontRenderer()
            .drawString(
                ChatLib.addColor("&cChatTriggers requires an update to work properly!"),
                2f,
                2f,
                -0x1,
                false
            )

        GlStateManager.popMatrix()
    }
}
