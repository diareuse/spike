plugins {
    `application`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
}

application {
    mainClass = "com.example.app.MainKt"
}

kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src")))
        }
    }
}

dependencies {
    implementation(project(":library"))
    implementation(project(":feature"))
    implementation(kotlin("stdlib"))
    implementation("local.spike:spike")
    ksp("local.spike:spike-compiler")
}
