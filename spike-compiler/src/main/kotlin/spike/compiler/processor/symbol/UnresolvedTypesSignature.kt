package spike.compiler.processor.symbol

import com.google.devtools.ksp.isConstructor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSTypeReference
import spike.Include
import spike.compiler.diagnostic.Diagnostic
import spike.compiler.diagnostic.DiagnosticUnresolvedType
import spike.compiler.diagnostic.Involved
import spike.compiler.processor.asOrigin
import spike.compiler.processor.described

@Include
class UnresolvedTypesSignature(private val references: SignatureReferences) : UnresolvedTypes {
    override fun of(symbols: List<KSAnnotated>): List<Diagnostic> = symbols.asSequence()
        .flatMap(references::of)
        .distinct()
        .mapNotNull { reference ->
            val name = reference.unresolvedName() ?: return@mapNotNull null
            DiagnosticUnresolvedType(name, Involved(reference.user(), reference.asOrigin()))
        }
        .toList()

    private fun KSTypeReference.unresolvedName(): String? {
        val type = resolve()
        if (type.isError) return type.declaration.simpleName.asString().withoutErrorMarker()
        return type.arguments.firstNotNullOfOrNull { it.type?.unresolvedName() }
    }

    private fun String.withoutErrorMarker() = removeSurrounding(ErrorMarker, ">")

    private fun KSNode.user(): String = when (val node = parent) {
        null -> "this declaration"
        is KSDeclaration -> node.usage()
        else -> node.user()
    }

    private fun KSDeclaration.usage(): String {
        if (this !is KSFunctionDeclaration || !isConstructor()) return described
        return "constructor of ${parentDeclaration?.simpleName?.asString()}"
    }

    private companion object {
        const val ErrorMarker = "<ERROR TYPE: "
    }
}
