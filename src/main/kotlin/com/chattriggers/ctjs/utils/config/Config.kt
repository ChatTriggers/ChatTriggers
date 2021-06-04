package com.chattriggers.ctjs.utils.config

import com.chattriggers.ctjs.CTJS
import com.google.gson.JsonObject
import java.awt.Color
import java.io.File
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaField

object Config {
    @ConfigOpt(name = "Directory", x = -110, y = 10, type = ConfigString::class)
    var modulesFolder: String = "./config/ChatTriggers/modules/"

    @ConfigOpt(name = "Print Chat To Console", x = -110, y = 65, type = ConfigBoolean::class)
    var printChatToConsole: Boolean = true

    @ConfigOpt(name = "Show Update Messages in Chat", x = -110, y = 120, type = ConfigBoolean::class)
    var showUpdatesInChat: Boolean = true

    @ConfigOpt(name = "Automatically Update Modules", x = -110, y = 175, type = ConfigBoolean::class)
    var autoUpdateModules: Boolean = true

    @ConfigOpt(name = "Clear Console on Load", x = 110, y = 10, type = ConfigBoolean::class)
    var clearConsoleOnLoad: Boolean = true

    @ConfigOpt(name = "Auto-Open Console on Error", x = 110, y = 65, type = ConfigBoolean::class)
    var openConsoleOnError: Boolean = false

    @ConfigOpt(name = "Console Pretty Font", x = 110, y = 120, type = ConfigBoolean::class)
    var consolePrettyFont: Boolean = true

    @ConfigOpt(name = "Console Font Size", x = 110, y = 175, type = ConfigString::class)
    var consoleFontSize: String = "9"

    @ConfigOpt(name = "Custom Console Theme", x = 110, y = 230, type = ConfigBoolean::class)
    var customTheme: Boolean = false

    @ConfigOpt(name = "Console Theme", x = 110, y = 285, type = ConsoleThemeSelector::class)
    var consoleTheme: String = "default.dark"

    @ConfigOpt(name = "Console Foreground Color", x = 110, y = 285, type = SpecialConfigColor::class)
    var consoleForegroundColor: Color = Color(208, 208, 208)

    @ConfigOpt(name = "Console Background Color", x = 110, y = 360, type = SpecialConfigColor::class)
    var consoleBackgroundColor: Color = Color(21, 21, 21)

    @ConfigOpt(name = "Threaded Loading", x = -110, y = 230, type = ConfigBoolean::class)
    var threadedLoading = true

    @ConfigOpt(name = "Show Module Help on Import", x = -110, y = 285, type = ConfigBoolean::class)
    var moduleImportHelp = true

    @ConfigOpt(name = "Show Module Changelog", x = -110, y = 340, type = ConfigBoolean::class)
    var moduleChangelog = true

    fun load(jsonObject: JsonObject) {
        this::class.declaredMemberProperties.filter {
            it.annotations.any { ann ->
                ann.annotationClass == ConfigOpt::class
            } && it is KMutableProperty<*>
        }.map {
            it as KMutableProperty<*>
        }.forEach {
            val wrapper = jsonObject.getAsJsonObject(it.name)

            val value = CTJS.gson.fromJson(wrapper.get("value"), it.javaField?.type)

            it.setter.call(this, value)
        }
    }

    fun save(file: File) {
        val obj = JsonObject()

        this::class.declaredMemberProperties.filter {
            it.annotations.any { ann ->
                ann.annotationClass == ConfigOpt::class
            }
        }.forEach {
            val wrapper = JsonObject()
            wrapper.add("value", CTJS.gson.toJsonTree(it.getter.call(this)))

            obj.add(it.name, wrapper)
        }

        file.writeText(
            CTJS.gson.toJson(obj)
        )
    }
}
