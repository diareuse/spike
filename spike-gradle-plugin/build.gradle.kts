plugins {
    id("spike.jvm")
    `java-gradle-plugin`
}

group = "spike.gradle"

gradlePlugin {
    plugins {
        create("spike") {
            id = "io.github.diareuse.spike"
            displayName = "Spike"
            description = "Sets up Spike dependency injection for Kotlin JVM, Android and Multiplatform projects."
            implementationClass = "spike.gradle.SpikePlugin"
        }
    }
}

dependencies {
    compileOnly(libs.plugin.multiplatform)
    implementation(libs.com.google.devtools.ksp.gradle.plugin)
}

tasks.processResources {
    val version = project.version.toString()
    inputs.property("version", version)
    filesMatching("spike-gradle-plugin.properties") {
        expand("version" to version)
    }
}
