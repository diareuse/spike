plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    id("io.github.diareuse.spike")
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    androidTarget()
}

android {
    namespace = "com.example.library"
    compileSdk = 37
    defaultConfig {
        minSdk = 24
    }
}

configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.github.diareuse:spike")).using(module("spike:spike-core:unspecified"))
        substitute(module("io.github.diareuse:spike-compiler")).using(module("spike.compiler:spike-compiler:unspecified"))
    }
}
