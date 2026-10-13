package spike.compiler.harness

import com.google.devtools.ksp.impl.KotlinSymbolProcessing
import com.google.devtools.ksp.processing.KSPJvmConfig
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import spike.compiler.SpikeSymbolProcessorProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

class SourceModule(
    private val sources: File,
    private val libraries: List<File>,
    private val workDirectory: File
) {

    val generatedSources: File = workDirectory.resolve("ksp/kotlin")
    val classes: File = workDirectory.resolve("classes")

    fun process(): Outcome {
        val errors = ErrorCollector()
        val exitCode = KotlinSymbolProcessing(kspConfig(), listOf(SpikeSymbolProcessorProvider()), errors).execute()
        return Outcome(exitCode == KotlinSymbolProcessing.ExitCode.OK, errors.messages.joinToString("\n"))
    }

    fun compile(): Outcome {
        val messages = ByteArrayOutputStream()
        val exitCode = K2JVMCompiler().exec(
            PrintStream(messages),
            sources.path,
            generatedSources.path,
            "-d", classes.path,
            "-classpath", libraries.joinToString(File.pathSeparator),
            "-no-stdlib",
            "-no-reflect",
            "-jvm-target", JvmTarget
        )
        return Outcome(exitCode == ExitCode.OK, messages.toString())
    }

    private fun kspConfig() = KSPJvmConfig.Builder().apply {
        moduleName = sources.parentFile.name
        sourceRoots = listOf(sources)
        javaSourceRoots = emptyList()
        commonSourceRoots = emptyList()
        libraries = this@SourceModule.libraries
        jdkHome = File(System.getProperty("java.home"))
        jvmTarget = JvmTarget
        languageVersion = KotlinVersion.CURRENT.run { "$major.$minor" }
        apiVersion = languageVersion
        projectBaseDir = workDirectory
        outputBaseDir = workDirectory
        cachesDir = workDirectory.resolve("caches")
        classOutputDir = workDirectory.resolve("ksp/classes")
        kotlinOutputDir = generatedSources
        javaOutputDir = workDirectory.resolve("ksp/java")
        resourceOutputDir = workDirectory.resolve("ksp/resources")
    }.build()

    data class Outcome(val succeeded: Boolean, val messages: String)

    private class ErrorCollector : KSPLogger {
        val messages = mutableListOf<String>()
        override fun logging(message: String, symbol: KSNode?) = Unit
        override fun info(message: String, symbol: KSNode?) = Unit
        override fun warn(message: String, symbol: KSNode?) = Unit
        override fun error(message: String, symbol: KSNode?) {
            messages += message
        }

        override fun exception(e: Throwable) {
            messages += e.stackTraceToString()
        }
    }

    private companion object {
        const val JvmTarget = "17"
    }

}
