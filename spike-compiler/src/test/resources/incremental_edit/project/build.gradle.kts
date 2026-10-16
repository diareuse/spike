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
            kotlin.srcDir(project.projectDir.resolve("src/main"))
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("local.spike:spike")
    ksp("local.spike:spike-compiler")
}