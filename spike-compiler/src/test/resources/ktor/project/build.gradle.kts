plugins {
    `application`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

application {
    mainClass = "MainKt"
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir(project.projectDir.resolve("src/main"))
        }
        test {
            kotlin.srcDir(project.projectDir.resolve("src/test"))
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.diareuse:spike")
    implementation("io.github.diareuse:spike-ktor")
    ksp("io.github.diareuse:spike-compiler")
    implementation(libs.ktor.core)
    implementation(libs.ktor.client.java)
    testImplementation(libs.ktor.server.test)
    testImplementation(kotlin("test-junit"))
}