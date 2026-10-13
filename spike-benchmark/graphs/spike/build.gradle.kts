plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

kotlin { jvmToolchain(17) }

dependencies {
    implementation(bench.spike)
    ksp(bench.spike.compiler)
}
