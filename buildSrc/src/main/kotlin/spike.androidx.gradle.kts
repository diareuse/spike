plugins {
    id("base.kotlin.multiplatform")
    id("base.android")
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktfmt().googleStyle().configure {
            it.setBlockIndent(4)
            it.setContinuationIndent(4)
        }
    }
}

kotlin {

    iosX64()
    iosArm64()
    iosSimulatorArm64()

}