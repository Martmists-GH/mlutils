plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.martmists"
version = "0.1.0"

repositories {
    maven("https://maven.martmists.com/snapshots")
    mavenCentral()
}

dependencies {
    // Vector implementation
    // IMPORTANT: This is a custom build of viktor, hosted on my own Maven repository. See the `repositories` block for the URL.
    // The only change is that this PR has been merged, everything else is identical:
    // https://github.com/JetBrains-Research/viktor/pull/52
    api("org.jetbrains.bio:viktor:1.2.0")

    // Compat: Exposed+PGVector
    compileOnly("com.pgvector:pgvector:0.1.5")
    compileOnly("org.jetbrains.exposed:exposed-core:0.52.0")

    // Compat: Image Formats
    compileOnly("com.sksamuel.scrimage:scrimage-core:4.1.3")
    compileOnly("com.sksamuel.scrimage:scrimage-webp:4.1.3")
    testRuntimeOnly("com.sksamuel.scrimage:scrimage-core:4.1.3")

    // Compat: Langchain4J
    compileOnly("dev.langchain4j:langchain4j:0.32.0")
    testRuntimeOnly("dev.langchain4j:langchain4j:0.32.0")

    // Compat: kotlinx.dataframe
    compileOnly("org.jetbrains.kotlinx:dataframe:0.13.1")
    testRuntimeOnly("org.jetbrains.kotlinx:dataframe:0.13.1")

    // Tests
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kandy-lets-plot:0.6.0")
}

kotlin {
    jvmToolchain(21)
}
