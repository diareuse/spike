plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(bench.plugins.metro) apply false
    alias(bench.plugins.kotlinx.benchmark) apply false
    kotlin("plugin.allopen") version libs.versions.kotlin.get() apply false
}
