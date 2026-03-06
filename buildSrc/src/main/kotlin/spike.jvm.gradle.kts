import gradle.kotlin.dsl.accessors._c24a720b039b223a012fa023f45bc99b.detekt

plugins {
    id("base.kotlin.jvm")
    id("dev.detekt")
}

detekt {
    buildUponDefaultConfig = true
    autoCorrect = true
    parallel = true
    baseline.set(file(".config/baseline"))
    val configFile = rootProject.file(".config/detekt.yml")
    if (configFile.exists()) {
        config.setFrom(configFile)
    }
}

