plugins {
    id("base.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    explicitApi()
    android {
        compileSdk = 37
        minSdk = 1
    }
    compilerOptions {
        allWarningsAsErrors = true
    }
}