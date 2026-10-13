plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

kotlin { jvmToolchain(17) }

dependencies {
    implementation(bench.kotlin.inject.runtime)
    ksp(bench.kotlin.inject.compiler)
}
