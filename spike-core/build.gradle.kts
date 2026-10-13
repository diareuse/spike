plugins { id("spike.multiplatform") }

group = "spike"

kotlin {
    android { namespace = "spike" }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.atomicfu)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

dependencies { add("kspJvmTest", project(":spike-compiler")) }
