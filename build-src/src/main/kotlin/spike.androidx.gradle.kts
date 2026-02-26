plugins {
    id("base.kotlin.multiplatform")
    id("base.android")
}

kotlin {

    iosX64()
    iosArm64()
    iosSimulatorArm64()

}