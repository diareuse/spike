package spike.compiler.processor.symbol

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import spike.Include
import spike.compiler.diagnostic.DiagnosticUnresolvedCompiled
import spike.compiler.diagnostic.Involved
import spike.compiler.processor.asOrigin
import spike.compiler.processor.described

@Include
class UnresolvedExportImportsConstructor(
    private val unresolved: UnresolvedTypes,
    private val registry: SymbolRegistry,
) : UnresolvedExportImports {
    override fun of(resolver: Resolver, generated: List<KSClassDeclaration>): ExportImports {
        val hidden = generated.associate { export -> export.qualifiedName?.asString().orEmpty() to export.hidden() }
        val consumers = (registry.entryPoint(resolver) + registry.export(resolver))
            .map { Involved(it.described, it.asOrigin()) }
            .toList()
        val diagnostics = generated.flatMap { export ->
            hidden.getValue(export.qualifiedName?.asString().orEmpty()).values.distinct()
                .map { type -> DiagnosticUnresolvedCompiled(type, export.exportName(), consumers) }
        }
        return ExportImports(
            diagnostics = diagnostics.takeIf { consumers.isNotEmpty() }.orEmpty(),
            hidden = hidden.mapValues { (_, parameters) -> parameters.keys },
        )
    }

    private fun KSClassDeclaration.hidden(): Map<String, String> = listOfNotNull(primaryConstructor)
        .flatMap { constructor ->
            constructor.parameters.mapNotNull { parameter ->
                unresolved.of(sequenceOf(parameter.type).resolved(constructor)).firstOrNull()
                    ?.let { parameter.name!!.asString() to it.type }
            }
        }
        .toMap()
}

fun KSClassDeclaration.exportName(): String {
    val exported = superTypes.first().resolve().declaration
    return "export ${exported.qualifiedName?.asString() ?: exported.simpleName.asString()}"
}
