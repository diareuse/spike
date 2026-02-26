plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "spike.graph"
version = ""

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(project(":spike-core"))
    testImplementation(libs.kotlin.test)
}