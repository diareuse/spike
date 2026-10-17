package spike.compiler.harness

import java.io.File

enum class Libraries(private val property: String) {
    Kotlin("spike.fixture.kotlin"),
    Androidx("spike.fixture.androidx"),
    Jsr("spike.fixture.jsr"),
    Dagger("spike.fixture.dagger"),
    KotlinInject("spike.fixture.kotlininject"),
    Metro("spike.fixture.metro");

    fun files(): List<File> = checkNotNull(System.getProperty(property)) {
        "System property $property is missing. Run the tests through Gradle, " +
            "which sets it from the fixture library configurations."
    }.split(File.pathSeparator).map(::File)
}
