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
    flavorDimensions += "tier"
    productFlavors {
        create("domain") {
            dimension = "tier"
        }
        create("free") {
            dimension = "tier"
        }
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

tasks.register("assertFlavorKspConfigurations") {
    val missing = listOf("kspDomain", "kspTestDomain", "kspAndroidTestDomain").filter { name ->
        configurations.findByName(name)?.dependencies?.none { it.name == "spike-compiler" } ?: true
    }
    doLast {
        check(missing.isEmpty()) { "Spike's compiler is missing from $missing" }
    }
}
