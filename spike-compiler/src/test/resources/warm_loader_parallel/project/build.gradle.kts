plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.google.ksp) apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.google.devtools.ksp")

    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        "implementation"(kotlin("stdlib"))
        "implementation"("local.spike:spike")
        "ksp"("local.spike:spike-compiler")
    }

    extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        sourceSets.getByName("main").kotlin.srcDir(projectDir.resolve("src/main"))
    }
}
