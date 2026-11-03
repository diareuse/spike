plugins {
    `java-test-fixtures`
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
        named("testFixtures") {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/testFixtures")))
        }
    }
}

dependencies {
    api("io.github.diareuse:spike-androidx")
    api(libs.androidx.lifecycle.viewmodel.savedstate)
    testFixturesApi("io.github.diareuse:spike")
}
