plugins {
    id("spike.jvm")
    `java-gradle-plugin`
    id(libs.plugins.google.ksp.get().pluginId)
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

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {
    implementation(project(":spike-core"))
    ksp(libs.spike.compiler)
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
