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
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/main")))
        }
        test {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/test")))
        }
    }
}

dependencies {
    implementation("io.github.diareuse:spike-androidx")
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    testImplementation("spike.test:spike-test:unspecified")
    add("kspTest", "io.github.diareuse:spike-compiler")
}

configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.github.diareuse:spike")).using(module("spike:spike-core:unspecified"))
        substitute(module("io.github.diareuse:spike-androidx")).using(module("spike:spike-androidx:unspecified"))
        substitute(module("io.github.diareuse:spike-compiler")).using(module("spike.compiler:spike-compiler:unspecified"))
    }
}

tasks.register<JavaExec>("runTestMain") {
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass = "TestMainKt"
}
