package com.chattriggers.ctjs.engine.module.import

import com.chattriggers.ctjs.minecraft.libs.FileLib
import com.chattriggers.ctjs.print
import com.google.gson.*
import net.minecraft.client.gui.GuiScreen
import org.jsoup.nodes.Document
import java.net.UnknownHostException

object ImportGui : GuiScreen() {
    val gson: Gson = GsonBuilder().apply {
        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        registerTypeAdapter(Document::class.java, DocumentTypeAdapter())
    }.create()

    var modules: List<ForeignModule> = listOf()

    override fun initGui() {
        updateModulesList()
    }

    private fun updateModulesList() = try {
        modules = gson.fromJson(
                FileLib.getUrlContent("https://www.chattriggers.com/api/modules"),
                Array<ForeignModule>::class.java
        ).toList()
    } catch (e: UnknownHostException) {
        e.print()
    }
}