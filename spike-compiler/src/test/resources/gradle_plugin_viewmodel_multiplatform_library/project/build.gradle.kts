plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
}

subprojects {
    repositories {
        mavenCentral()
        google()
    }
    configurations.configureEach {
        resolutionStrategy.dependencySubstitution {
            substitute(module("io.github.diareuse:spike")).using(module("spike:spike-core:unspecified"))
            substitute(module("io.github.diareuse:spike-androidx")).using(module("spike:spike-androidx:unspecified"))
            substitute(module("io.github.diareuse:spike-compiler")).using(module("spike.compiler:spike-compiler:unspecified"))
        }
    }
}
