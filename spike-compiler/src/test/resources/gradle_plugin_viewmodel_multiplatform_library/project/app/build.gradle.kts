plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("io.github.diareuse.spike")
}

kotlin {
    jvm {
        @Suppress("OPT_IN_USAGE")
        mainRun {
            mainClass = "com.example.app.MainKt"
        }
    }
    sourceSets {
        commonMain.dependencies {
            implementation(project(":view"))
        }
    }
}
