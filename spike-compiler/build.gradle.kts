import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("spike.jvm")
}

group = "spike.compiler"
version = ""

kotlin {
    explicitApi = ExplicitApiMode.Disabled
}

dependencies {
    implementation(project(":spike-core"))
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(libs.google.ksp.api)
    api(libs.kotlin.reflect)
    testImplementation(project(":spike-androidx"))
    testImplementation(project(":spike-ktor"))
    testImplementation(gradleTestKit())
}