plugins {
    alias(libs.plugins.kotlin.jvm)
    id("io.github.diareuse.spike")
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
    add("kspTest", "io.github.diareuse:spike-compiler")
}

tasks.register<JavaExec>("runTestMain") {
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass = "com.example.view.TestMainKt"
}
