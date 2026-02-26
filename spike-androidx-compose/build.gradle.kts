plugins {
    id("spike.multiplatform.compose")
}

kotlin {
    androidLibrary {
        namespace = "spike.androidx.compose"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel.savedstate)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(project(":spike-androidx"))
        }
    }
}
