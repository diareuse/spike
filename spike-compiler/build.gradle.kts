plugins {
    id("spike.jvm")
}

group = "spike.compiler"
version = ""

dependencies {
    implementation(project(":spike-core"))
    implementation(project(":spike-graph"))
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(libs.google.ksp.api)
    api(libs.kotlin.reflect)
    testImplementation(project(":spike-androidx"))
    testImplementation(project(":spike-ktor"))
    testImplementation(gradleTestKit())
}