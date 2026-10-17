pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}
rootProject.name = "spike"
include(":spike-core")
include(":spike-compiler")
include(":spike-androidx")
include(":spike-androidx-compose")
include(":spike-ktor")
include(":spike-test")
include(":spike-gradle-plugin")
include(":spike-jsr")
include(":spike-compiler-androidx")
include(":spike-compiler-conversion")
include(":spike-compiler-jsr")
include(":spike-dagger")
include(":spike-compiler-dagger")
include(":spike-compiler-kotlin-inject")
