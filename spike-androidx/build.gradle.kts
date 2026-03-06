plugins {
    id("spike.androidx")
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