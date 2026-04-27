plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.google.ksp)
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm {
        @Suppress("OPT_IN_USAGE")
        mainRun {
            mainClass = "MainKt"
        }
    }
    js {
        browser()
        nodejs()
    }
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.diareuse:spike")
            implementation(kotlin("stdlib"))
        }
        jvmMain.dependencies {}
        jsMain.dependencies {}
    }
}


dependencies {
    add("kspJvm", "io.github.diareuse:spike-compiler")
    add("kspJs", "io.github.diareuse:spike-compiler")
}