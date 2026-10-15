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
            implementation("local.spike:spike")
            implementation(kotlin("stdlib"))
        }
        jvmMain.dependencies {}
        jsMain.dependencies {}
    }
}


dependencies {
    add("kspJvm", "local.spike:spike-compiler")
    add("kspJs", "local.spike:spike-compiler")
}