plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(bench.plugins.metro)
}

kotlin { jvmToolchain(17) }
