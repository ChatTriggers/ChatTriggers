pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://maven.architectury.dev")
        maven("https://maven.fabricmc.net")
        maven("https://maven.minecraftforge.net")
    }

    plugins {
        val egtVersion = "0.1.6"
        id("gg.essential.loom") version "0.10.0.2"
        id("gg.essential.multi-version.root") version egtVersion
    }
}

listOf(
    "1.8.9-forge",
    "1.17.1-forge"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}

rootProject.buildFileName = "root.gradle.kts"
rootProject.name = "ChatTriggers"
