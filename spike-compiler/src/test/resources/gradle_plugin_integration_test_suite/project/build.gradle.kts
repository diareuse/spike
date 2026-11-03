plugins {
    `jvm-test-suite`
    alias(libs.plugins.kotlin.jvm)
    id("io.github.diareuse.spike")
}

repositories {
    mavenCentral()
    google()
}

testing {
    suites {
        register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())
                implementation("io.github.diareuse:spike")
            }
        }
    }
}

kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/main")))
        }
        named("integrationTest") {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/integrationTest")))
        }
    }
}

configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.github.diareuse:spike")).using(module("spike:spike-core:unspecified"))
        substitute(module("io.github.diareuse:spike-compiler")).using(module("spike.compiler:spike-compiler:unspecified"))
    }
}

tasks.register<JavaExec>("runIntegrationMain") {
    classpath = sourceSets.getByName("integrationTest").runtimeClasspath
    mainClass = "IntegrationMainKt"
}
