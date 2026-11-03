plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("io.github.diareuse.spike")
}

spike {
    library = true
}

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            api("io.github.diareuse:spike-androidx")
            api(libs.androidx.lifecycle.viewmodel.savedstate)
        }
    }
}

tasks.register<JavaExec>("runTestMain") {
    val test = kotlin.jvm().compilations.getByName("test")
    classpath = files(test.output.allOutputs, test.runtimeDependencyFiles)
    mainClass = "com.example.view.TestMainKt"
}
