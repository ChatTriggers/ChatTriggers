plugins {
    kotlin("jvm") version "1.6.10" apply false
    id("gg.essential.loom") version "0.10.0.1" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    val forge11701 = createNode("1.17.1-forge", 11701, "mcp")
    val forge10809 = createNode("1.8.9-forge", 10809, "mcp")

    forge11701.link(forge10809, file("versions/1.17.1-1.8.9.txt"))
}
