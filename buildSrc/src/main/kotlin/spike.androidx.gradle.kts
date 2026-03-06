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

}