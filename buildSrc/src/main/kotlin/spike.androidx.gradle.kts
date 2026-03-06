plugins {
    id("base.kotlin.multiplatform")
    id("base.android")
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktlint()
    }
}

kotlin {

    iosX64()
    iosArm64()
    iosSimulatorArm64()

}