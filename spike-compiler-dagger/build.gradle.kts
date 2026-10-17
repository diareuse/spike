import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("spike.jvm")
    id(libs.plugins.google.ksp.get().pluginId)
}

group = "spike.compiler.dagger"

kotlin {
    explicitApi = ExplicitApiMode.Disabled
}

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {
    api(project(":spike-compiler-jsr"))
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(project(":spike-core"))
    implementation(libs.google.ksp.api)
    ksp(libs.spike.compiler)
}
