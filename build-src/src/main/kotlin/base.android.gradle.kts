plugins {
    id("base.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    androidLibrary {
        compileSdk = 36
        minSdk = 1
    }
}