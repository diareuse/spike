plugins {
    `application`
    alias(libs.plugins.kotlin.jvm)
    id("io.github.diareuse.spike")
}

application {
    mainClass = "com.example.app.MainKt"
}

kotlin {
    sourceSets {
        main {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/main")))
        }
        test {
            kotlin.setSrcDirs(listOf(project.projectDir.resolve("src/test")))
        }
    }
}

dependencies {
    implementation(project(":view"))
    testImplementation(testFixtures(project(":view")))
}

tasks.register<JavaExec>("runTestMain") {
    classpath = sourceSets.test.get().runtimeClasspath
    mainClass = "com.example.app.TestMainKt"
}
