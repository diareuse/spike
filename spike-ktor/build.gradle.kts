plugins {
    id("spike.jvm")
}

group = "spike.ktor"

dependencies {
    implementation(project(":spike-core"))
    implementation(libs.ktor.core)
}