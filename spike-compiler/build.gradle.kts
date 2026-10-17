import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode

plugins {
    id("spike.jvm")
    id(libs.plugins.google.ksp.get().pluginId)
}

group = "spike.compiler"
version = ""

kotlin {
    explicitApi = ExplicitApiMode.Disabled
}

ksp {
    arg("spike.viewmodel.enabled", "false")
}

dependencies {
    implementation(project(":spike-core"))
    runtimeOnly(project(":spike-compiler-androidx"))
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(libs.google.ksp.api)
    api(libs.kotlin.reflect)
    ksp(libs.spike.compiler)
    testImplementation(project(":spike-androidx"))
    testImplementation(project(":spike-ktor"))
    testImplementation(project(":spike-compiler-jsr"))
    testImplementation(project(":spike-compiler-dagger"))
    testImplementation(project(":spike-compiler-kotlin-inject"))
    testImplementation(project(":spike-compiler-metro"))
    testImplementation(gradleTestKit())
    testImplementation(libs.google.ksp.standalone)
    testImplementation(libs.google.ksp.common.deps)
    testImplementation(libs.kotlin.compiler.embeddable)
}
fun fixtureLibraries(name: String, vararg notations: Any) = configurations.create(name) {
    isCanBeConsumed = false
    isCanBeResolved = true
    val runtime = configurations.testRuntimeClasspath.get().attributes
    attributes {
        runtime.keySet().forEach { key ->
            @Suppress("UNCHECKED_CAST")
            attribute(key as Attribute<Any>, runtime.getAttribute(key)!!)
        }
    }
    dependencies.addAll(notations.map { project.dependencies.create(it) })
}

val fixtureKotlin = fixtureLibraries("fixtureKotlin", project(":spike-core"), project(":spike-test"), "org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")
val fixtureAndroidx = fixtureLibraries(
    "fixtureAndroidx",
    project(":spike-core"),
    project(":spike-androidx"),
    "org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}",
    libs.androidx.lifecycle.viewmodel.savedstate.get()
)

val fixtureJsr = fixtureLibraries(
    "fixtureJsr",
    project(":spike-core"),
    project(":spike-jsr"),
    "org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}",
    libs.javax.inject.get(),
    libs.jakarta.inject.get(),
)

val fixtureDagger = fixtureLibraries(
    "fixtureDagger",
    project(":spike-core"),
    project(":spike-jsr"),
    project(":spike-dagger"),
    "org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}",
    libs.javax.inject.get(),
    libs.dagger.get(),
)

val fixtureKotlinInject = fixtureLibraries(
    "fixtureKotlinInject",
    project(":spike-core"),
    "org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}",
    libs.kotlin.inject.runtime.get(),
)

val fixtureMetro = fixtureLibraries(
    "fixtureMetro",
    project(":spike-core"),
    project(":spike-metro"),
    "org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}",
    libs.metro.runtime.get(),
)

tasks.test {
    inputs.files(fixtureKotlin, fixtureAndroidx, fixtureJsr, fixtureDagger, fixtureKotlinInject, fixtureMetro).withPropertyName("fixtureLibraries")
    jvmArgumentProviders += CommandLineArgumentProvider {
        listOf(
            "-Dspike.fixture.kotlin=${fixtureKotlin.asPath}",
            "-Dspike.fixture.androidx=${fixtureAndroidx.asPath}",
            "-Dspike.fixture.jsr=${fixtureJsr.asPath}",
            "-Dspike.fixture.dagger=${fixtureDagger.asPath}",
            "-Dspike.fixture.kotlininject=${fixtureKotlinInject.asPath}",
            "-Dspike.fixture.metro=${fixtureMetro.asPath}"
        )
    }
}
