plugins {
    kotlin("jvm")
}

dependencies {
    compileOnly("org.jetbrains.dokka:dokka-core:1.6.21")
    compileOnly("org.jetbrains.dokka:dokka-base:1.6.21")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")
}