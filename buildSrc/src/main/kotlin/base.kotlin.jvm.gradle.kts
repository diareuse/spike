import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("com.vanniktech.maven.publish")
}

kotlin {
    jvmToolchain(17)
    explicitApi()
    compilerOptions {
        allWarningsAsErrors = true
    }
}

tasks.test {
    useJUnitPlatform()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xcontext-parameters"))
}

dependencies {
    testImplementation(kotlin("test"))
}