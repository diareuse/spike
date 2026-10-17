package spike.compiler.koin

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.EntryPoint
import spike.Include
import spike.compiler.conversion.AnnotationNames
import spike.compiler.conversion.QualifierLookupMeta
import spike.compiler.conversion.SpikeNames
import spike.compiler.conversion.SymbolProcessorAll
import spike.compiler.conversion.SymbolProcessorOnce
import spike.compiler.conversion.SymbolProcessorRequiring
import spike.compiler.conversion.SymbolProcessorUnchecked
import spike.compiler.conversion.SymbolSourceAnnotated
import spike.compiler.conversion.SymbolSourceFiltered

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
fun koinProcessor(environment: SymbolProcessorEnvironment): SymbolProcessor {
    val qualifiers = QualifierLookupMeta(KoinNames.SpikeQualifier, converted = AnnotationNames())
    val members = ExportedMembersAll(
        listOf(ExportedMembersProperties(qualifiers), ExportedMembersFunctions(qualifiers)),
    )
    val entryPoints = SymbolSourceFiltered(SymbolSourceAnnotated(KoinNames.EntryPoint)) { it is KSClassDeclaration }
    val exports = SymbolProcessorUnchecked(
        entryPoints,
        KoinModuleConversionFile(members, environment.codeGenerator),
    )
    val imports = SymbolProcessorKoinImports(
        SymbolSourceAnnotated(KoinNames.FromKoin),
        KoinImportFile(SpikeNames.Include, environment.codeGenerator),
    )
    val both = SymbolProcessorAll(listOf(exports, imports))
    return SymbolProcessorOnce(SymbolProcessorRequiring(KoinNames.Koin, both))
}
