import org.gradle.kotlin.dsl.implementation

plugins {
    kotlin("jvm") version "2.2.21"
    id("com.google.devtools.ksp") version "2.3.4"
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/main")))
        }
        test {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/test")))
        }
    }
}

val testClasspath: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.ktor:ktor-server-core:3.4.0")
    implementation("io.ktor:ktor-client-java:3.4.0")
    testImplementation("io.ktor:ktor-server-test-host:3.4.0")
    testImplementation(kotlin("test"))
    implementation(files(testClasspath.split(";")))
    ksp(files(testClasspath.split(";")))
}