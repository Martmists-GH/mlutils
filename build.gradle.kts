plugins {
    kotlin("jvm") version "2.0.0"
}

group = "com.martmists"
version = "0.1.0"

repositories {
    maven("https://maven.martmists.com/releases")
    mavenCentral()
}

dependencies {
    // Vector implementation
    api("com.martmists.ndarray-simd:ndarray-simd:1.0.6")

    // Compat: Exposed+PGVector
    compileOnly("com.pgvector:pgvector:0.1.6")
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
