plugins {
    id("spike.androidx")
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    androidLibrary {
        namespace = "spike.androidx"
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