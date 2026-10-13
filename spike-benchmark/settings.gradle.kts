pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal {
            content { includeGroup("io.github.diareuse") }
        }
        mavenCentral()
        google()
    }
    versionCatalogs {
        create("libs") { from(files("../gradle/libs.versions.toml")) }
        create("bench") { from(files("gradle/bench.versions.toml")) }
    }
}

rootProject.name = "spike-benchmark"

include(":generator")
include(":graphs:manual")
include(":graphs:spike")
include(":graphs:dagger")
include(":graphs:metro")
include(":graphs:kotlin-inject")
include(":graphs:koin")
include(":runtime")
