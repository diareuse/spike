package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.DiagnosticUnresolvedCompiled
import spike.compiler.diagnostic.Involved
import spike.compiler.processor.asOrigin
import spike.compiler.processor.described

@Include
class UnresolvedExportImportsConstructor(
    private val unresolved: UnresolvedTypes,
    private val registry: SymbolRegistry,
) : UnresolvedExportImports {
    override fun of(resolver: Resolver, generated: List<KSClassDeclaration>): List<Diagnostic> {
        val consumers = (registry.entryPoint(resolver) + registry.export(resolver))
            .map { Involved(it.described, it.asOrigin()) }
            .toList()
        if (consumers.isEmpty()) return emptyList()
        return generated.flatMap { export -> unresolvedImports(export) }
            .map { DiagnosticUnresolvedCompiled(it.type, it.compiled, consumers) }
    }

    private fun unresolvedImports(export: KSClassDeclaration): List<UnresolvedCompiledType> {
        val types = listOfNotNull(export.primaryConstructor).asSequence()
            .flatMap { constructor -> constructor.parameters.asSequence().map { it.type }.resolved(constructor) }
        return unresolved.of(types).map { UnresolvedCompiledType(it.type, export.exportName()) }.distinct().toList()
    }
}

fun KSClassDeclaration.exportName(): String {
    val exported = superTypes.first().resolve().declaration
    return "export ${exported.qualifiedName?.asString() ?: exported.simpleName.asString()}"
}
