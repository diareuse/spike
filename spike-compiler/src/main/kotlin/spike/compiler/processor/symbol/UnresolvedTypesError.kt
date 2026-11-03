package spike.compiler.processor.symbol

import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSType
import spike.Include
import spike.compiler.graph.errorTypeName

@Include
class UnresolvedTypesError : UnresolvedTypes {
    override fun of(types: Sequence<SignatureType>): Sequence<UnresolvedReference> = types
        .mapNotNull { signature ->
            val name = signature.type.unresolvedName() ?: return@mapNotNull null
            UnresolvedReference(signature.annotationName() ?: name, signature.node, signature.owner)
        }

    private fun SignatureType.annotationName(): String? {
        val annotation = node.parent as? KSAnnotation ?: return null
        return annotation.shortName.asString().takeUnless { it == UnknownName } ?: "an annotation"
    }

    private companion object {
        const val UnknownName = "<error>"
    }
}

private fun KSType.unresolvedName(): String? {
    if (isError) return declaration.simpleName.asString().let { errorTypeName(it) ?: it }
    return arguments.firstNotNullOfOrNull { it.type?.resolve()?.unresolvedName() }
}

