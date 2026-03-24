plugins {
    `application`
    kotlin("jvm") version "2.2.21"
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

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.diareuse:spike:unspecified")
    ksp("io.github.diareuse:spike-compiler:unspecified")
}