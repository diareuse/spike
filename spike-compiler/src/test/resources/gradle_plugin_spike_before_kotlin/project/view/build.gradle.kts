plugins {
    id("io.github.diareuse.spike")
    alias(libs.plugins.kotlin.jvm)
}

spike {
    library = true
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
    api("io.github.diareuse:spike-androidx")
    api(libs.androidx.lifecycle.viewmodel.savedstate)
}

tasks.register<JavaExec>("runTestMain") {
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass = "com.example.view.TestMainKt"
}
