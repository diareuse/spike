import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("spike.jvm")
    id(libs.plugins.google.ksp.get().pluginId)
}

group = "spike.compiler.conversion"

kotlin {
    explicitApi = ExplicitApiMode.Disabled
}

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {
    api(project(":spike-core"))
    implementation(libs.google.ksp.api)
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    ksp(libs.spike.compiler)
}
