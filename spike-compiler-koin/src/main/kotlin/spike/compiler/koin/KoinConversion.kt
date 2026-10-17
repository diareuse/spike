package spike.compiler.koin

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.BindTarget
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.Order
import spike.compiler.conversion.QualifierLookup
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolProcessorRequiring
import spike.compiler.conversion.SymbolProcessorUnchecked
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered
import spike.compiler.conversion.ordered

@EntryPoint
interface KoinConversion {
    val processor: SymbolProcessor

    @EntryPoint.Factory
    interface Factory {
        fun create(environment: SymbolProcessorEnvironment): KoinConversion
    }

    companion object
}

@Include
fun koinProcessor(processors: Map<Int, SymbolProcessor>): SymbolProcessor =
    SymbolProcessorOnce(SymbolProcessorRequiring(KoinNames.Koin, SymbolProcessorAll(processors.ordered())))

@Include(bindTo = BindTarget.Map)
@Order(10)
fun koinExports(conversion: KoinModuleConversionFile): SymbolProcessor = SymbolProcessorUnchecked(
    SymbolSourceFiltered(SymbolSourceAnnotated(KoinNames.EntryPoint)) { it is KSClassDeclaration },
    conversion,
)

@Include(bindTo = BindTarget.Map)
@Order(20)
fun koinImports(import: KoinImport): SymbolProcessor =
    SymbolProcessorKoinImports(SymbolSourceAnnotated(KoinNames.FromKoin), import)

@Include
fun koinQualifiers(): QualifierLookup = QualifierLookupMeta(KoinNames.SpikeQualifier, converted = AnnotationNames())

@Include
fun SymbolProcessorEnvironment.koinCodeGenerator(): CodeGenerator = codeGenerator
