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

benchmark {
    targets { register("main") }
    configurations {
        named("main") {
            include("GraphBenchmarkCreate")
            include("GraphBenchmarkResolve")
            mode = "avgt"
            outputTimeUnit = "us"
            warmups = 5
            iterations = 10
            iterationTime = 1
            iterationTimeUnit = "s"
            advanced("jvmForks", 3)
        }
        register("cold") {
            include("GraphBenchmarkColdStart")
            outputTimeUnit = "us"
            warmups = 0
            iterations = 1
            advanced("jvmForks", 20)
        }
    }
}

dependencies { testImplementation(kotlin("test")) }

tasks.test { useJUnitPlatform() }
