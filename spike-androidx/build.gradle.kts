@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("spike.androidx")
}

kotlin {
    androidLibrary {
        namespace = "spike.androidx"
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
            implementation(project(":spike-core"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}