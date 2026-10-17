package spike.compiler.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import spike.compiler.diagnostic.Involved
import spike.compiler.graph.Alias
import spike.compiler.graph.Type

/**
 * A test declaration annotated @Replaces, which takes the place of [replaced] in the production graph of [base].
 */
class Replacement(
    val base: KSClassDeclaration,
    val replacedType: KSType,
    val providedType: KSType?,
    val replaced: Type,
    val provided: Type?,
    val involved: Involved,
) {
    val replacedName get() = replaced.toString().removePrefix(".")
    val providedName get() = provided.toString().removePrefix(".")
    val alias: Alias? get() = provided?.takeIf { it != replaced }?.let { Alias(replaced, it, involved.origin) }
}
