plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

kotlin { jvmToolchain(17) }

ksp {
    arg("dagger.formatGeneratedSource", "disabled")
}

dependencies {
    implementation(bench.dagger)
    ksp(bench.dagger.compiler)
}
