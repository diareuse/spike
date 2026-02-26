plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

group = "spike"
version = ""

// https://github.com/Kotlin/multiplatform-library-template/blob/main/library/build.gradle.kts
kotlin {
    jvmToolchain(21)
    jvm()
    androidLibrary {
        namespace = "spike"
        compileSdk = 36
        minSdk = 1
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    //ksp project(":spike-compiler")
    add("kspJvmTest", project(":spike-compiler"))
}