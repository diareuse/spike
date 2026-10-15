plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(bench.plugins.kotlinx.benchmark)
    kotlin("plugin.allopen")
}

kotlin { jvmToolchain(17) }

allOpen { annotation("org.openjdk.jmh.annotations.State") }

dependencies {
    implementation(bench.kotlinx.benchmark.runtime)
    implementation(project(":graphs:manual"))
    implementation(project(":graphs:spike"))
    implementation(project(":graphs:dagger"))
    implementation(project(":graphs:metro"))
    implementation(project(":graphs:kotlin-inject"))
    implementation(project(":graphs:koin"))
}

val only = providers.gradleProperty("benchmarkOnly").orNull?.replace("-", "_")
fun selected(benchmark: String) = if (only == null) benchmark else "$benchmark\\.$only$"

benchmark {
    targets { register("main") }
    configurations {
        named("main") {
            include(selected("GraphBenchmarkCreate"))
            include(selected("GraphBenchmarkResolve"))
            mode = "avgt"
            outputTimeUnit = "us"
            warmups = 5
            iterations = 10
            iterationTime = 1
            iterationTimeUnit = "s"
            advanced("jvmForks", 3)
        }
        register("cold") {
            include(selected("GraphBenchmarkColdStart"))
            outputTimeUnit = "us"
            warmups = 0
            iterations = 1
            advanced("jvmForks", 20)
        }
    }
}

tasks.matching { it.name == "mainColdBenchmark" }.configureEach { mustRunAfter("mainBenchmark") }

dependencies { testImplementation(kotlin("test")) }

tasks.test { useJUnitPlatform() }
