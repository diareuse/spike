plugins {
    alias(libs.plugins.kotlin.jvm)
    id("io.github.diareuse.spike")
}

kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/main")))
        }
    }
}

dependencies {
    api("io.github.diareuse:spike-androidx")
    api(libs.androidx.lifecycle.viewmodel.savedstate)
}
