plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
    application
}

kotlin { jvmToolchain(17) }

application { mainClass = "bench.generator.MainKt" }

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {
    implementation("io.github.diareuse:spike:${libs.versions.spike.get()}")
    ksp(libs.spike.compiler)
    testImplementation(kotlin("test"))
}

tasks.test { useJUnitPlatform() }

tasks.named<JavaExec>("run") {
    args(listOfNotNull(rootDir.path, providers.gradleProperty("benchmarkSize").orNull))
}
