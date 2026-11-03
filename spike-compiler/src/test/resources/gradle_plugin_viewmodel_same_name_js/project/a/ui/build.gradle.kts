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
            api("io.github.diareuse:spike-androidx")
            api(libs.androidx.lifecycle.viewmodel.savedstate)
        }
    }
}
