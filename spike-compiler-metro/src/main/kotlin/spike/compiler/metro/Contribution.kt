package spike.compiler.metro

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Origin
import com.squareup.kotlinpoet.ksp.toClassName
import spike.compiler.conversion.Included

/**
 * A class that Metro binds to its supertype through @ContributesBinding or @ContributesIntoSet.
 */
class Contribution(override val declaration: KSClassDeclaration) : Included {
    override val packageName get() = declaration.toClassName().packageName
    override val fileName get() = declaration.toClassName().simpleNames.joinToString("") + "_SpikeContributed"

    val supertypes get() = declaration.superTypes.map { it.resolve() }
        .filter { it.declaration.qualifiedName?.asString() != "kotlin.Any" }
        .toList()

    val hasExplicitBinding
        get() = declaration.annotations.any { annotation ->
            annotation.arguments.any { it.name?.asString() == "binding" && it.origin != Origin.SYNTHETIC }
        }

    companion object {
        fun of(symbol: KSAnnotated) = (symbol as? KSClassDeclaration)?.let(::Contribution)
    }
}
