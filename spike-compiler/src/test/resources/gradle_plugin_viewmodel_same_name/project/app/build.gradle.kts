plugins {
    `application`
    alias(libs.plugins.kotlin.jvm)
    id("io.github.diareuse.spike")
}

application {
    mainClass = "com.example.app.MainKt"
}

kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/main")))
        }
    }
}

dependencies {
    implementation(project(":a:ui"))
    implementation(project(":b:ui"))
}
