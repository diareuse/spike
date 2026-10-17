package spike.compiler.dagger

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.ordered

@EntryPoint
interface DaggerConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): DaggerConversion
    }

    companion object
}

@Include
fun daggerProcessor(processors: Map<Int, SymbolProcessor>): SymbolProcessor =
    SymbolProcessorOnce(SymbolProcessorAll(processors.ordered()))

@Include
fun daggerReporter(logger: KSPLogger): ProblemReporter = ProblemReporterLogger(logger)

@Include
fun SymbolProcessorEnvironment.daggerLogger(): KSPLogger = logger

@Include
fun SymbolProcessorEnvironment.daggerCodeGenerator(): CodeGenerator = codeGenerator
