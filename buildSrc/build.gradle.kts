plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.plugin.jvm)
    implementation(libs.plugin.kotlin.compose)
    implementation(libs.plugin.compose)
    implementation(libs.plugin.multiplatform)
    implementation(libs.android.kotlin.multiplatform.library)
    implementation(libs.com.google.devtools.ksp.gradle.plugin)
    implementation(libs.com.vanniktech.maven.publish.gradle.plugin)
    implementation(libs.dev.detekt.gradle.plugin)
}