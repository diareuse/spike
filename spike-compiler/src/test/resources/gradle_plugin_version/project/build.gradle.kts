plugins {
    alias(libs.plugins.kotlin.jvm)
    id("io.github.diareuse.spike")
}

tasks.register("assertSpikeVersion") {
    val versions = listOf("implementation", "ksp")
        .flatMap { configurations.getByName(it).dependencies }
        .filter { it.group == "io.github.diareuse" }
        .map { "${it.name}:${it.version}" }
    doLast {
        check(versions == listOf("spike:1.2.3", "spike-compiler:1.2.3")) { "Spike was added as $versions" }
    }
}
