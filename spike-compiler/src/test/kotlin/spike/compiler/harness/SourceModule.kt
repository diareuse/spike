package spike.compiler.harness

import com.google.devtools.ksp.impl.KotlinSymbolProcessing
import com.google.devtools.ksp.processing.KSPJvmConfig
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSNode
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.util.ServiceLoader

class SourceModule(
    private val name: String,
    private val sources: File,
    private val libraries: List<File>,
    private val workDirectory: File
) {

    val generated: File = workDirectory.resolve("ksp")
    val generatedSources: File = generated.resolve("kotlin")
    val classes: File = workDirectory.resolve("classes")

    fun process(): Processing {
        val log = ProcessingLog()
        val exitCode = KotlinSymbolProcessing(kspConfig(), processors, log).execute()
        return Processing(exitCode == KotlinSymbolProcessing.ExitCode.OK, log.errors, log.exceptions)
    }

    fun compile(): Compilation {
        val messages = ByteArrayOutputStream()
        val exitCode = K2JVMCompiler().exec(
            PrintStream(messages),
            sources.path,
            generatedSources.path,
            "-d", classes.path,
            "-classpath", libraries.joinToString(File.pathSeparator),
            "-module-name", name,
            "-language-version", LanguageVersion,
            "-api-version", LanguageVersion,
            "-jvm-target", JvmTarget,
            "-no-stdlib",
            "-no-reflect"
        )
        return Compilation(exitCode == ExitCode.OK, messages.toString())
    }

    private fun kspConfig() = KSPJvmConfig.Builder().apply {
        moduleName = name
        sourceRoots = listOf(sources)
        javaSourceRoots = emptyList()
        commonSourceRoots = emptyList()
        libraries = this@SourceModule.libraries
        jdkHome = File(System.getProperty("java.home"))
        jvmTarget = JvmTarget
        languageVersion = LanguageVersion
        apiVersion = LanguageVersion
        projectBaseDir = workDirectory
        outputBaseDir = workDirectory
        cachesDir = workDirectory.resolve("caches")
        classOutputDir = generated.resolve("classes")
        kotlinOutputDir = generatedSources
        javaOutputDir = generated.resolve("java")
        resourceOutputDir = generated.resolve("resources")
    }.build()

    data class Error(val message: String, val symbol: KSNode?)

    data class Processing(val succeeded: Boolean, val errors: List<Error>, val exceptions: List<Throwable>) {
        val report: String
            get() = (errors.map { it.message } + exceptions.map { it.stackTraceToString() }).joinToString("\n")
    }

    data class Compilation(val succeeded: Boolean, val messages: String)

    private class ProcessingLog : KSPLogger {
        val errors = mutableListOf<Error>()
        val exceptions = mutableListOf<Throwable>()
        override fun logging(message: String, symbol: KSNode?) = Unit
        override fun info(message: String, symbol: KSNode?) = Unit
        override fun warn(message: String, symbol: KSNode?) = Unit
        override fun error(message: String, symbol: KSNode?) {
            errors += Error(message, symbol)
        }

        override fun exception(e: Throwable) {
            exceptions += e
        }
    }

    private companion object {
        const val JvmTarget = "17"
        const val LanguageVersion = "2.4"
        val processors = ServiceLoader.load(SymbolProcessorProvider::class.java).toList()
    }

}
