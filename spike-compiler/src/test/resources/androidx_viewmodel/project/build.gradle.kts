plugins {
    `application`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

application {
    mainClass = "MainKt"
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
    implementation("io.github.diareuse:spike-androidx")
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    ksp("io.github.diareuse:spike-compiler")
}