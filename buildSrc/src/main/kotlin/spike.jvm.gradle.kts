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

