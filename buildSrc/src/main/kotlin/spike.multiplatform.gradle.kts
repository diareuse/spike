@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("base.kotlin.multiplatform")
    id("base.android")
}

kotlin {

    iosArm64()
    iosSimulatorArm64()

    tvosArm64()
    tvosSimulatorArm64()

    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()

    linuxX64()
    linuxArm64()

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
tasks.register("test") {
    dependsOn(tasks.matching { it.name == "jvmTest" })
}
