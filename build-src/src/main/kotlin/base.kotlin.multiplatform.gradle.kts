plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.google.devtools.ksp")
    id("com.vanniktech.maven.publish")
}

kotlin {
    jvmToolchain(17)
    jvm()
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}