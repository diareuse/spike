plugins {
    `application`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

application {
    mainClass = "MainKt"
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir(project.projectDir.resolve("src/main"))
        }
    }
}

ksp {
    providers.gradleProperty("spikeWarm").orNull?.let { arg("spike.warm", it) }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("local.spike:spike")
    ksp(files("compiler.jar"))
    providers.gradleProperty("spikeProcessorDirectory").orNull?.let {
        mkdir(it)
        ksp(files(it))
    }
    ksp("local.spike:spike-compiler")
}
