plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

// https://github.com/Kotlin/multiplatform-library-template/blob/main/library/build.gradle.kts
kotlin {
    jvmToolchain(21)
    jvm()
    androidLibrary {
        namespace = "spike.androidx.compose"
        compileSdk = 36
        minSdk = 1
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.androidx.lifecycle.viewmodel.savedstate)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(project(":spike-androidx"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
