@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("base.kotlin.multiplatform")
    id("base.android")
    id("dev.detekt")
}

detekt {
    buildUponDefaultConfig = true
    autoCorrect = true
    parallel = true
    baseline.set(file(".config/baseline"))
    val configFile = rootProject.file(".config/detekt.yml")
    if (configFile.exists()) {
        config.setFrom(configFile)
    }
}

kotlin {

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()

    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()

    linuxX64()
    linuxArm64()

    macosX64()
    macosArm64()

    js {
        browser()
        nodejs()
    }
    wasmJs {
        browser()
        nodejs()
        d8()
    }
    wasmWasi {
        nodejs()
    }

    mingwX64()

}