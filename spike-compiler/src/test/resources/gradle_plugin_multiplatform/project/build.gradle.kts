plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("io.github.diareuse.spike")
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm {
        @Suppress("OPT_IN_USAGE")
        mainRun {
            mainClass = "MainKt"
        }
    }
    js {
        nodejs()
    }
}

configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.github.diareuse:spike")).using(module("spike:spike-core:unspecified"))
        substitute(module("io.github.diareuse:spike-compiler")).using(module("spike.compiler:spike-compiler:unspecified"))
    }
}
