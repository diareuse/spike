plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("io.github.diareuse.spike")
}

repositories {
    mavenCentral()
    google()
}

android {
    namespace = "com.example.library"
    compileSdk = 37
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.github.diareuse:spike")).using(module("spike:spike-core:unspecified"))
        substitute(module("io.github.diareuse:spike-compiler")).using(module("spike.compiler:spike-compiler:unspecified"))
    }
}
