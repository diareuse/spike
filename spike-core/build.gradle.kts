plugins {
    id("spike.multiplatform")
}

group = "spike"

kotlin {
    androidLibrary {
        namespace = "spike"
    }
}

dependencies {
    add("kspJvmTest", project(":spike-compiler"))
}