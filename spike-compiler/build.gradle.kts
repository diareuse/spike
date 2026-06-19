import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("spike.jvm")
    id(libs.plugins.google.ksp.get().pluginId)
}

group = "spike.compiler"
version = ""

kotlin {
    explicitApi = ExplicitApiMode.Disabled
}

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {
    implementation(project(":spike-core"))
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(libs.google.ksp.api)
    api(libs.kotlin.reflect)
    ksp(libs.spike.compiler)
    testImplementation(project(":spike-androidx"))
    testImplementation(project(":spike-ktor"))
    testImplementation(gradleTestKit())
}