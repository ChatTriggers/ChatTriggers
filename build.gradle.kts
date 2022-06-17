plugins {
    id("org.jetbrains.kotlin.jvm")
    id("gg.essential.multi-version")
    id("gg.essential.loom")
    id("gg.essential.defaults")
}

group = "com.chattriggers"
version = "2.1.0"

val mcMinor = platform.mcMinor
val isForge = platform.isForge
val accessTransformerName = "chattriggers_at.cfg"
val mixinName = "chattriggers1.$mcMinor.mixins.json"

loom {
    runConfigs {
        named("client") {
            ideConfigGenerated(true)
        }
    }
    if (isForge) {
        forge {
            accessTransformer(rootProject.file("src/main/resources/$accessTransformerName"))
        }
    }
    launchConfigs {
        getByName("client") {
            property("mixin.debug.verbose", "true")
            property("mixin.debug.export", "true")
            property("mixin.dumpTargetOnFailure", "true")

            arg("--mixin", "chattriggers.mixins.json")

            if (isForge)
                arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
        }
    }

    if (isForge) {
        forge {
            mixinConfigs.set(mutableListOf("mixinName"))
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

    if (isForge && mcMinor <= 12)
        embed("gg.essential:loader-launchwrapper:1.1.3")

    if (platform.isFabric) {
        val include by configurations

        modImplementation(include("gg.essential:loader-fabric:1.0.0")!!)
        modImplementation("net.fabricmc.fabric-api:fabric-api:0.45.0+1.17")
    }

    compileOnly("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT:processor")

    embed("com.chattriggers:rhino:1.8.6")
    embed("com.fasterxml.jackson.core:jackson-core:2.13.2")
    embed("com.fifesoft:rsyntaxtextarea:3.2.0")
}

tasks.processResources {
    rename("(.+_at.cfg)", "META-INF/$1")

    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.jar {
    from(embed.files.map {
        exclude("META-INF/LICENSE")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("LICENSE.txt")
        exclude("META-INF/LICENSE.txt")
        exclude("**/module-info.class")
        exclude("META-INF/versions/9/**")
        zipTree(it)
    })

    manifest.attributes(mapOf(
        "ModSide" to "CLIENT",
        "FMLAT" to accessTransformerName,
        "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
        "TweakOrder" to "0",
        "MixinConfigs" to mixinName,
    ))
}
