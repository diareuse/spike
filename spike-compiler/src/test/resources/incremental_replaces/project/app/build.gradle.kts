plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("local.spike:spike")
    ksp("local.spike:spike-compiler")
}
