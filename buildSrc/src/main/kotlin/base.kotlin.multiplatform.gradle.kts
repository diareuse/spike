plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.google.devtools.ksp")
    id("com.vanniktech.maven.publish")
}

kotlin {
    jvmToolchain(17)
    jvm()
    explicitApi()
    compilerOptions {
        allWarningsAsErrors = true
    }
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}