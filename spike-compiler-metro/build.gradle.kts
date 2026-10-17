import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("spike.jvm")
    id(libs.plugins.google.ksp.get().pluginId)
}

group = "spike.compiler.metro"

kotlin {
    explicitApi = ExplicitApiMode.Disabled
}

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {
    implementation(project(":spike-compiler-conversion"))
    implementation(project(":spike-core"))
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(libs.google.ksp.api)
    ksp(libs.spike.compiler)
}
