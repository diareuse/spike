package spike.compiler.kotlininject

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.ProblemReporter
import spike.compiler.conversion.ProblemReporterLogger
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.ScopeLookup
import spike.compiler.conversion.ScopeLookupAnnotation
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.ordered

@EntryPoint
interface KotlinInjectConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): KotlinInjectConversion
    }

    companion object
}

internal const val QUALIFIER_CODE = "SPIKE-KI-QUALIFIER"
internal const val QUALIFIER_MESSAGE =
    "kotlin-inject qualifiers are not converted; annotate the qualifier with @spike.Qualifier instead."

@Include
fun kotlinInjectProcessor(processors: Map<Int, SymbolProcessor>): SymbolProcessor =
    SymbolProcessorOnce(SymbolProcessorAll(processors.ordered()))

@Include
fun kotlinInjectReporter(logger: KSPLogger): ProblemReporter = ProblemReporterLogger(logger)

@Include
fun SymbolProcessorEnvironment.kotlinInjectLogger(): KSPLogger = logger

@Include
fun SymbolProcessorEnvironment.kotlinInjectCodeGenerator(): CodeGenerator = codeGenerator

@Include
fun kotlinInjectQualifiers(): QualifierLookup =
    QualifierLookupMeta(KotlinInjectNames.Qualifier, converted = AnnotationNames())

@Include
fun kotlinInjectScopes(): ScopeLookup = ScopeLookupAnnotation(singleton = AnnotationNames(), KotlinInjectNames.Scope)
