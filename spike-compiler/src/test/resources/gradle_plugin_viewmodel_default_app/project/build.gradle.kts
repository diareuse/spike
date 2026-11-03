plugins {
    alias(libs.plugins.kotlin.jvm)
    id("io.github.diareuse.spike")
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/main")))
        }
    }
}

dependencies {
    implementation("io.github.diareuse:spike-androidx")
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
}

configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.github.diareuse:spike")).using(module("spike:spike-core:unspecified"))
        substitute(module("io.github.diareuse:spike-androidx")).using(module("spike:spike-androidx:unspecified"))
        substitute(module("io.github.diareuse:spike-compiler")).using(module("spike.compiler:spike-compiler:unspecified"))
    }
}

tasks.register<JavaExec>("runMain") {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "MainKt"
}
