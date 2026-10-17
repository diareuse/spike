package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSType
import spike.compiler.diagnostic.Involved

/**
 * Reads what an @Replaces declaration replaces and what it provides instead, both qualified as it is.
 */
class ReplacementReader(private val provided: ProvidedTypeVisitor) {
    fun of(symbol: KSAnnotated, base: KSClassDeclaration): Replacement {
        val replacedType = symbol.annotations
            .first { it.shortName.asString() == "Replaces" }
            .arguments.single().value as KSType
        val providedType = symbol.accept(provided, Unit)
        val qualifiers = symbol.findQualifiers()
        val declaration = symbol as KSDeclaration
        return Replacement(
            base = base,
            replacedType = replacedType,
            providedType = providedType,
            replaced = replacedType.toType().qualifiedBy(qualifiers),
            provided = providedType?.toType()?.qualifiedBy(qualifiers),
            involved = Involved("${declaration.kind} ${declaration.qualifiedName?.asString()}", symbol.asOrigin()),
        )
    }
}
