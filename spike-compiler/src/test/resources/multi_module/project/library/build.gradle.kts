plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

repositories {
    mavenCentral()
    google()
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
    implementation("io.github.diareuse:spike")
    ksp("io.github.diareuse:spike-compiler")
}