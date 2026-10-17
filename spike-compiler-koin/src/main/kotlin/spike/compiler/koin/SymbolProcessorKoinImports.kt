package spike.compiler.koin

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import spike.compiler.conversion.SymbolSource

/**
 * Imports each type named by @FromKoin once, however many entry points name it, in a stable order.
 */
class SymbolProcessorKoinImports(
    private val source: SymbolSource,
    private val import: KoinImport,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        source.symbols(resolver)
            .filterIsInstance<KSClassDeclaration>()
            .flatMap { entryPoint -> imported(entryPoint).map { it to entryPoint } }
            .sortedBy { (type, _) -> type.declaration.qualifiedName?.asString() }
            .distinctBy { (type, _) -> type.declaration.qualifiedName?.asString() }
            .forEach { (type, entryPoint) -> import.write(type, entryPoint) }
        return emptyList()
    }

    private fun imported(entryPoint: KSClassDeclaration): List<KSType> = entryPoint.annotations
        .filter(KoinNames.FromKoin::matches)
        .flatMap { annotation ->
            (annotation.arguments.single().value as? List<*>).orEmpty().filterIsInstance<KSType>()
        }
        .toList()
}
