buildscript {
    repositories {
        mavenCentral()
        maven { url = uri("https://maven.fabricmc.net/") }
    }
    dependencies {
        classpath("net.fabricmc:fabric-loom:1.6-SNAPSHOT")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
        classpath("org.jetbrains.kotlin:kotlin-serialization:2.1.20")
    }
}

plugins {
    id("fabric-loom")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
}

version = "1.0.0"
group = "com.pulsehud"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.fabricmc.net/") }
}

dependencies {
    loomModImplementation("net.fabricmc:fabric-loader:*")
    implementation("com.mojang:yaml:2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    testImplementation("io.kotest:kotest-runner-junit5:6.2.0")
    testImplementation("io.kotest:kotest-assertions-core:6.2.0")
}

tasks.register("clean") {
    delete(rootProject.buildDir)
}
