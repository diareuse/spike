plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "spike.compiler"
version = ""

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":spike-core"))
    implementation(project(":spike-graph"))
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(libs.google.ksp.api)
    api(libs.kotlin.reflect)
    testImplementation(libs.kotlin.test)
    testImplementation(project(":spike-androidx"))
    testImplementation(project(":spike-ktor"))
    testImplementation(gradleTestKit())
}