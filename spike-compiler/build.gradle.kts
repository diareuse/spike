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
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.squareup.kotlinpoet.ksp)
    implementation(libs.google.ksp.api)
    api(libs.kotlin.reflect)
    ksp(libs.spike.compiler)
    testImplementation(project(":spike-androidx"))
    testImplementation(project(":spike-ktor"))
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

val fixtureKotlin = fixtureLibraries("fixtureKotlin", project(":spike-core"), "org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}")
val fixtureAndroidx = fixtureLibraries(
    "fixtureAndroidx",
    project(":spike-core"),
    project(":spike-androidx"),
    "org.jetbrains.kotlin:kotlin-stdlib:${libs.versions.kotlin.get()}",
    libs.androidx.lifecycle.viewmodel.savedstate.get()
)

tasks.test {
    inputs.files(fixtureKotlin, fixtureAndroidx).withPropertyName("fixtureLibraries")
    jvmArgumentProviders += CommandLineArgumentProvider {
        listOf(
            "-Dspike.fixture.kotlin=${fixtureKotlin.asPath}",
            "-Dspike.fixture.androidx=${fixtureAndroidx.asPath}"
        )
    }
}
