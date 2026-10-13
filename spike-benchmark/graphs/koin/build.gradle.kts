plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin { jvmToolchain(17) }

dependencies {
    api(bench.koin.core)
}
