import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "io.tellery.connectors"
version = "0.5.0"

repositories {
    maven {
        url = uri("https://maven.pkg.github.com/tellery/tellery")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
        }
    }
    jcenter()
}

plugins {
    idea
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

dependencies {
    implementation("io.tellery:connector-interface:0.5.0-SNAPSHOT")
    implementation("org.apache.hive:hive-jdbc:2.1.0") {
        exclude(group = "org.slf4j", module = "")
        exclude(group = "log4j", module = "log4j")
        exclude(group = "org.apache.logging.log4j", module = "")
        exclude(group = "io.netty", module = "netty")
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
}

sourceSets.main {
    java.srcDirs("src")
    resources.srcDirs("resources")
}

sourceSets.test {
    java.srcDirs("test")
    resources.srcDirs("testresources")
}

idea {
    module {
        inheritOutputDirs = false
        outputDir = file("$buildDir/classes/kotlin/main")
        testOutputDir = file("$buildDir/classes/kotlin/test")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${project.name}-${project.version}.jar")
    isZip64 = true
}
