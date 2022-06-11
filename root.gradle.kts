plugins {
    kotlin("jvm") version "1.6.10" apply false
    id("gg.essential.loom") version "0.10.0.1" apply false
    id("gg.essential.multi-version.root")
}

preprocess {
    val fabric11701 = createNode("1.17.1-fabric", 11701, "yarn")
    val forge11701 = createNode("1.17.1-forge", 11701, "srg")
    val forge10809 = createNode("1.8.9-forge", 10809, "srg")

    fabric11701.link(forge11701)
    forge11701.link(forge10809, file("versions/forge1.17.1-1.8.9.txt"))
}
