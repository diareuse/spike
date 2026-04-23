plugins { id("spike.multiplatform") }

group = "spike"

kotlin {
    androidLibrary { namespace = "spike" }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.atomicfu)
        }
    }
}

dependencies { add("kspJvmTest", project(":spike-compiler")) }
