plugins {
    `application`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

application {
    mainClass = "com.example.test.MainKt"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(project(":app"))
    implementation("local.spike:spike-test")
    ksp("local.spike:spike-compiler")
}
