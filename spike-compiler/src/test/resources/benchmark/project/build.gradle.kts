plugins {
    `application`
    kotlin("jvm") version "2.3.20"
    id("com.google.devtools.ksp") version "2.3.4"
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

application {
    mainClass = "MainKt"
}

kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src")))
        }
    }
}

val testClasspath: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(files(testClasspath.split(";")))
    ksp(files(testClasspath.split(";")))
}