plugins { id("spike.multiplatform") }

group = "spike"

kotlin {
    android { namespace = "spike" }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.atomicfu)
        }
    }
}

dependencies { add("kspJvmTest", project(":spike-compiler")) }
