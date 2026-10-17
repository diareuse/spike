@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("spike.multiplatform.compose")
}

kotlin {
    android {
        namespace = "spike.androidx.compose"
    }
    js {
        browser()
    }
    wasmJs {
        browser()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel.savedstate)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(project(":spike-androidx"))
        }
    }
}
