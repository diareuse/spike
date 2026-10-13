plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

kotlin { jvmToolchain(17) }

application { mainClass = "bench.generator.MainKt" }

dependencies { testImplementation(kotlin("test")) }

tasks.test { useJUnitPlatform() }

tasks.named<JavaExec>("run") {
    args(rootDir.path)
}
