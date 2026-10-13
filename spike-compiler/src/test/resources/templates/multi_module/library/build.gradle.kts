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
    implementation("local.spike:spike")
    ksp("local.spike:spike-compiler")
}