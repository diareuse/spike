plugins {
    `application`
    alias(libs.plugins.kotlin.jvm)
    id("io.github.diareuse.spike")
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
    }
}

configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.github.diareuse:spike")).using(module("spike:spike-core:unspecified"))
        substitute(module("io.github.diareuse:spike-compiler")).using(module("spike.compiler:spike-compiler:unspecified"))
    }
}
