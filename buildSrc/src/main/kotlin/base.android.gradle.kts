plugins {
    id("base.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    explicitApi()
    androidLibrary {
        compileSdk = 36
        minSdk = 1
    }
    compilerOptions {
        allWarningsAsErrors = true
    }
}