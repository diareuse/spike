plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("io.github.diareuse.spike")
}

kotlin {
    js {
        nodejs()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":a:ui"))
            implementation(project(":b:ui"))
        }
    }
}
