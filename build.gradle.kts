plugins {
    id("org.jetbrains.kotlin.jvm")
    id("gg.essential.loom")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

group = "com.chattriggers"
version = "2.1.0"

val accessTransformerName = "ctjs1.${platform.mcMinor}_at.cfg"

loom {
    forge {
        accessTransformer(rootProject.file("src/main/resources/$accessTransformerName"))
    }
    mixin {
        defaultRefmapName.set("chattriggers.mixins.refmap.json")
    }
    launchConfigs {
        getByName("client") {
            property("mixin.debug.verbose", "true")
            property("mixin.debug.export", "true")
            property("mixin.dumpTargetOnFailure", "true")
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            arg("--mixin", "chattriggers.mixins.json")
        }
    }
}

val embed by configurations.creating
configurations.implementation.get().extendsFrom(embed)

repositories {
    maven("https://repo.spongepowered.org/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("gg.essential:essential-$platform:2666")
    embed("gg.essential:loader-launchwrapper:1.1.3")

    // implementation("dev.falsehonesty.asmhelper:AsmHelper:1.5.3-${platform.mcVersion}") {
    //     exclude("org.jetbrains.kotlin")
    // }
    implementation("com.chattriggers:rhino:1.8.6")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.2")
    implementation("com.fifesoft:rsyntaxtextarea:3.2.0")
}

tasks.processResources {
    rename("(.+_at.cfg)", "META-INF/$1")
}

tasks.jar {
    from(embed.files.map { zipTree(it) })

    manifest.attributes(mapOf(
        "ModSide" to "CLIENT",
        "FMLAT" to accessTransformerName,
        "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
        "TweakOrder" to "0",
        "MixinConfigs" to "chattriggers.mixins.json"
    ))
}
